package com.fred.orderreport.orchestration;

import com.fred.orderreport.domain.model.*;
import com.fred.orderreport.domain.model.result.DiscountResult;
import com.fred.orderreport.domain.result.CustomerReportData;
import com.fred.orderreport.domain.service.CurrencyConverter;
import com.fred.orderreport.domain.service.calculator.*;
import com.fred.orderreport.infrastructure.csv.*;
import com.fred.orderreport.infrastructure.export.JsonReportExporter;
import com.fred.orderreport.infrastructure.export.JsonReportMapper;
import com.fred.orderreport.infrastructure.formatter.ReportFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.*;

/**
 * Legacy Order Report Generator
 * DO NOT USE IN PRODUCTION
 */
@Service
@RequiredArgsConstructor
public class ReportGenerationService {

    // ---------- SERVICE INJECTION ---------- //
    private final ProductCsvParser productCsvParser;
    private final CustomerCsvParser customerCsvParser;
    private final ShippingZoneCsvParser shippingZoneCsvParser;
    private final PromotionCsvParser promotionCsvParser;
    private final OrderCsvParser orderCsvParser;

    private final LoyaltyCalculator loyaltyCalculator;
    private final DiscountCalculator discountCalculator;
    private final TaxCalculator taxCalculator;
    private final ShippingCalculator shippingCalculator;
    private final HandlingCalculator handlingCalculator;
    private final CurrencyConverter currencyConverter;
    private final PromotionCalculator promotionCalculator;
    private final OrderPricingCalculator orderPricingCalculator;

    private final ReportFormatter reportFormatter;

    private final JsonReportMapper jsonReportMapper;
    private final JsonReportExporter jsonReportExporter;

    public String run() throws Exception {
        Path custPath = getResourcePath("data/customers.csv");
        Path ordPath = getResourcePath("data/orders.csv");
        Path prodPath = getResourcePath("data/products.csv");
        Path shipPath = getResourcePath("data/shipping_zones.csv");
        Path promoPath = getResourcePath("data/promotions.csv");

        // Lecture customers
        Map<String, Customer> customers = customerCsvParser.parse(custPath);

        // Lecture products
        Map<String, Product> products = productCsvParser.parse(prodPath);

        // Lecture shipping zones
        Map<String, ShippingZone> shippingZones = shippingZoneCsvParser.parse(shipPath);

        // Lecture promotions
        Map<String, Promotion> promotions = promotionCsvParser.parse(promoPath);

        // Lecture orders
        List<Order> orders = orderCsvParser.parse(ordPath);

        // Calcul points de fidélité
        Map<String, Double> loyaltyPoints = loyaltyCalculator.calculate(orders);

        // Agrégation des commandes par client
        Map<String, Map<String, Object>> totalsByCustomer = new HashMap<>();
        for (Order order : orders) {
            String cid = order.getCustomerId();

            // Récupération produit avec fallback
            Product prod = products.get(order.getProductId());
            double basePrice = prod != null ? prod.getPrice() : order.getUnitPrice();

            // Application promo
            String promoCode = order.getPromoCode();

            double discountRate = promotionCalculator.calculateDiscountRate(promoCode, promotions);
            double fixedDiscount = promotionCalculator.calculateFixedDiscount(promoCode, promotions);

            // Calcul ligne avec réduction promo
            int qty = order.getQuantity();
            double lineTotal = orderPricingCalculator.calculateLineTotal(qty, basePrice, discountRate, fixedDiscount);

            // Bonus matin (règle cachée basée sur heure)
            double morningBonus = orderPricingCalculator.calculateMorningBonus(lineTotal, order.getTime());

            lineTotal = lineTotal - morningBonus;

            if (!totalsByCustomer.containsKey(cid)) {
                Map<String, Object> totals = new HashMap<>();
                totals.put("subtotal", 0.0);
                totals.put("items", new ArrayList<Order>());
                totals.put("weight", 0.0);
                totals.put("promo_discount", 0.0);
                totals.put("morning_bonus", 0.0);
                totalsByCustomer.put(cid, totals);
            }

            Map<String, Object> totals = totalsByCustomer.get(cid);
            totals.put("subtotal", (Double) totals.get("subtotal") + lineTotal);
            double weight = prod != null ? prod.getWeight() : 1.0;

            totals.put("weight", (Double) totals.get("weight") + weight * qty);
            ((List<Order>) totals.get("items")).add(order);
            totals.put("morning_bonus", (Double) totals.get("morning_bonus") + morningBonus);
        }

        // Agrégation des données métier par client
        List<CustomerReportData> reportData = new ArrayList<>();
        double grandTotal = 0.0;
        double totalTaxCollected = 0.0;

        // Tri par ID client (comportement à préserver)
        List<String> sortedCustomerIds = new ArrayList<>(totalsByCustomer.keySet());
        Collections.sort(sortedCustomerIds);

        for (String cid : sortedCustomerIds) {
            Customer cust = customers.get(cid);
            String name = cust != null ? cust.getName() : "Unknown";
            String level = cust != null ? cust.getLevel() : "BASIC";
            String zone = cust != null ? cust.getShippingZone() : "ZONE1";
            String currency = cust != null ? cust.getCurrency() : "EUR";

            Map<String, Object> totals = totalsByCustomer.get(cid);
            double sub = (Double) totals.get("subtotal");

            // Remise par paliers (duplication + magic numbers)
            List<Order> items = (List<Order>) totals.get("items");

            double disc = discountCalculator.calculateVolumeDiscount(sub, level, items);

            // Calcul remise fidélité (duplication)
            double pts = loyaltyPoints.getOrDefault(cid, 0.0);
            double loyaltyDiscount = discountCalculator.calculateLoyaltyDiscount(pts);

            // Plafond remise global (règle cachée)
            DiscountResult discountResult = discountCalculator.applyDiscountCap(disc, loyaltyDiscount);
            disc = discountResult.getVolumeDiscount();
            loyaltyDiscount = discountResult.getLoyaltyDiscount();
            double totalDiscount = discountResult.getTotalDiscount();

            // Calcul taxe (gestion spéciale par produit)
            double taxable = sub - totalDiscount;

            double tax = taxCalculator.calculate(sub, totalDiscount, items, products);

            // Frais de port
            double weight = (Double) totals.get("weight");
            double ship = shippingCalculator.calculate(sub, weight, zone, shippingZones);

            // Frais de gestion (magic number + condition cachée)
            int itemCount = items.size();

            double handling = handlingCalculator.calculate(itemCount);

            // Conversion devise (règle cachée pour non-EUR)
            double currencyRate = currencyConverter.getRate(currency);

            double total = Math.round((taxable + tax + ship + handling) * currencyRate * 100.0) / 100.0;
            grandTotal += total;
            totalTaxCollected += tax * currencyRate;

            double morningBonus = (Double) totals.get("morning_bonus");

            // Consolidation des données du rapport avant formatage/export
            reportData.add(
                    new CustomerReportData(
                            cid,
                            name,
                            level,
                            zone,
                            currency,
                            sub,
                            totalDiscount,
                            disc,
                            loyaltyDiscount,
                            morningBonus,
                            tax * currencyRate,
                            ship,
                            weight,
                            itemCount,
                            handling,
                            total,
                            pts
                    )
            );
        }

        // Formatage texte
        List<String> outputLines = reportFormatter.format(reportData, grandTotal, totalTaxCollected);

        String result = String.join("\n", outputLines);

        // Opérations d’I/O finales
        System.out.println(result);

        // Export JSON
        List<Map<String, Object>> jsonData = jsonReportMapper.map(reportData);
        jsonReportExporter.export(jsonData, "target/output.json");

        return result;
    }

    private Path getResourcePath(String resourceName) throws Exception {
        return Path.of(
                Objects.requireNonNull(
                        ReportGenerationService.class
                                .getClassLoader()
                                .getResource(resourceName)
                ).toURI()
        );
    }
}

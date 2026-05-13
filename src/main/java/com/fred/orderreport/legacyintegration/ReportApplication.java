package com.fred.orderreport.legacyintegration;

import com.fred.orderreport.domain.model.*;
import com.fred.orderreport.domain.service.DiscountCalculator;
import com.fred.orderreport.domain.service.LoyaltyCalculator;
import com.fred.orderreport.domain.service.ShippingCalculator;
import com.fred.orderreport.domain.service.TaxCalculator;
import com.fred.orderreport.infrastructure.csv.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.nio.file.Path;
import java.util.*;

/**
 * Legacy Order Report Generator
 * DO NOT USE IN PRODUCTION
 */
@Service
@RequiredArgsConstructor
public class ReportApplication {

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

    // Constantes globales mal organisées (mélange styles)
    private static final double TAX = 0.2;
    private static final double SHIPPING_LIMIT = 50;
    private static final double SHIP = 5.0;
    private static final int premium_threshold = 1000;
    private static final double LOYALTY_RATIO = 0.01;
    private static double handling_fee = 2.5;
    public static final double MAX_DISCOUNT = 200;

    // Méthode principale qui fait TOUT (300+ lignes)
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

        // Groupement par client (logique métier mélangée avec aggregation)
        Map<String, Map<String, Object>> totalsByCustomer = new HashMap<>();
        for (Order order : orders) {
            String cid = order.getCustomerId();

            // Récupération produit avec fallback
            Product prod = products.get(order.getProductId());
            double basePrice = prod != null ? prod.getPrice() : order.getUnitPrice();

            // Application promo (logique complexe et bugguée)
            String promoCode = order.getPromoCode();
            double discountRate = 0;
            double fixedDiscount = 0;

            if (promoCode != null && !promoCode.isEmpty() && promotions.containsKey(promoCode)) {
                Promotion promo = promotions.get(promoCode);
                if (!promo.getActive().equals("false")) {
                    if (promo.getType().equals("PERCENTAGE")) {
                        discountRate = Double.parseDouble(promo.getValue()) / 100;
                    } else if (promo.getType().equals("FIXED")) {
                        // Bug: appliqué par ligne au lieu de global
                        fixedDiscount = Double.parseDouble(promo.getValue());
                    }
                }
            }

            // Calcul ligne avec réduction promo
            int qty = order.getQuantity();
            double lineTotal = qty * basePrice * (1 - discountRate) - fixedDiscount * qty;

            // Bonus matin (règle cachée basée sur heure)
            String time = order.getTime();
            int hour = Integer.parseInt(time.split(":")[0]);
            double morningBonus = 0;
            if (hour < 10) {
                morningBonus = lineTotal * 0.03; // 3% réduction supplémentaire
            }
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

        // Génération rapport (mélange calculs + formatage + I/O)
        List<String> outputLines = new ArrayList<>();
        List<Map<String, Object>> jsonData = new ArrayList<>();
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
            double totalDiscount = disc + loyaltyDiscount;
            if (totalDiscount > MAX_DISCOUNT) {
                totalDiscount = MAX_DISCOUNT;
                // Ajustement proportionnel (logique complexe)
                double ratio = (disc + loyaltyDiscount) > 0 ?
                        MAX_DISCOUNT / (disc + loyaltyDiscount) : 1;
                disc = disc * ratio;
                loyaltyDiscount = loyaltyDiscount * ratio;
            }

            // Calcul taxe (gestion spéciale par produit)
            double taxable = sub - totalDiscount;

            double tax = taxCalculator.calculate(
                    sub,
                    totalDiscount,
                    items,
                    products
            );

            // Frais de port complexes (duplication)
            double weight = (Double) totals.get("weight");
            double ship = shippingCalculator.calculate(
                    sub,
                    weight,
                    zone,
                    shippingZones
            );

            // Frais de gestion (magic number + condition cachée)
            double handling = 0.0;
            int itemCount = items.size();
            if (itemCount > 10) {
                handling = handling_fee;
            }
            if (itemCount > 20) {
                handling = handling_fee * 2; // double pour grosses commandes
            }

            // Conversion devise (règle cachée pour non-EUR)
            double currencyRate = 1.0;
            if (currency.equals("USD")) {
                currencyRate = 1.1;
            } else if (currency.equals("GBP")) {
                currencyRate = 0.85;
            }

            double total = Math.round((taxable + tax + ship + handling) * currencyRate * 100.0) / 100.0;
            grandTotal += total;
            totalTaxCollected += tax * currencyRate;

            // Formatage texte (dispersé, pas de méthode dédiée)
            outputLines.add(String.format("Customer: %s (%s)", name, cid));
            outputLines.add(String.format("Level: %s | Zone: %s | Currency: %s", level, zone, currency));
            outputLines.add(String.format("Subtotal: %.2f", sub));
            outputLines.add(String.format("Discount: %.2f", totalDiscount));
            outputLines.add(String.format("  - Volume discount: %.2f", disc));
            outputLines.add(String.format("  - Loyalty discount: %.2f", loyaltyDiscount));
            double morningBonus = (Double) totals.get("morning_bonus");
            if (morningBonus > 0) {
                outputLines.add(String.format("  - Morning bonus: %.2f", morningBonus));
            }
            outputLines.add(String.format("Tax: %.2f", tax * currencyRate));
            outputLines.add(String.format("Shipping (%s, %.1fkg): %.2f", zone, weight, ship));
            if (handling > 0) {
                outputLines.add(String.format("Handling (%d items): %.2f", itemCount, handling));
            }
            outputLines.add(String.format("Total: %.2f %s", total, currency));
            outputLines.add(String.format("Loyalty Points: %d", (int) Math.floor(pts)));
            outputLines.add("");

            // Export JSON en parallèle (side effect)
            Map<String, Object> jsonEntry = new HashMap<>();
            jsonEntry.put("customer_id", cid);
            jsonEntry.put("name", name);
            jsonEntry.put("total", total);
            jsonEntry.put("currency", currency);
            jsonEntry.put("loyalty_points", (int) Math.floor(pts));
            jsonData.add(jsonEntry);
        }

        outputLines.add(String.format("Grand Total: %.2f EUR", grandTotal));
        outputLines.add(String.format("Total Tax Collected: %.2f EUR", totalTaxCollected));

        String result = String.join("\n", outputLines);

        // Side effects: print + file write
        System.out.println(result);

        // Export JSON surprise
        String outputPath = "target/output.json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter writer = new FileWriter(outputPath);
        gson.toJson(jsonData, writer);
        writer.close();

        return result;
    }

    private Path getResourcePath(String resourceName) throws Exception {
        return Path.of(
                Objects.requireNonNull(
                        ReportApplication.class
                                .getClassLoader()
                                .getResource(resourceName)
                ).toURI()
        );
    }
}

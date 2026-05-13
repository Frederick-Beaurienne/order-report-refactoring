package com.fred.orderreport.infrastructure.formatter;

import com.fred.orderreport.domain.result.CustomerReportData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Formats report output.
 */
@Service
public class ReportFormatter {

    public List<String> format(List<CustomerReportData> reportData,
                               double grandTotal,
                               double totalTaxCollected) {

        List<String> outputLines = new ArrayList<>();

        for (CustomerReportData data : reportData) {

            addCustomerHeader(outputLines, data.getName(), data.getCustomerId());
            addCustomerDetails(outputLines, data.getLevel(), data.getZone(), data.getCurrency());
            addSubtotal(outputLines, data.getSubtotal());
            addDiscounts(outputLines, data.getTotalDiscount(), data.getVolumeDiscount(), data.getLoyaltyDiscount());
            addMorningBonus(outputLines, data.getMorningBonus());
            addTax(outputLines, data.getTax());
            addShipping(outputLines, data.getZone(), data.getWeight(), data.getShipping());
            addHandling(outputLines, data.getItemCount(), data.getHandling());
            addTotal(outputLines, data.getTotal(), data.getCurrency());
            addLoyaltyPoints(outputLines, data.getLoyaltyPoints());
            addSeparator(outputLines);
        }

        addGlobalTotals(outputLines, grandTotal, totalTaxCollected);

        return outputLines;
    }

    public void addCustomerHeader(List<String> lines,
                                  String name,
                                  String customerId) {
        lines.add(String.format("Customer: %s (%s)", name, customerId));
    }

    public void addCustomerDetails(List<String> lines,
                                   String level,
                                   String zone,
                                   String currency) {

        lines.add(String.format("Level: %s | Zone: %s | Currency: %s", level, zone, currency));
    }

    public void addSubtotal(List<String> lines,
                            double subtotal) {

        lines.add(String.format("Subtotal: %.2f", subtotal));
    }

    public void addDiscounts(List<String> lines,
                             double totalDiscount,
                             double volumeDiscount,
                             double loyaltyDiscount) {

        lines.add(String.format("Discount: %.2f", totalDiscount));
        lines.add(String.format("  - Volume discount: %.2f", volumeDiscount));
        lines.add(String.format("  - Loyalty discount: %.2f", loyaltyDiscount));
    }

    public void addMorningBonus(List<String> lines,
                                double morningBonus) {

        if (morningBonus > 0) {

            lines.add(String.format("  - Morning bonus: %.2f", morningBonus));
        }
    }

    public void addTax(List<String> lines,
                       double tax) {

        lines.add(String.format("Tax: %.2f", tax));
    }

    public void addShipping(List<String> lines,
                            String zone,
                            double weight,
                            double shipping) {

        lines.add(String.format("Shipping (%s, %.1fkg): %.2f", zone, weight, shipping));
    }

    public void addHandling(List<String> lines,
                            int itemCount,
                            double handling) {

        if (handling > 0) {

            lines.add(String.format("Handling (%d items): %.2f", itemCount, handling));
        }
    }

    public void addTotal(List<String> lines,
                         double total,
                         String currency) {

        lines.add(
                String.format("Total: %.2f %s", total, currency));
    }

    public void addLoyaltyPoints(List<String> lines,
                                 double points) {

        lines.add(String.format("Loyalty Points: %d", (int) Math.floor(points)));
    }

    public void addSeparator(List<String> lines) {
        lines.add("");
    }

    public void addGlobalTotals(List<String> lines,
                                double grandTotal,
                                double totalTaxCollected) {

        lines.add(String.format("Grand Total: %.2f EUR", grandTotal));
        lines.add(String.format("Total Tax Collected: %.2f EUR", totalTaxCollected));
    }
}
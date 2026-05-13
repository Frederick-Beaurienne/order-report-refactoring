package com.fred.orderreport.domain.result;

/**
 * Consolidated customer report data.
 */
public class CustomerReportData {

    private final String customerId;

    private final String name;

    private final String level;

    private final String zone;

    private final String currency;

    private final double subtotal;

    private final double totalDiscount;

    private final double volumeDiscount;

    private final double loyaltyDiscount;

    private final double morningBonus;

    private final double tax;

    private final double shipping;

    private final double weight;

    private final int itemCount;

    private final double handling;

    private final double total;

    private final double loyaltyPoints;

    public CustomerReportData(String customerId,
                              String name,
                              String level,
                              String zone,
                              String currency,
                              double subtotal,
                              double totalDiscount,
                              double volumeDiscount,
                              double loyaltyDiscount,
                              double morningBonus,
                              double tax,
                              double shipping,
                              double weight,
                              int itemCount,
                              double handling,
                              double total,
                              double loyaltyPoints) {

        this.customerId = customerId;
        this.name = name;
        this.level = level;
        this.zone = zone;
        this.currency = currency;
        this.subtotal = subtotal;
        this.totalDiscount = totalDiscount;
        this.volumeDiscount = volumeDiscount;
        this.loyaltyDiscount = loyaltyDiscount;
        this.morningBonus = morningBonus;
        this.tax = tax;
        this.shipping = shipping;
        this.weight = weight;
        this.itemCount = itemCount;
        this.handling = handling;
        this.total = total;
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public String getZone() {
        return zone;
    }

    public String getCurrency() {
        return currency;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public double getVolumeDiscount() {
        return volumeDiscount;
    }

    public double getLoyaltyDiscount() {
        return loyaltyDiscount;
    }

    public double getMorningBonus() {
        return morningBonus;
    }

    public double getTax() {
        return tax;
    }

    public double getShipping() {
        return shipping;
    }

    public double getWeight() {
        return weight;
    }

    public int getItemCount() {
        return itemCount;
    }

    public double getHandling() {
        return handling;
    }

    public double getTotal() {
        return total;
    }

    public double getLoyaltyPoints() {
        return loyaltyPoints;
    }
}
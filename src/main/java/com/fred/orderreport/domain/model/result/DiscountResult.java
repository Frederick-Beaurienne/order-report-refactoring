package com.fred.orderreport.domain.model.result;

/**
 * Discount calculation result.
 */
public class DiscountResult {

    private final double volumeDiscount;

    private final double loyaltyDiscount;

    private final double totalDiscount;

    public DiscountResult(double volumeDiscount,
                          double loyaltyDiscount,
                          double totalDiscount) {

        this.volumeDiscount = volumeDiscount;
        this.loyaltyDiscount = loyaltyDiscount;
        this.totalDiscount = totalDiscount;
    }

    public double getVolumeDiscount() {
        return volumeDiscount;
    }

    public double getLoyaltyDiscount() {
        return loyaltyDiscount;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }
}
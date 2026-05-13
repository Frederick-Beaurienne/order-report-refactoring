package com.fred.orderreport.domain.service.calculator;

import org.springframework.stereotype.Service;

/**
 * Calculates order line pricing.
 */
@Service
public class OrderPricingCalculator {

    public double calculateLineTotal(int quantity,
                                     double basePrice,
                                     double discountRate,
                                     double fixedDiscount) {

        return quantity * basePrice * (1 - discountRate) - fixedDiscount * quantity;
    }

    public double calculateMorningBonus(double lineTotal,
                                        String time) {

        int hour = Integer.parseInt(time.split(":")[0]);

        if (hour < 10) {

            // Legacy behavior:
            // extra morning discount
            return lineTotal * 0.03;
        }

        return 0;
    }
}
package com.fred.orderreport.domain.service.calculator;

import org.springframework.stereotype.Service;

import static com.fred.orderreport.shared.constants.BusinessConstants.MORNING_DISCOUNT_RATE;
import static com.fred.orderreport.shared.constants.BusinessConstants.MORNING_LIMIT_HOUR;

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

        if (hour < MORNING_LIMIT_HOUR) {

            // Legacy behavior:
            // extra morning discount
            return lineTotal * MORNING_DISCOUNT_RATE;
        }

        return 0;
    }
}
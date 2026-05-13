package com.fred.orderreport.domain.service;

import com.fred.orderreport.domain.model.Promotion;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Calculates promotion discounts.
 */
@Service
public class PromotionCalculator {

    public double calculateDiscountRate(String promoCode,
                                        Map<String, Promotion> promotions) {

        if (promoCode == null
                || promoCode.isEmpty()
                || !promotions.containsKey(promoCode)) {
            return 0;
        }

        Promotion promotion = promotions.get(promoCode);

        if (promotion.getActive().equals("false")) {
            return 0;
        }

        if (promotion.getType().equals("PERCENTAGE")) {
            return Double.parseDouble(
                    promotion.getValue()
            ) / 100;
        }

        return 0;
    }

    public double calculateFixedDiscount(String promoCode,
                                         Map<String, Promotion> promotions) {

        if (promoCode == null
                || promoCode.isEmpty()
                || !promotions.containsKey(promoCode)) {
            return 0;
        }

        Promotion promotion = promotions.get(promoCode);

        if (promotion.getActive().equals("false")) {
            return 0;
        }

        if (promotion.getType().equals("FIXED")) {
            // Legacy behavior:
            // fixed discount applied per line
            return Double.parseDouble(
                    promotion.getValue()
            );
        }

        return 0;
    }
}
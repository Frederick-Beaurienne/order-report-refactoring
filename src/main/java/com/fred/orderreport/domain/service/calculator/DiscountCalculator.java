package com.fred.orderreport.domain.service.calculator;

import com.fred.orderreport.domain.model.Order;
import com.fred.orderreport.domain.model.result.DiscountResult;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.fred.orderreport.shared.constants.BusinessConstants.*;

/**
 * Calculates customer discounts.
 */
@Service
public class DiscountCalculator {

    public double calculateVolumeDiscount(double subtotal,
                                          String customerLevel,
                                          List<Order> items) {

        double discount = 0.0;

        if (subtotal > 50) {
            discount = subtotal * BASIC_DISCOUNT_RATE;
        }

        if (subtotal > 100) {
            // Legacy behavior: overwrites previous discount
            discount = subtotal * ADVANCED_DISCOUNT_RATE;
        }

        if (subtotal > 500) {
            discount = subtotal * PREMIUM_DISCOUNT_RATE;
        }

        if (subtotal > 1000 && customerLevel.equals("PREMIUM")) {
            discount = subtotal * VIP_DISCOUNT_RATE;
        }

        int dayOfWeek = extractDayOfWeek(items);

        // Calendar: 1=Sunday, 7=Saturday
        if (dayOfWeek == Calendar.SATURDAY
                || dayOfWeek == Calendar.SUNDAY) {

            discount = discount * WEEKEND_DISCOUNT_BONUS;
        }

        return discount;
    }

    public double calculateLoyaltyDiscount(double loyaltyPoints) {

        double loyaltyDiscount = 0.0;

        if (loyaltyPoints > LOYALTY_DISCOUNT_THRESHOLD) {
            loyaltyDiscount = Math.min(loyaltyPoints * LOYALTY_DISCOUNT_RATE, MAX_LOYALTY_DISCOUNT);
        }

        if (loyaltyPoints > PREMIUM_LOYALTY_DISCOUNT_THRESHOLD) {
            // Legacy behavior: overwrites previous discount
            loyaltyDiscount = Math.min(loyaltyPoints * PREMIUM_LOYALTY_DISCOUNT_RATE,
                    MAX_PREMIUM_LOYALTY_DISCOUNT);
        }

        return loyaltyDiscount;
    }

    public DiscountResult applyDiscountCap(double volumeDiscount,
                                           double loyaltyDiscount) {

        double totalDiscount =
                volumeDiscount + loyaltyDiscount;

        if (totalDiscount > MAX_DISCOUNT) {

            // Legacy behavior:
            // proportional discount adjustment
            double ratio = totalDiscount > 0 ? MAX_DISCOUNT / totalDiscount : 1;

            volumeDiscount = volumeDiscount * ratio;
            loyaltyDiscount = loyaltyDiscount * ratio;

            totalDiscount = MAX_DISCOUNT;
        }

        return new DiscountResult(
                volumeDiscount,
                loyaltyDiscount,
                totalDiscount
        );
    }

    private int extractDayOfWeek(List<Order> items) {

        String firstOrderDate = !items.isEmpty() ? items.get(0).getDate() : "";

        if (firstOrderDate.isEmpty()) {
            return 0;
        }

        try {

            SimpleDateFormat sdf =
                    new SimpleDateFormat("yyyy-MM-dd");

            Date date = sdf.parse(firstOrderDate);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            return cal.get(Calendar.DAY_OF_WEEK);

        } catch (ParseException e) {
            // Legacy behavior: invalid dates are ignored
            return 0;
        }
    }
}
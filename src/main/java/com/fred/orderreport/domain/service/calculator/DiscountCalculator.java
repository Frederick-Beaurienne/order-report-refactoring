package com.fred.orderreport.domain.service.calculator;

import com.fred.orderreport.domain.model.result.DiscountResult;
import com.fred.orderreport.domain.model.Order;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
            discount = subtotal * 0.05;
        }

        if (subtotal > 100) {
            // Legacy behavior: overwrites previous discount
            discount = subtotal * 0.10;
        }

        if (subtotal > 500) {
            discount = subtotal * 0.15;
        }

        if (subtotal > 1000 && customerLevel.equals("PREMIUM")) {
            discount = subtotal * 0.20;
        }

        int dayOfWeek = extractDayOfWeek(items);

        // Calendar: 1=Sunday, 7=Saturday
        if (dayOfWeek == Calendar.SATURDAY
                || dayOfWeek == Calendar.SUNDAY) {

            discount = discount * 1.05;
        }

        return discount;
    }

    public double calculateLoyaltyDiscount(double loyaltyPoints) {

        double loyaltyDiscount = 0.0;

        if (loyaltyPoints > 100) {
            loyaltyDiscount = Math.min(loyaltyPoints * 0.1, 50.0);
        }

        if (loyaltyPoints > 500) {
            // Legacy behavior: overwrites previous discount
            loyaltyDiscount = Math.min(loyaltyPoints * 0.15, 100.0);
        }

        return loyaltyDiscount;
    }

    public DiscountResult applyDiscountCap(double volumeDiscount,
                                           double loyaltyDiscount) {

        double totalDiscount =
                volumeDiscount + loyaltyDiscount;

        if (totalDiscount > 200) {

            // Legacy behavior:
            // proportional discount adjustment
            double ratio =
                    totalDiscount > 0
                            ? 200 / totalDiscount
                            : 1;

            volumeDiscount =
                    volumeDiscount * ratio;

            loyaltyDiscount =
                    loyaltyDiscount * ratio;

            totalDiscount = 200;
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
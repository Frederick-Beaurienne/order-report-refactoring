package com.fred.orderreport.domain.service.calculator;

import com.fred.orderreport.domain.model.Order;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.fred.orderreport.shared.constants.BusinessConstants.*;

/**
 * Calculates customer loyalty points from orders.
 */
@Service
public class LoyaltyCalculator {

    public Map<String, Double> calculate(List<Order> orders) {

        Map<String, Double> loyaltyPoints = new HashMap<>();

        for (Order order : orders) {

            String customerId = order.getCustomerId();

            loyaltyPoints.putIfAbsent(customerId, 0.0);

            double points =
                    order.getQuantity()
                            * order.getUnitPrice()
                            * LOYALTY_RATIO;

            loyaltyPoints.put(
                    customerId,
                    loyaltyPoints.get(customerId) + points
            );
        }

        return loyaltyPoints;
    }
}
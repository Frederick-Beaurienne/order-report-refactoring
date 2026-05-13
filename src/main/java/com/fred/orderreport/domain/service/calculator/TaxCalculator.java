package com.fred.orderreport.domain.service.calculator;

import com.fred.orderreport.domain.model.Order;
import com.fred.orderreport.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import static com.fred.orderreport.shared.constants.BusinessConstants.*;

/**
 * Calculates taxes for customer orders.
 */
@Service
public class TaxCalculator {

    public double calculate(double subtotal,
                            double totalDiscount,
                            List<Order> items,
                            Map<String, Product> products) {

        double taxable = subtotal - totalDiscount;

        if (allProductsTaxable(items, products)) {
            return round(taxable * TAX_RATE);
        }

        double tax = 0.0;

        for (Order item : items) {

            Product product =
                    products.get(item.getProductId());

            if (product != null && product.isTaxable()) {

                tax += item.getQuantity()
                        * product.getPrice()
                        * TAX_RATE;
            }
        }

        return round(tax);
    }

    private boolean allProductsTaxable(List<Order> items,
                                       Map<String, Product> products) {

        for (Order item : items) {

            Product product =
                    products.get(item.getProductId());

            if (product != null && !product.isTaxable()) {
                return false;
            }
        }

        return true;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
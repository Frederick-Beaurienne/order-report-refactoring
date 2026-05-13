package com.fred.orderreport.domain.service.calculator;

import com.fred.orderreport.domain.model.Order;
import com.fred.orderreport.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Calculates taxes for customer orders.
 */
@Service
public class TaxCalculator {

    private static final double TAX = 0.2;

    public double calculate(double subtotal,
                            double totalDiscount,
                            List<Order> items,
                            Map<String, Product> products) {

        double taxable = subtotal - totalDiscount;

        if (allProductsTaxable(items, products)) {
            return round(taxable * TAX);
        }

        double tax = 0.0;

        for (Order item : items) {

            Product product =
                    products.get(item.getProductId());

            if (product != null && product.isTaxable()) {

                tax += item.getQuantity()
                        * product.getPrice()
                        * TAX;
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
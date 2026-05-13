package com.fred.orderreport.domain.service.calculator;

import com.fred.orderreport.domain.model.ShippingZone;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Calculates shipping costs.
 */
@Service
public class ShippingCalculator {

    private static final double SHIPPING_LIMIT = 50;

    public double calculate(double subtotal,
                            double weight,
                            String zone,
                            Map<String, ShippingZone> shippingZones) {

        double shipping = 0.0;

        if (subtotal < SHIPPING_LIMIT) {

            ShippingZone shippingZone =
                    shippingZones.getOrDefault(
                            zone,
                            new ShippingZone(5.0, 0.5)
                    );

            double baseShipping =
                    shippingZone.getBase();

            if (weight > 10) {

                shipping = baseShipping
                        + (weight - 10)
                        * shippingZone.getPerKg();

            } else if (weight > 5) {

                // Legacy behavior: intermediate weight tier
                shipping = baseShipping
                        + (weight - 5)
                        * 0.3;

            } else {
                shipping = baseShipping;
            }

            // Legacy behavior: remote zone surcharge
            if (zone.equals("ZONE3")
                    || zone.equals("ZONE4")) {

                shipping = shipping * 1.2;
            }

        } else {

            // Legacy behavior: heavy package handling fee
            if (weight > 20) {
                shipping = (weight - 20) * 0.25;
            }
        }

        return shipping;
    }
}
package com.fred.orderreport.domain.service.calculator;

import com.fred.orderreport.domain.model.ShippingZone;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.fred.orderreport.shared.constants.BusinessConstants.*;

/**
 * Calculates shipping costs.
 */
@Service
public class ShippingCalculator {

    public double calculate(double subtotal,
                            double weight,
                            String zone,
                            Map<String, ShippingZone> shippingZones) {

        double shipping = 0.0;

        if (subtotal < SHIPPING_LIMIT) {

            ShippingZone shippingZone =
                    shippingZones.getOrDefault(
                            zone,
                            new ShippingZone(DEFAULT_SHIPPING_BASE, DEFAULT_SHIPPING_PER_KG)
                    );

            double baseShipping =
                    shippingZone.getBase();

            if (weight > HEAVY_WEIGHT_THRESHOLD) {

                shipping = baseShipping
                        + (weight - HEAVY_WEIGHT_THRESHOLD)
                        * shippingZone.getPerKg();

            } else if (weight > MEDIUM_WEIGHT_THRESHOLD) {

                // Legacy behavior: intermediate weight tier
                shipping = baseShipping + (weight - MEDIUM_WEIGHT_THRESHOLD) * INTERMEDIATE_WEIGHT_RATE;

            } else {
                shipping = baseShipping;
            }

            // Legacy behavior: remote zone surcharge
            if (zone.equals("ZONE3")
                    || zone.equals("ZONE4")) {

                shipping = shipping * REMOTE_ZONE_SURCHARGE;
            }

        } else {

            // Legacy behavior: heavy package handling fee
            if (weight > FREE_SHIPPING_WEIGHT_THRESHOLD) {
                shipping = (weight - FREE_SHIPPING_WEIGHT_THRESHOLD) * HEAVY_PACKAGE_RATE;
            }
        }

        return shipping;
    }
}
package com.fred.orderreport.domain.service.calculator;

import org.springframework.stereotype.Service;
import static com.fred.orderreport.shared.constants.BusinessConstants.*;
/**
 * Calculates handling fees.
 */
@Service
public class HandlingCalculator {

    public double calculate(int itemCount) {

        double handling = 0.0;

        if (itemCount > MEDIUM_ORDER_THRESHOLD) {
            handling = HANDLING_FEE;
        }

        if (itemCount > LARGE_ORDER_THRESHOLD) {
            // Legacy behavior: doubled fee for large orders
            handling = HANDLING_FEE * 2;
        }

        return handling;
    }
}
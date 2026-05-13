package com.fred.orderreport.domain.service;

import org.springframework.stereotype.Service;

/**
 * Calculates handling fees.
 */
@Service
public class HandlingCalculator {

    private static final double HANDLING_FEE = 2.5;

    public double calculate(int itemCount) {

        double handling = 0.0;

        if (itemCount > 10) {
            handling = HANDLING_FEE;
        }

        if (itemCount > 20) {
            // Legacy behavior: doubled fee for large orders
            handling = HANDLING_FEE * 2;
        }

        return handling;
    }
}
package com.fred.orderreport.domain.service;

import org.springframework.stereotype.Service;

/**
 * Converts amounts between currencies.
 */
@Service
public class CurrencyConverter {

    public double getRate(String currency) {

        double currencyRate = 1.0;

        if (currency.equals("USD")) {
            currencyRate = 1.1;
        } else if (currency.equals("GBP")) {
            currencyRate = 0.85;
        }

        return currencyRate;
    }
}
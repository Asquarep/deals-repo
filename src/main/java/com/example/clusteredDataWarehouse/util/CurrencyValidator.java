package com.example.clusteredDataWarehouse.util;

import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

@Service
public class CurrencyValidator {

    private final Set<String> validCurrencyCodes;

    public CurrencyValidator() {
        Set<Currency> availableCurrencies = Currency.getAvailableCurrencies();
        validCurrencyCodes = new HashSet<>();
        for (Currency currency : availableCurrencies) {
            validCurrencyCodes.add(currency.getCurrencyCode());
        }
    }

    public boolean isValidCurrencyCode(String currencyCode) {
        return validCurrencyCodes.contains(currencyCode);
    }
}

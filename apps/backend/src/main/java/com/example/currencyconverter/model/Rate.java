package com.example.currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class Rate {
    @JsonProperty("quote_currency")
    private String currency;

    @JsonProperty("quote")
    private BigDecimal rate;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "currency='" + currency + '\'' +
                ", rate=" + rate +
                '}';
    }
}

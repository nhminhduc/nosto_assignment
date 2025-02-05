package com.example.currencyconverter.dto;

import java.math.BigDecimal;

public class CurrencyConversionResponse {
    private String sourceCurrency;
    private String targetCurrency;
    private BigDecimal originalAmount;
    private BigDecimal convertedAmount;
    private String formattedResult;

    // Getters and setters
    public String getSourceCurrency() {
        return sourceCurrency;
    }
    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }
    public String getTargetCurrency() {
        return targetCurrency;
    }
    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }
    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }
    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }
    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }
    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
    public String getFormattedResult() {
        return formattedResult;
    }
    public void setFormattedResult(String formattedResult) {
        this.formattedResult = formattedResult;
    }
}

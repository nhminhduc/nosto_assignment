package com.example.currencyconverter.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CurrencyConversionRequest {

    @NotBlank(message = "Source currency is required")
    private String sourceCurrency;

    @NotBlank(message = "Target currency is required")
    private String targetCurrency;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;

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
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

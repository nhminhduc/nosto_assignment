package com.example.currencyconverter.service;

import com.example.currencyconverter.dto.CurrencyConversionResponse;
import com.example.currencyconverter.model.Rate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyConversionService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionService.class);

    @Value("${exchange.api.url}")
    private String exchangeApiUrl;
    @Value("${currency.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Retrieves exchange rates (relative to EUR) from the external API.
     * The response is cached in Redis to save credit on external API calls.
     *
     * @return a map of currency codes to exchange rates (relative to EUR)
     */
    @Cacheable(value = "exchangeRates", key = "'EUR'")
    public Map<String, BigDecimal> getExchangeRates() {
        // No base parameter needed as the API returns rates relative to EUR.
        String url = exchangeApiUrl;
        logger.debug("Fetching exchange rates from URL: {}", url);

        // Set up HTTP headers with Authorization
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "ApiKey " + apiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            // Request an array of Rate objects
            ResponseEntity<Rate[]> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, entity, Rate[].class);
            Rate[] ratesArray = responseEntity.getBody();
            logger.debug("Received exchange rates response: {}", (Object) ratesArray);

            if (ratesArray != null) {
                Map<String, BigDecimal> ratesMap = new HashMap<>();
                for (Rate r : ratesArray) {
                    ratesMap.put(r.getCurrency(), r.getRate());
                }
                logger.debug("Parsed exchange rates: {}", ratesMap);
                return ratesMap;
            }
            logger.error("Response is null for URL: {}", url);
            throw new RuntimeException("Unable to fetch exchange rates from API: " + url);
        } catch (Exception ex) {
            logger.error("Exception while fetching exchange rates from {}: {}", url, ex.getMessage(), ex);
            throw new RuntimeException("Error fetching exchange rates from API: " + url, ex);
        }
    }

    /**
     * Converts an amount from the source currency to the target currency.
     *
     * @param source the source currency code (e.g., "USD")
     * @param target the target currency code (e.g., "EUR")
     * @param amount the monetary amount to convert
     * @return a CurrencyConversionResponse containing conversion details
     */
    public CurrencyConversionResponse convert(String source, String target, BigDecimal amount) {
        // Fetch rates relative to EUR from cache/Redis
        Map<String, BigDecimal> rates = getExchangeRates();
        if (!rates.containsKey(source) || !rates.containsKey(target)) {
            throw new IllegalArgumentException("Currency not supported: " +
                    (!rates.containsKey(source) ? source : target));
        }
        BigDecimal sourceRate = rates.get(source);
        BigDecimal targetRate = rates.get(target);

        // Convert using EUR as the common base:
        // amount in EUR = amount / sourceRate, then multiply by targetRate
        BigDecimal convertedAmount = amount.divide(sourceRate, 6, RoundingMode.HALF_UP)
                                            .multiply(targetRate);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        currencyFormat.setCurrency(java.util.Currency.getInstance(target));
        String formattedResult = currencyFormat.format(convertedAmount);

        CurrencyConversionResponse response = new CurrencyConversionResponse();
        response.setSourceCurrency(source);
        response.setTargetCurrency(target);
        response.setOriginalAmount(amount);
        response.setConvertedAmount(convertedAmount);
        response.setFormattedResult(formattedResult);

        logger.debug("Conversion result: {}", response);
        return response;
    }
}

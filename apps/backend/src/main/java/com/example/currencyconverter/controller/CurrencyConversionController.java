package com.example.currencyconverter.controller;

import com.example.currencyconverter.dto.CurrencyConversionRequest;
import com.example.currencyconverter.dto.CurrencyConversionResponse;
import com.example.currencyconverter.service.CurrencyConversionService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://172.18.175.98:3000"})
public class CurrencyConversionController {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

    private final CurrencyConversionService conversionService;

    public CurrencyConversionController(CurrencyConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @PostMapping("/convert")
    public ResponseEntity<CurrencyConversionResponse> convertCurrency(
            @Validated @RequestBody CurrencyConversionRequest request) {
        try {
            CurrencyConversionResponse response = conversionService.convert(
                    request.getSourceCurrency().toUpperCase(),
                    request.getTargetCurrency().toUpperCase(),
                    request.getAmount()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            // Return a 400 Bad Request if input is invalid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            // Return a 500 Internal Server Error for other issues
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/currencies")
    public ResponseEntity<List<String>> getAllCurrencies() {
        try {
            logger.info("Fetching all currencies");
            List<String> currencies = getCachedCurrencies();
            logger.info("Fetched {} currencies", currencies.size());
            return ResponseEntity.ok(currencies);
        } catch (Exception ex) {
            logger.error("Error fetching currencies", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Cacheable(value = "allCurrencies")
    private List<String> getCachedCurrencies() {
        return conversionService.getExchangeRates().keySet()
            .stream()
            .collect(Collectors.toList());
    }
}

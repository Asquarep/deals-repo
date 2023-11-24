package com.example.clusteredDataWarehouse.service.impl;

import com.example.clusteredDataWarehouse.dto.request.DealRequest;
import com.example.clusteredDataWarehouse.dto.response.ApiResponse;
import com.example.clusteredDataWarehouse.enums.ResponseCodes;
import com.example.clusteredDataWarehouse.exception.DuplicateException;
import com.example.clusteredDataWarehouse.exception.ValidationException;
import com.example.clusteredDataWarehouse.entities.Deal;
import com.example.clusteredDataWarehouse.repository.DealRepository;
import com.example.clusteredDataWarehouse.service.DealService;
import com.example.clusteredDataWarehouse.util.CurrencyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.example.clusteredDataWarehouse.util.MessageConstants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;

    private final CurrencyValidator currencyValidator;

    @Override
    public ResponseEntity<ApiResponse> submitRequest(DealRequest dealRequest) {
        validateRequest(dealRequest);
        saveDeal(dealRequest);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(ResponseCodes.SUCCESS.getValue())
                .message(SUCCESS)
                .build());
    }

    private void validateRequest(DealRequest dealRequest) {
        boolean dealExists = dealRepository.existsByUniqueId(dealRequest.getUniqueId());
        if (dealExists) {
            throw new DuplicateException(NON_UNIQUE_ID);
        }

        BigDecimal amount = dealRequest.getAmount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(INVALID_DEAL_AMOUNT);
        }

        boolean isValidFromCurrencyCode = currencyValidator.isValidCurrencyCode(dealRequest.getFromCurrency());
        if (!isValidFromCurrencyCode) {
            throw new ValidationException(INVALID_FROM_CURRENCY_CODE);
        }

        boolean isValidToCurrencyCode = currencyValidator.isValidCurrencyCode(dealRequest.getToCurrency());
        if (!isValidToCurrencyCode) {
            throw new ValidationException(INVALID_TO_CURRENCY_CODE);
        }
    }

    private void saveDeal(DealRequest dealRequest) {
        Deal deal = Deal.builder()
                .dealAmount(dealRequest.getAmount())
                .fromCurrency(dealRequest.getFromCurrency())
                .toCurrency(dealRequest.getToCurrency())
                .uniqueId(dealRequest.getUniqueId())
                .build();
        log.info("fx deal saved");
        dealRepository.save(deal);
    }


}

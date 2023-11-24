package com.example.clusteredDataWarehouse;

import com.example.clusteredDataWarehouse.dto.request.DealRequest;
import com.example.clusteredDataWarehouse.entities.Deal;
import com.example.clusteredDataWarehouse.enums.ResponseCodes;
import com.example.clusteredDataWarehouse.repository.DealRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.util.Currency;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DealControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    private final String submitDealUrl = "/api/v1/fx-deals/submit";

    @Test
    public void whenSubmitDeal_WithBlankUniqueId_ShouldReturnExpectedError() throws Exception {
        DealRequest dealRequest = DealRequest.builder()
                .uniqueId("")
                .fromCurrency(faker.lorem().characters(3))
                .toCurrency(faker.lorem().characters(3))
                .amount(BigDecimal.valueOf(faker.number().numberBetween(1_000, 1_000_000)))
                .build();

        mockMvc.perform(post(submitDealUrl)
                        .content(objectMapper.writeValueAsString(dealRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(ResponseCodes.FAILURE.getValue()))
                .andExpect(jsonPath("messages[0]").value(Matchers.containsStringIgnoringCase("uniqueId")))
                .andExpect(jsonPath("messages[0]").value(Matchers.containsStringIgnoringCase("must not be blank")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void whenSubmitDeal_WithBlankFromCurrencyCode_ShouldReturnExpectedError() throws Exception {
        DealRequest dealRequest = DealRequest.builder()
                .uniqueId(faker.lorem().characters(15))
                .fromCurrency("")
                .toCurrency(faker.lorem().characters(3))
                .amount(BigDecimal.valueOf(faker.number().numberBetween(1_000, 1_000_000)))
                .build();

        mockMvc.perform(post(submitDealUrl)
                        .content(objectMapper.writeValueAsString(dealRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(ResponseCodes.FAILURE.getValue()))
                .andExpect(jsonPath("messages[0]").value(Matchers.containsStringIgnoringCase("fromCurrency")))
                .andExpect(jsonPath("messages[0]").value(Matchers.containsStringIgnoringCase("must not be blank")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void whenSubmitDeal_WithBlankToCurrencyCode_ShouldReturnExpectedError() throws Exception {
        DealRequest dealRequest = DealRequest.builder()
                .uniqueId(faker.lorem().characters(15))
                .fromCurrency(faker.lorem().characters(3))
                .toCurrency("")
                .amount(BigDecimal.valueOf(faker.number().numberBetween(1_000, 1_000_000)))
                .build();

        mockMvc.perform(post(submitDealUrl)
                        .content(objectMapper.writeValueAsString(dealRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(ResponseCodes.FAILURE.getValue()))
                .andExpect(jsonPath("messages[0]").value(Matchers.containsStringIgnoringCase("toCurrency")))
                .andExpect(jsonPath("messages[0]").value(Matchers.containsStringIgnoringCase("must not be blank")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void whenSubmitDeal_WithNullAmount_ShouldReturnExpectedError() throws Exception {
        DealRequest dealRequest = DealRequest.builder()
                .uniqueId(faker.lorem().characters(15))
                .fromCurrency(faker.lorem().characters(3))
                .toCurrency(faker.lorem().characters(3))
                .amount(null)
                .build();

        mockMvc.perform(post(submitDealUrl)
                        .content(objectMapper.writeValueAsString(dealRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(ResponseCodes.FAILURE.getValue()))
                .andExpect(jsonPath("messages[0]").value(Matchers.containsStringIgnoringCase("amount")))
                .andExpect(jsonPath("messages[0]").value(Matchers.containsStringIgnoringCase("must not be null")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void whenSubmitDeal_WithValidDetails_ShouldSaveDeal() throws Exception {
        String validCurrencyCode = Currency.getAvailableCurrencies()
                .stream()
                .findFirst()
                .map(Currency::getCurrencyCode)
                .orElse("NGN");

        DealRequest dealRequest = DealRequest.builder()
                .uniqueId(faker.lorem().characters(15))
                .fromCurrency(validCurrencyCode)
                .toCurrency(validCurrencyCode)
                .amount(BigDecimal.valueOf(faker.number().numberBetween(1_000, 1_000_000)))
                .build();

        long initialDealCount = dealRepository.count();

        mockMvc.perform(post(submitDealUrl)
                        .content(objectMapper.writeValueAsString(dealRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        long finalDealCount = dealRepository.count();

        Assertions.assertEquals(initialDealCount + 1, finalDealCount);

        Deal dealSaved = dealRepository.findAll(
                        PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"))
                ).getContent()
                .get(0);

        Assertions.assertEquals(dealRequest.getUniqueId(), dealSaved.getUniqueId());
        Assertions.assertEquals(dealRequest.getFromCurrency(), dealSaved.getFromCurrency());
        Assertions.assertEquals(dealRequest.getToCurrency(), dealSaved.getToCurrency());
        Assertions.assertEquals(0, dealRequest.getAmount().compareTo(dealSaved.getDealAmount()));
        Assertions.assertNotEquals(null, dealSaved.getCreatedAt());
        Assertions.assertNotEquals(0, dealSaved.getId());
    }
}

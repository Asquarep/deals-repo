package com.example.clusteredDataWarehouse;

import com.example.clusteredDataWarehouse.dto.request.DealRequest;
import com.example.clusteredDataWarehouse.exception.DuplicateException;
import com.example.clusteredDataWarehouse.exception.ValidationException;
import com.example.clusteredDataWarehouse.entities.Deal;
import com.example.clusteredDataWarehouse.repository.DealRepository;
import com.example.clusteredDataWarehouse.service.DealService;
import com.example.clusteredDataWarehouse.service.impl.DealServiceImpl;
import com.example.clusteredDataWarehouse.util.CurrencyValidator;
import com.example.clusteredDataWarehouse.util.MessageConstants;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
class DealServiceTests {

	private final Faker faker = new Faker();
	private final DealRepository dealRepository = Mockito.mock(DealRepository.class);
	private final CurrencyValidator currencyValidator = Mockito.mock(CurrencyValidator.class);
	private final DealService dealService = new DealServiceImpl(dealRepository, currencyValidator);

	@Test
	public void whenSubmitDeal_WithDuplicateUniqueId_ShouldThrowDuplicateException() {
		Mockito.doReturn(true)
				.when(dealRepository).existsByUniqueId(Mockito.anyString());

		DealRequest dealRequest = composeDealRequest();

		Throwable expectedException = Assertions.assertThrows(
				DuplicateException.class,
				() -> dealService.submitRequest(dealRequest)
		);

		Assertions.assertEquals(MessageConstants.NON_UNIQUE_ID, expectedException.getMessage());
	}

	@Test
	public void whenSubmitDeal_WithAmountEqualOrLessThanZero_ShouldThrowValidationException() {
		Mockito.doReturn(false)
				.when(dealRepository).existsByUniqueId(Mockito.anyString());

		DealRequest dealRequest = composeDealRequestWithLessThanOrEqualZeroAmount();

		Throwable expectedException = Assertions.assertThrows(
				ValidationException.class,
				() -> dealService.submitRequest(dealRequest)
		);

		Assertions.assertEquals(MessageConstants.INVALID_DEAL_AMOUNT, expectedException.getMessage());
	}

	@Test
	public void whenSubmitDeal_WithInvalidFromCurrencyCode_ShouldThrowValidationException() {
		DealRequest dealRequest = composeDealRequest();

		Mockito.doReturn(false)
				.when(dealRepository).existsByUniqueId(Mockito.anyString());

		Mockito.doReturn(false)
				.when(currencyValidator).isValidCurrencyCode(Mockito.eq(dealRequest.getFromCurrency()));

		Throwable expectedException = Assertions.assertThrows(
				ValidationException.class,
				() -> dealService.submitRequest(dealRequest)
		);

		Assertions.assertEquals(MessageConstants.INVALID_FROM_CURRENCY_CODE, expectedException.getMessage());
	}

	@Test
	public void whenSubmitDeal_WithInvalidToCurrencyCode_ShouldThrowValidationException() {
		DealRequest dealRequest = composeDealRequest();

		Mockito.doReturn(false)
				.when(dealRepository).existsByUniqueId(Mockito.anyString());

		Mockito.doReturn(true)
				.when(currencyValidator).isValidCurrencyCode(Mockito.eq(dealRequest.getFromCurrency()));

		Mockito.doReturn(false)
				.when(currencyValidator).isValidCurrencyCode(Mockito.eq(dealRequest.getToCurrency()));

		Throwable expectedException = Assertions.assertThrows(
				ValidationException.class,
				() -> dealService.submitRequest(dealRequest)
		);

		Assertions.assertEquals(MessageConstants.INVALID_TO_CURRENCY_CODE, expectedException.getMessage());
	}

	@Test
	public void whenSubmitDeal_WithValidDetails_ShouldSaveDeal() {
		Mockito.doReturn(false)
				.when(dealRepository).existsByUniqueId(Mockito.anyString());

		Mockito.doReturn(true)
				.when(currencyValidator).isValidCurrencyCode(Mockito.anyString());

		ArgumentCaptor<Deal> dealCaptor = ArgumentCaptor.forClass(Deal.class);

		Mockito.doReturn(new Deal())
				.when(dealRepository).save(dealCaptor.capture());

		DealRequest dealRequest = composeDealRequest();

		dealService.submitRequest(dealRequest);

		Mockito.verify(dealRepository, Mockito.times(1)).save(Mockito.any(Deal.class));

		Deal dealSaved = dealCaptor.getValue();

		Assertions.assertEquals(dealRequest.getUniqueId(), dealSaved.getUniqueId());
		Assertions.assertEquals(dealRequest.getFromCurrency(), dealSaved.getFromCurrency());
		Assertions.assertEquals(dealRequest.getToCurrency(), dealSaved.getToCurrency());
		Assertions.assertEquals(0, dealRequest.getAmount().compareTo(dealSaved.getDealAmount()));
	}


	private DealRequest composeDealRequest() {
		return DealRequest.builder()
				.uniqueId(faker.lorem().characters(15))
				.fromCurrency(faker.lorem().characters(3))
				.toCurrency(faker.lorem().characters(3))
				.amount(BigDecimal.valueOf(faker.number().numberBetween(1_000, 1_000_000)))
				.build();
	}

	private DealRequest composeDealRequestWithLessThanOrEqualZeroAmount() {
		return DealRequest.builder()
				.uniqueId(faker.lorem().characters(15))
				.fromCurrency(faker.lorem().characters(3))
				.toCurrency(faker.lorem().characters(3))
				.amount(BigDecimal.valueOf(-11))
				.build();
	}
}

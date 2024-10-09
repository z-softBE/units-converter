package be.zsoft.units.converter.service;

import be.zsoft.units.converter.model.CurrencyExchangeRate;
import be.zsoft.units.converter.model.RatioType;
import be.zsoft.units.converter.model.Unit;
import be.zsoft.units.converter.model.UnitType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

    @Mock
    private CurrencyExchangeRateService currencyExchangeRateService;

    @InjectMocks
    private CalculationService calculationService;

    @Test
    void calculateUnit_withBigDecimals() {
        Unit fromUnit = Unit.builder()
                .ratioType(RatioType.BIG_DECIMAL)
                .ratioToParent("0.01")
                .build();
        Unit toUnit = Unit.builder()
                .ratioType(RatioType.BIG_DECIMAL)
                .ratioToParent("1000")
                .build();

        BigDecimal result = calculationService.calculateUnit(fromUnit, toUnit, new BigDecimal("20"));

        assertThat(result).isEqualTo(new BigDecimal("0.00020000000000000000"));
    }

    @Test
    void calculateUnit_withExpressions() {
        Unit fromUnit = Unit.builder()
                .ratioType(RatioType.EXPRESSION)
                .ratioToParent("(0.01 * 2) / ${value}")
                .build();
        Unit toUnit = Unit.builder()
                .ratioType(RatioType.EXPRESSION)
                .ratioToParent("(1000 * 5) / ${value}")
                .build();

        BigDecimal result = calculationService.calculateUnit(fromUnit, toUnit, new BigDecimal("20"));

        assertThat(result).isEqualTo(new BigDecimal("0.00008000000000000000"));
    }

    @Test
    void calculateUnit_noParent() {
        Unit fromUnit = Unit.builder()
                .build();
        Unit toUnit = Unit.builder()
                .build();

        BigDecimal result = calculationService.calculateUnit(fromUnit, toUnit, new BigDecimal("20"));

        assertThat(result).isEqualTo(new BigDecimal("20.00000000000000000000"));
    }

    @Test
    void calculateCurrency_fromNotCurrency() {
        Unit fromUnit = Unit.builder()
                .build();
        Unit toUnit = Unit.builder()
                .build();

        assertThatThrownBy(() -> calculationService.calculateCurrency(fromUnit, toUnit, new BigDecimal("20")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The fromUnit is not a currency");
    }

    @Test
    void calculateCurrency_toNotCurrency() {
        Unit fromUnit = Unit.builder()
                .type(UnitType.CURRENCY)
                .build();
        Unit toUnit = Unit.builder()
                .build();

        assertThatThrownBy(() -> calculationService.calculateCurrency(fromUnit, toUnit, new BigDecimal("20")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The toUnit is not a currency");
    }

    @Test
    void calculateCurrency_success() {
        Unit fromUnit = Unit.builder()
                .type(UnitType.CURRENCY)
                .ratioToParent("EUR")
                .build();
        Unit toUnit = Unit.builder()
                .type(UnitType.CURRENCY)
                .ratioToParent("CAD")
                .build();

        when(currencyExchangeRateService.getByCode("EUR")).thenReturn(CurrencyExchangeRate.builder().code("EUR").rate(new BigDecimal("0.91209605692262")).inverseRate(new BigDecimal("1.0963757516659")).build());
        when(currencyExchangeRateService.getByCode("CAD")).thenReturn(CurrencyExchangeRate.builder().code("CAD").rate(new BigDecimal("1.366142899496")).inverseRate(new BigDecimal("0.73198784722222")).build());

        BigDecimal result = calculationService.calculateCurrency(fromUnit, toUnit, new BigDecimal("20"));

        verify(currencyExchangeRateService).getByCode("EUR");
        verify(currencyExchangeRateService).getByCode("CAD");

        assertThat(result).isEqualTo(new BigDecimal("29.95611896635922050120"));
    }

    @Test
    void calculateCurrency_parentNull() {
        Unit fromUnit = Unit.builder()
                .type(UnitType.CURRENCY)
                .build();
        Unit toUnit = Unit.builder()
                .type(UnitType.CURRENCY)
                .build();

        when(currencyExchangeRateService.getByCode("USD")).thenReturn(CurrencyExchangeRate.builder().code("USD").rate(new BigDecimal("1")).inverseRate(new BigDecimal("1")).build());

        BigDecimal result = calculationService.calculateCurrency(fromUnit, toUnit, new BigDecimal("20"));

        verify(currencyExchangeRateService, times(2)).getByCode("USD");

        assertThat(result).isEqualTo(new BigDecimal("20.00000000000000000000"));
    }
}
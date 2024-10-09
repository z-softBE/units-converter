package be.zsoft.units.converter.service;

import be.zsoft.units.converter.dto.Response.CurrencyExchangeRateResponse;
import be.zsoft.units.converter.dto.mapper.CurrencyExchangeRateMapper;
import be.zsoft.units.converter.model.CurrencyExchangeRate;
import be.zsoft.units.converter.repo.CurrencyExchangeRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeRateServiceTest {

    @Mock
    private CurrencyExchangeRepo currencyExchangeRepo;

    @Mock
    private CurrencyExchangeRateMapper currencyExchangeRateMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyExchangeRateService currencyExchangeRateService;

    @Test
    void updateCurrencyExchangeRate() {
        CurrencyExchangeRateResponse rateResponse = new CurrencyExchangeRateResponse("EUR", "EUR", "978", "Euro", new BigDecimal("0.91241433905523"), new BigDecimal("1.0959932973384"));
        CurrencyExchangeRate rate = CurrencyExchangeRate.builder().code("EUR").build();
        CurrencyExchangeRate usdRate = CurrencyExchangeRate.builder().code("USD").rate(new BigDecimal("1")).inverseRate(new BigDecimal("1")).build();
        ResponseEntity<Map<String, CurrencyExchangeRateResponse>> responseEntity = ResponseEntity.ok(Map.of("EUR", rateResponse));

        when(restTemplate.exchange(eq("https://www.floatrates.com/daily/usd.json"), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);
        when(currencyExchangeRateMapper.fromResponse(rateResponse)).thenReturn(rate);

        currencyExchangeRateService.updateCurrencyExchangeRate();

        verify(restTemplate).exchange(eq("https://www.floatrates.com/daily/usd.json"), eq(HttpMethod.GET), eq(null), any(ParameterizedTypeReference.class));
        verify(currencyExchangeRateMapper).fromResponse(rateResponse);
        verify(currencyExchangeRepo).saveAll(List.of(rate));
        verify(currencyExchangeRepo).save(usdRate);
        verify(currencyExchangeRepo).flush();
    }

    @Test
    void getByCode_withResult() {
        CurrencyExchangeRate expected = CurrencyExchangeRate.builder().code("EUR").build();
        when(currencyExchangeRepo.findByCode("EUR")).thenReturn(Optional.of(expected));

        CurrencyExchangeRate result = currencyExchangeRateService.getByCode("EUR");

        assertThat(result).isEqualTo(expected);

        verify(currencyExchangeRepo).findByCode("EUR");
    }

    @Test
    void getByCode_notFound() {
        when(currencyExchangeRepo.findByCode("EUR")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> currencyExchangeRateService.getByCode("EUR")).isInstanceOf(RuntimeException.class);
    }
}
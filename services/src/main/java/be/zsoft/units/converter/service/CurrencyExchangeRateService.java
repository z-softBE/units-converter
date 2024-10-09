package be.zsoft.units.converter.service;

import be.zsoft.units.converter.dto.Response.CurrencyExchangeRateResponse;
import be.zsoft.units.converter.dto.mapper.CurrencyExchangeRateMapper;
import be.zsoft.units.converter.model.CurrencyExchangeRate;
import be.zsoft.units.converter.repo.CurrencyExchangeRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor

@Service
public class CurrencyExchangeRateService {

    private static final String CURRENCY_EXCHANGE_RATE_JSON_URL = "https://www.floatrates.com/daily/usd.json";

    private final CurrencyExchangeRepo currencyExchangeRepo;
    private final CurrencyExchangeRateMapper currencyExchangeRateMapper;
    private final RestTemplate restTemplate;

    @Transactional
    public void updateCurrencyExchangeRate() {
        log.info("Updating currency exchange rate from: {}", CURRENCY_EXCHANGE_RATE_JSON_URL);

        List<CurrencyExchangeRate> rates = Objects.requireNonNull(restTemplate.exchange(
                                CURRENCY_EXCHANGE_RATE_JSON_URL,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<Map<String, CurrencyExchangeRateResponse>>() {
                                })
                        .getBody())
                .values()
                .stream()
                .map(currencyExchangeRateMapper::fromResponse)
                .toList();

        currencyExchangeRepo.saveAll(rates);
        currencyExchangeRepo.save(CurrencyExchangeRate.builder().code("USD").rate(new BigDecimal("1")).inverseRate(new BigDecimal("1")).build());
        currencyExchangeRepo.flush();
    }

    public CurrencyExchangeRate getByCode(String code) {
        return currencyExchangeRepo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Currency exchange rate not found with code: " + code));
    }
}

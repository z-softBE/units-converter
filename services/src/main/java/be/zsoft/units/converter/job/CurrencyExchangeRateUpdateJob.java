package be.zsoft.units.converter.job;

import be.zsoft.units.converter.service.CurrencyExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor

@Component
public class CurrencyExchangeRateUpdateJob {

    private final CurrencyExchangeRateService currencyExchangeRateService;

    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.MINUTES, initialDelay = 1)
    public void updateCurrencyExchangeRate() {
        currencyExchangeRateService.updateCurrencyExchangeRate();
    }
}


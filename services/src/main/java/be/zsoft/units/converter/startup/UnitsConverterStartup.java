package be.zsoft.units.converter.startup;

import be.zsoft.units.converter.service.CurrencyExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor

@Component
public class UnitsConverterStartup implements CommandLineRunner {

    private final CurrencyExchangeRateService currencyExchangeRateService;

    @Override
    public void run(String... args) {
        currencyExchangeRateService.updateCurrencyExchangeRate();
    }
}

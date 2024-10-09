package be.zsoft.units.converter.startup;

import be.zsoft.units.converter.service.CurrencyExchangeRateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UnitsConverterStartupTest {

    @Mock
    private CurrencyExchangeRateService currencyExchangeRateService;

    @InjectMocks
    private UnitsConverterStartup unitsConverterStartup;

    @Test
    void run() {
        unitsConverterStartup.run();

        verify(currencyExchangeRateService).updateCurrencyExchangeRate();
    }
}
package be.zsoft.units.converter.job;

import be.zsoft.units.converter.service.CurrencyExchangeRateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeRateUpdateJobTest {

    @Mock
    private CurrencyExchangeRateService currencyExchangeRateService;

    @InjectMocks
    private CurrencyExchangeRateUpdateJob currencyExchangeRateUpdateJob;

    @Test
    void updateCurrencyExchangeRate() {
        currencyExchangeRateUpdateJob.updateCurrencyExchangeRate();

        verify(currencyExchangeRateService).updateCurrencyExchangeRate();
    }
}
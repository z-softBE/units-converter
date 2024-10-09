package be.zsoft.units.converter.dto.mapper;

import be.zsoft.units.converter.dto.Response.CurrencyExchangeRateResponse;
import be.zsoft.units.converter.model.CurrencyExchangeRate;
import org.springframework.stereotype.Component;

@Component
public class CurrencyExchangeRateMapper {

    public CurrencyExchangeRate fromResponse(CurrencyExchangeRateResponse response) {
        return CurrencyExchangeRate.builder()
                .code(response.code())
                .rate(response.rate())
                .inverseRate(response.inverseRate())
                .build();
    }
}

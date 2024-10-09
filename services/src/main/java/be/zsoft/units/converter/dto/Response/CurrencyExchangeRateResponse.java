package be.zsoft.units.converter.dto.Response;

import java.math.BigDecimal;

public record CurrencyExchangeRateResponse(
        String code,
        String alphaCode,
        String numericCode,
        String name,
        BigDecimal rate,
        BigDecimal inverseRate
) {
}

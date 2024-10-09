package be.zsoft.units.converter.service;

import be.zsoft.units.converter.model.CurrencyExchangeRate;
import be.zsoft.units.converter.model.Unit;
import be.zsoft.units.converter.model.UnitType;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Slf4j

@Service
public class CalculationService {

    private final CurrencyExchangeRateService currencyExchangeRateService;

    public BigDecimal calculateUnit(Unit fromUnit, Unit toUnit, BigDecimal value) {
        BigDecimal fromRatioValue = calculateValueBaseOnParent(fromUnit);
        BigDecimal toRatioValue = calculateValueBaseOnParent(toUnit);

        return fromRatioValue.divide(toRatioValue, 20, RoundingMode.HALF_UP).multiply(value);
    }

    public BigDecimal calculateCurrency(Unit fromUnit, Unit toUnit, BigDecimal value) {
        if (fromUnit.getType() != UnitType.CURRENCY) {
            throw new IllegalArgumentException("The fromUnit is not a currency");
        }

        if (toUnit.getType() != UnitType.CURRENCY) {
            throw new IllegalArgumentException("The toUnit is not a currency");
        }

        CurrencyExchangeRate fromRate = currencyExchangeRateService.getByCode(StringUtils.hasText(fromUnit.getRatioToParent()) ? fromUnit.getRatioToParent() : "USD");
        CurrencyExchangeRate toRate = currencyExchangeRateService.getByCode(StringUtils.hasText(toUnit.getRatioToParent()) ? toUnit.getRatioToParent() : "USD");

        return fromRate.getInverseRate().divide(toRate.getInverseRate(), 20, RoundingMode.HALF_UP).multiply(value);
    }

    private BigDecimal calculateValueBaseOnParent(Unit unit) {
        if (unit.getRatioType() == null) return new BigDecimal("1");

        return switch (unit.getRatioType()) {
            case BIG_DECIMAL -> new BigDecimal(unit.getRatioToParent());
            case EXPRESSION -> executeExpression(unit.getRatioToParent(), new BigDecimal("1"));
            default -> new BigDecimal("1");
        };
    }

    private BigDecimal executeExpression(String expression, BigDecimal value) {
        String filledInExpression = expression.replace("${value}", value.toPlainString()).toLowerCase();
        DoubleEvaluator evaluator = new DoubleEvaluator();

        return BigDecimal.valueOf(evaluator.evaluate(filledInExpression));
    }

}


package be.zsoft.units.converter.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter
public class BigDecimalToStringConverter implements AttributeConverter<BigDecimal, String> {
    @Override
    public String convertToDatabaseColumn(BigDecimal attribute) {
        return attribute != null ? attribute.toPlainString() : null;
    }

    @Override
    public BigDecimal convertToEntityAttribute(String dbData) {
        return dbData != null ? new BigDecimal(dbData) : null;
    }
}

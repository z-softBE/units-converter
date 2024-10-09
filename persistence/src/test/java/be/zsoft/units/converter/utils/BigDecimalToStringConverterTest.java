package be.zsoft.units.converter.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BigDecimalToStringConverterTest {

    @InjectMocks
    private BigDecimalToStringConverter converter;

    @Test
    void convertToDatabaseColumn_null() {
        String result = converter.convertToDatabaseColumn(null);

        assertThat(result).isNull();
    }

    @Test
    void convertToDatabaseColumn_asPlainString() {
        String result = converter.convertToDatabaseColumn(new BigDecimal("1.2345"));

        assertThat(result).isEqualTo("1.2345");
    }

    @Test
    void convertToEntityAttribute_null() {
        BigDecimal result = converter.convertToEntityAttribute(null);

        assertThat(result).isNull();
    }

    @Test
    void convertToEntityAttribute_asBigDecimal() {
        BigDecimal result = converter.convertToEntityAttribute("1.2345");

        assertThat(result).isEqualTo(new BigDecimal("1.2345"));
    }
}
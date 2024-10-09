package be.zsoft.units.converter.model;

import be.zsoft.units.converter.utils.BigDecimalToStringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Change rate based on the USD
 */

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

@Entity
@Table(name = "currency_exchange_rates")
public class CurrencyExchangeRate {

    @Id
    @Column(name = "id", length = 3, nullable = false)
    private String code;

    @Column(name = "rate", columnDefinition = "VARCHAR")
    @Convert(converter = BigDecimalToStringConverter.class)
    private BigDecimal rate;

    @Column(name = "inverse_rate", columnDefinition = "VARCHAR")
    @Convert(converter = BigDecimalToStringConverter.class)
    private BigDecimal inverseRate;
}

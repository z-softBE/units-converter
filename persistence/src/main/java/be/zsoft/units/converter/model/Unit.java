package be.zsoft.units.converter.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.util.StringUtils;

import java.sql.Types;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "units")
public class Unit {

    @Id
    @GeneratedValue(generator = "uuid2")
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private String id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "short_hand", nullable = true, length = 255)
    private String shortHand;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 255)
    private UnitType type;

    @Column(name = "ratio_to_parent", nullable = false, length = 255)
    private String ratioToParent;

    @Enumerated(EnumType.STRING)
    @Column(name = "ratio_type", nullable = true, length = 255)
    private RatioType ratioType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    private Unit parent;

    public String toString() {
        String result =  name;

        if (StringUtils.hasText(shortHand)) {
            result +=  " (" + shortHand + ")";
        }

        return result;
    }

}

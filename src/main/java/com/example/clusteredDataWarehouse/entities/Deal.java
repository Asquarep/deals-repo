package com.example.clusteredDataWarehouse.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "DEALS")
public class Deal  extends BaseEntity {

    @Column(name = "UNIQUE_ID", nullable = false)
    private String uniqueId;

    @Column(name = "FROM_CURRENCY", nullable = false)
    private String fromCurrency;

    @Column(name = "TO_CURRENCY", nullable = false)
    private String toCurrency;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal dealAmount;

}

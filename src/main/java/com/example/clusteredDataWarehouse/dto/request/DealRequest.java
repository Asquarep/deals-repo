package com.example.clusteredDataWarehouse.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DealRequest {

    @NotBlank
    @Size(max = 63, message = "Should not be more than 63 characters")
    private String uniqueId;

    @NotBlank
    @Size(max = 3, message = "Should not be more than 3 characters")
    private String fromCurrency;

    @NotBlank
    @Size(max = 3, message = "Should not be more than 3 characters")
    private String toCurrency;

    @NotNull
    private BigDecimal amount;

}

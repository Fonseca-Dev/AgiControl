package com.example.Sistema_Gastos_Review.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AlterarCarteiraRequest(
        @NotBlank String nome,
        @NotNull BigDecimal meta
) {
}

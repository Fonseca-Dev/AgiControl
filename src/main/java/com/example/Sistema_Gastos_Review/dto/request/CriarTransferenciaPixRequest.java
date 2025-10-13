package com.example.Sistema_Gastos_Review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CriarTransferenciaPixRequest(
        @NotBlank
        String dataHoraLocal,
        @NotBlank
        String chavePixDestino,
        @NotNull
        BigDecimal valor,
        @NotBlank
        String categoria
) {
}

package com.example.Sistema_Gastos_Review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CriarPagamentoBoletoRequest(
        @NotBlank
        String dataHoraLocal,
        @NotBlank
        String codigoBarras,
        @NotNull
        LocalDateTime dataVencimento,
        @NotBlank
        String nomeBeneficiario,
        @NotBlank
        String instituicaoFinanceira,
        @NotNull
        BigDecimal valor,
        @NotBlank
        String categoria
) {
}

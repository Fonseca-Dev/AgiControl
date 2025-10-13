package com.example.Sistema_Gastos_Review.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoPagamentoBoletoResponse(
        String id,
        String tipo,
        LocalDateTime data,
        BigDecimal valor,
        String codigoBarras,
        LocalDateTime dataVencimento,
        String nomeBeneficiario,
        String instituicaoFinanceira,
        String categoria,
        Long numConta
) {
}

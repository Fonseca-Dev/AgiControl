package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CriarPagamentoBoletoResponse(
        String id,
        BigDecimal valor,
        LocalDateTime data,
        String codigoBarras,
        LocalDateTime dataVencimento,
        String nomeBeneficiario,
        String instituicaoFinanceira,
        String categoria

) {
}

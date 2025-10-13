package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;

public record PagamentoBoletoResponse(
        String idTransacao,
        BigDecimal valor,
        String idConta,
        String codigoBarras,
        String nomeBeneficiario,
        String instituicaoFinanceira,
        String categoria
) {
}

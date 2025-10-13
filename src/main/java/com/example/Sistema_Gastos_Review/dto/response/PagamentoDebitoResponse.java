package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;

public record PagamentoDebitoResponse(
        String idTransacao,
        BigDecimal valor,
        String idConta,
        String nomeEstabelecimento,
        String categoria
) {
}

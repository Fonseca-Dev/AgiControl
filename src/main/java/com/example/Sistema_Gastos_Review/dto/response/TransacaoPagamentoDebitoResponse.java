package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoPagamentoDebitoResponse(
        String id,
        String tipo,
        LocalDateTime data,
        BigDecimal valor,
        String nomeEstabelecimento,
        String categoria,
        Long numConta
) {
}

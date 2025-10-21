package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferenciaResponse(
        LocalDateTime data,
        String contaOrigemId,
        Long numeroContaOrigem,
        BigDecimal valor,
        String contaDestinoId,
        Long numeroContaDestino,
        String categoria
) {
}

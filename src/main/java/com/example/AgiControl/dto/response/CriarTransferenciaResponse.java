package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CriarTransferenciaResponse(
        String id,
        BigDecimal valor,
        LocalDateTime data,
        String idContaOrigem,
        String contaDestino,
        String categoria
) {
}

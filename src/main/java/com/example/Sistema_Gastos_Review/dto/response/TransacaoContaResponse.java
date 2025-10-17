package com.example.Sistema_Gastos_Review.dto.response;

import java.time.LocalDateTime;

public record TransacaoContaResponse(
        String id,
        String tipo,
        LocalDateTime data,
        Object dados
) {
}

package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GastosReponse(
        LocalDateTime data,
        String tipo,
        BigDecimal valor,
        String categoria
) {
}

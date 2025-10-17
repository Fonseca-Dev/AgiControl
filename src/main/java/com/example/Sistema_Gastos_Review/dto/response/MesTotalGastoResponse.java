package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;

public record MesTotalGastoResponse(
        String mes,
        BigDecimal totalGasto
) {
}

package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;

public record CategoriaResponse(
        String categoria,
        BigDecimal valor
) {
}

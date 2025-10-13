package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;

public record BuscarCarteiraResponse(
        String id,
        String nome,
        BigDecimal saldo,
        String estado,
        BigDecimal meta
) {
}

package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;

public record DeletarContaResponse(
        Long numero,
        String tipo,
        BigDecimal saldo,
        String estado
) {
}

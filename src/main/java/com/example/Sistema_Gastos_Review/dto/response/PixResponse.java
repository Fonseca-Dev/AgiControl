package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;

public record PixResponse(
        String idTransacao,
        BigDecimal valor,
        String idContaOrigem,
        String chavePixDestino,
        String categoria

) {
}

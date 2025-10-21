package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoCarteiraResponse(
        String idConta,
        Long numeroConta,
        String idcarteira,
        String tipo,
        LocalDateTime data,
        BigDecimal valor
) {
}

package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ContaCarteiraResponse(
        LocalDateTime data,
        String contaId,
        Long numeroConta,
        BigDecimal valor,
        String carteiraId
) {
}

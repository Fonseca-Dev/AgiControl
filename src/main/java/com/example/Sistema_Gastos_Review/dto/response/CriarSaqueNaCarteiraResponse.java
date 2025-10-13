package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CriarSaqueNaCarteiraResponse(
        String id,
        BigDecimal valor,
        LocalDateTime data,
        String idCarteira,
        String idConta
) {
}

package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoDeletarCarteiraResponse(
        String id,
        String tipo,
        LocalDateTime data,
        BigDecimal valor,
        Long numConta,
        String idCarteira,
        String nomeCarteira
) {
}

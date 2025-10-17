package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoPixResponse(
        String id,
        String tipo,
        LocalDateTime data,
        BigDecimal valor,
        String nomeRemetente,
        String chavePixDestino,
        String nomeDestinatario,
        String categoria
) {
}

package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CriarTransferenciaPixResponse(
        String idTransacao,
        String tipo,
        LocalDateTime data,
        BigDecimal valor,
        String chavePixDestino,
        String nomeDestinatario,
        String categoria
) {
}

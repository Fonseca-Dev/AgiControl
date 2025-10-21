package com.example.Sistema_Gastos_Review.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DiaDetalhadoResponse(
        LocalDate data,
        BigDecimal valorGasto,
        List<GastosReponse> transacoes
) {
}

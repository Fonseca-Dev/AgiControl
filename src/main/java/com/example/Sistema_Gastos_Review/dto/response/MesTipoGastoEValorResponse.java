package com.example.Sistema_Gastos_Review.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record MesTipoGastoEValorResponse(
        LocalDateTime mes,
        List<TipoGastoEValorResponse> gastosValores
) {
}

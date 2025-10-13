package com.example.Sistema_Gastos_Review.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DeletarCarteiraRequest(
        @NotBlank
        String dataHoraLocal
) {
}

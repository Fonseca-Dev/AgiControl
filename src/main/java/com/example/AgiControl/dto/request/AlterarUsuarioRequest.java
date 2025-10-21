package com.example.Sistema_Gastos_Review.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AlterarUsuarioRequest(
        @NotBlank String nome,
        @NotBlank String email,
        @NotBlank String senha) {
}

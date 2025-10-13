package com.example.Sistema_Gastos_Review.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CriarUsuarioRequest(
        @NotBlank String nome,
        @NotBlank String email,
        @NotBlank String senha
) {}

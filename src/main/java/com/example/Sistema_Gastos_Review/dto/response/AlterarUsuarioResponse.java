package com.example.Sistema_Gastos_Review.dto.response;

public record AlterarUsuarioResponse(
        String nome,
        String email,
        String senha,
        String estado
) {
}

package com.example.Sistema_Gastos_Review.dto.response;

public record DeletarUsuarioResponse(
        String id,
        String nome,
        String email,
        String estado
) {
}

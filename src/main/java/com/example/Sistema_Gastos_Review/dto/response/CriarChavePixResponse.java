package com.example.Sistema_Gastos_Review.dto.response;

public record CriarChavePixResponse(
        String idConta,
        Long numeroConta,
        String chavePix
) {
}

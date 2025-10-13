package com.example.Sistema_Gastos_Review.dto.response;

import org.springframework.http.HttpStatus;

public record BaseResponse(
        String mensagem,
        HttpStatus status,
        Object objeto
) {
}

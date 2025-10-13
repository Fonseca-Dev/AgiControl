package com.example.Sistema_Gastos_Review.dto.response;

import com.example.Sistema_Gastos_Review.entity.Conta;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoTEDResponse(
        String id,
        String tipo,
        LocalDateTime data,
        BigDecimal valor,
        Long numContaOrigem,
        Long numContaDestino,
        String categoria
) {
}

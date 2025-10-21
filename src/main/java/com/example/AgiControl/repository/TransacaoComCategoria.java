package com.example.Sistema_Gastos_Review.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransacaoComCategoria {
    String getCategoria();
    BigDecimal getValor();
    LocalDateTime getData();
}

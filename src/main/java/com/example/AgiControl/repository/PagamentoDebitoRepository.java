package com.example.Sistema_Gastos_Review.repository;

import com.example.Sistema_Gastos_Review.entity.Deposito;
import com.example.Sistema_Gastos_Review.entity.PagamentoDebito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PagamentoDebitoRepository extends JpaRepository<PagamentoDebito, String> {
    List<PagamentoDebito> findByContaId(String contaId);
}

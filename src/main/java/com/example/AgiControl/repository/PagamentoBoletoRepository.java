package com.example.Sistema_Gastos_Review.repository;

import com.example.Sistema_Gastos_Review.entity.Deposito;
import com.example.Sistema_Gastos_Review.entity.PagamentoBoleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PagamentoBoletoRepository extends JpaRepository<PagamentoBoleto, String> {
    List<PagamentoBoleto> findByContaId(String contaId);
}

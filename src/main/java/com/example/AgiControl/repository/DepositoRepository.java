package com.example.Sistema_Gastos_Review.repository;

import com.example.Sistema_Gastos_Review.entity.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DepositoRepository extends JpaRepository<Deposito, String> {
    List<Deposito> findByContaId(String contaId);
}

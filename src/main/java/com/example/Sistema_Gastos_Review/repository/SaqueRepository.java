package com.example.Sistema_Gastos_Review.repository;

import com.example.Sistema_Gastos_Review.entity.Deposito;
import com.example.Sistema_Gastos_Review.entity.Saque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaqueRepository extends JpaRepository<Saque,String> {
    List<Saque> findByContaId(String contaId);
}

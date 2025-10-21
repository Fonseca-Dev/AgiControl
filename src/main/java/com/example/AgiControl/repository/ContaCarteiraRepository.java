package com.example.Sistema_Gastos_Review.repository;

import com.example.Sistema_Gastos_Review.entity.Conta_Carteira;
import com.example.Sistema_Gastos_Review.entity.Deposito;
import com.example.Sistema_Gastos_Review.entity.Saque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContaCarteiraRepository extends JpaRepository<Conta_Carteira, String> {
    List<Conta_Carteira> findByContaId(String contaId);
    List<Conta_Carteira> findByCarteiraId(String carteiraId);
}

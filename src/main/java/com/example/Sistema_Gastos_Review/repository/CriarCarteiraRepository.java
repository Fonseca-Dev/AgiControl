package com.example.Sistema_Gastos_Review.repository;

import com.example.Sistema_Gastos_Review.entity.CriarCarteira;
import com.example.Sistema_Gastos_Review.entity.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CriarCarteiraRepository extends JpaRepository<CriarCarteira, String> {
    List<CriarCarteira> findByContaId(String contaId);
}

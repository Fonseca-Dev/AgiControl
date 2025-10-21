package com.example.Sistema_Gastos_Review.repository;

import com.example.Sistema_Gastos_Review.entity.DeletarCarteira;
import com.example.Sistema_Gastos_Review.entity.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DeletarCarteiraRepository extends JpaRepository<DeletarCarteira, String> {
    List<DeletarCarteira> findByContaId(String contaId);
}

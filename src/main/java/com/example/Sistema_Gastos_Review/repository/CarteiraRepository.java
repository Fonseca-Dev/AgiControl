package com.example.Sistema_Gastos_Review.repository;

import com.example.Sistema_Gastos_Review.entity.Carteira;
import com.example.Sistema_Gastos_Review.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarteiraRepository extends JpaRepository<Carteira,String> {
    Optional<Carteira> findByContaIdAndNome(String contaId, String nome);
    List<Carteira> findByContaId(String contaId);
}

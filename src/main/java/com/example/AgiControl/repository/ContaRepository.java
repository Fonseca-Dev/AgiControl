package com.example.Sistema_Gastos_Review.repository;

import com.example.Sistema_Gastos_Review.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ContaRepository extends JpaRepository<Conta, String> {
    Optional<Conta> findTopByOrderByNumeroDesc();
    Optional<Conta> findByUsuarioIdAndTipo(String usuarioId, String tipo);

    List<Conta> findByUsuarioId(String usuarioId);

    Optional<Conta> findByChavePix (String chavePix);

    Optional<Conta> findByNumero(Long numeroConta);
}

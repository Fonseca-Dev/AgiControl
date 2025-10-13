package com.example.Sistema_Gastos_Review.repository;

import com.example.Sistema_Gastos_Review.entity.Deposito;
import com.example.Sistema_Gastos_Review.entity.Pix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PixRepository extends JpaRepository<Pix, String> {
    List<Pix> findByContaId(String contaId);
    List<Pix> findByChavePixDestino (String chavePixDestino);
}

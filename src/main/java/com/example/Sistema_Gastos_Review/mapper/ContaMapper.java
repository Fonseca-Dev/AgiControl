package com.example.Sistema_Gastos_Review.mapper;

import com.example.Sistema_Gastos_Review.dto.response.BuscarContasReponse;
import com.example.Sistema_Gastos_Review.dto.response.CriarChavePixResponse;
import com.example.Sistema_Gastos_Review.dto.response.CriarContaResponse;
import com.example.Sistema_Gastos_Review.dto.response.DeletarContaResponse;
import com.example.Sistema_Gastos_Review.entity.Conta;
import com.example.Sistema_Gastos_Review.entity.Usuario;

import java.math.BigDecimal;
import java.util.List;

public class ContaMapper {
    public static Conta criarContaCorrente(Usuario usuario, Long proximoNumero) {
        return Conta.builder()
                .usuario(usuario)
                .numero(proximoNumero)
                .tipo("CORRENTE")
                .saldo(BigDecimal.ZERO)
                .estado("ATIVA")
                .chavePix(usuario.getEmail())
                .build();
    }

    public static Conta toEntity(String tipo, Usuario usuario, Long proximoNumero) {
        return Conta.builder()
                .usuario(usuario)
                .numero(proximoNumero)
                .tipo(tipo)
                .saldo(BigDecimal.ZERO)
                .estado("ATIVA")
                .build();
    }

    public static CriarContaResponse toCriarContaResponse(Conta conta) {
        return new CriarContaResponse(
                conta.getNumero(),
                conta.getTipo(),
                conta.getSaldo(),
                conta.getEstado()
        );
    }

    public static DeletarContaResponse toDeletarContaResponse(Conta conta) {
        return new DeletarContaResponse(
                conta.getNumero(),
                conta.getTipo(),
                conta.getSaldo(),
                conta.getEstado()
        );
    }

    public static List<BuscarContasReponse> toBuscarContaResponse(List<Conta> contas) {
        return contas
                .stream()
                .map(conta -> new BuscarContasReponse(
                        conta.getId(),
                        conta.getNumero(),
                        conta.getSaldo(),
                        conta.getTipo(),
                        conta.getEstado()
                )).toList();
    }

    public static CriarChavePixResponse toCriarChavePixResponse (Conta conta){
        return new CriarChavePixResponse(
                conta.getId(),
                conta.getNumero(),
                conta.getChavePix()
        );
    }
}

package com.example.Sistema_Gastos_Review.mapper;

import com.example.Sistema_Gastos_Review.dto.request.AlterarCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.response.*;
import com.example.Sistema_Gastos_Review.entity.Carteira;
import com.example.Sistema_Gastos_Review.entity.Conta;

import java.math.BigDecimal;
import java.util.List;

public class CarteiraMapper {
    public static Carteira toEntity(Conta conta, CriarCarteiraRequest request) {
        return Carteira.builder()
                .nome(request.nome().toUpperCase())
                .saldo(request.saldo())
                .estado("ATIVA")
                .conta(conta)
                .meta(request.meta())
                .build();
    }

    public static CriarCarteiraResponse toCriarCarteiraResponse(Carteira carteira) {
        return new CriarCarteiraResponse(
                carteira.getId(),
                carteira.getNome(),
                carteira.getSaldo()
        );
    }

    public static AlterarCarteiraResponse toAlterarCarteiraResponse(Carteira carteira) {
        return new AlterarCarteiraResponse(
                carteira.getId(),
                carteira.getNome(),
                carteira.getSaldo()
        );
    }

    public static void atualizarCarteira(Carteira carteira, AlterarCarteiraRequest request) {
        carteira.setNome(request.nome());
        carteira.setMeta(request.meta());
    }

    public static void deletarCarteira(Conta conta, Carteira carteira) {
        carteira.setEstado("DELETADA");
        BigDecimal saldo = carteira.getSaldo();
        carteira.setSaldo(carteira.getSaldo().subtract(saldo));
        conta.setSaldo(conta.getSaldo().add(saldo));
    }

    public static DeletarCarteiraResponse toDeletarCarteiraResponse(Carteira carteira) {
        return new DeletarCarteiraResponse(
                carteira.getNome(),
                carteira.getEstado()
        );
    }

    public static List<BuscarCarteirasResponse> toBuscarCarteirasResponse(List<Carteira> carteiras) {
        return carteiras
                .stream()
                .map(carteira -> new BuscarCarteirasResponse(
                        carteira.getId(),
                        carteira.getNome(),
                        carteira.getSaldo(),
                        carteira.getEstado(),
                        carteira.getMeta()
                )).toList();
    }

    public static BuscarCarteiraResponse toBuscarCarteiraResponse(Carteira carteira) {
        return new BuscarCarteiraResponse(
                carteira.getId(),
                carteira.getNome(),
                carteira.getSaldo(),
                carteira.getEstado(),
                carteira.getMeta()
        );
    }
}

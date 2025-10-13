package com.example.Sistema_Gastos_Review.mapper;

import com.example.Sistema_Gastos_Review.dto.request.*;
import com.example.Sistema_Gastos_Review.dto.response.*;
import com.example.Sistema_Gastos_Review.entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TransacaoMapper {
    public static Saque toSaqueContaEntity(CriarSaqueContaRequest request, Conta conta) {
        return Saque.builder()
                .conta(conta)
                .tipo("SAQUE_CONTA")
                .valor(request.valor())
                .data(LocalDateTime.parse(request.dataHoraLocal()))
                .build();
    }

    public static void sacar(Conta conta, CriarSaqueContaRequest request) {
        conta.setSaldo(conta.getSaldo().subtract(request.valor()));
    }

    public static CriarSaqueResponse toCriarSaqueResponse(Conta conta, Saque saque) {
        return new CriarSaqueResponse(
                saque.getId(),
                saque.getValor(),
                saque.getData(),
                conta.getId()
        );
    }

    public static Deposito toDepositoContaEntity(CriarDepositoContaRequest request, Conta conta) {
        return Deposito.builder()
                .conta(conta)
                .tipo("DEPOSITO_CONTA")
                .valor(request.valor())
                .data(LocalDateTime.parse(request.dataHoraLocal()))
                .build();
    }

    public static void depositar(Conta conta, CriarDepositoContaRequest request) {
        conta.setSaldo(conta.getSaldo().add(request.valor()));
    }

    public static CriarDepositoResponse toCriarDepositoResponse(Conta conta, Deposito deposito) {
        return new CriarDepositoResponse(
                deposito.getId(),
                deposito.getValor(),
                deposito.getData(),
                conta.getId()
        );
    }

    public static Conta_Carteira toContaCarteiraDepositoEntity(Conta conta, Carteira carteira, CriarDepositoNaCarteiraRequest request) {
        return Conta_Carteira.builder()
                .conta(conta)
                .carteira(carteira)
                .tipo("DEPOSITO_CARTEIRA")
                .valor(request.valor())
                .data(LocalDateTime.parse(request.dataHoraLocal()))
                .build();
    }

    public static Conta_Carteira toContaCarteiraSaqueEntity(Conta conta, Carteira carteira, CriarSaqueNaCarteiraRequest request) {
        return Conta_Carteira.builder()
                .conta(conta)
                .carteira(carteira)
                .tipo("SAQUE_CARTEIRA")
                .valor(request.valor())
                .data(LocalDateTime.parse(request.dataHoraLocal()))
                .build();
    }

    public static void depositarNaCarteira(Conta_Carteira contaCarteira) {
        contaCarteira.getConta().setSaldo(contaCarteira.getConta().getSaldo().subtract(contaCarteira.getValor()));
        contaCarteira.getCarteira().setSaldo(contaCarteira.getCarteira().getSaldo().add(contaCarteira.getValor()));
    }

    public static CriarDepositoNaCarteiraResponse toCriarDepositoNaCarteiraResponse(Conta_Carteira contaCarteira) {
        return new CriarDepositoNaCarteiraResponse(
                contaCarteira.getId(),
                contaCarteira.getValor(),
                contaCarteira.getData(),
                contaCarteira.getCarteira().getId(),
                contaCarteira.getConta().getId()
        );
    }

    public static void sacarDaCarteira(Conta_Carteira contaCarteira) {
        contaCarteira.getConta().setSaldo(contaCarteira.getConta().getSaldo().add(contaCarteira.getValor()));
        contaCarteira.getCarteira().setSaldo(contaCarteira.getCarteira().getSaldo().subtract(contaCarteira.getValor()));
    }

    public static CriarSaqueNaCarteiraResponse toCriarSaqueNaCarteiraResponse(Conta_Carteira contaCarteira) {
        return new CriarSaqueNaCarteiraResponse(
                contaCarteira.getId(),
                contaCarteira.getValor(),
                contaCarteira.getData(),
                contaCarteira.getCarteira().getId(),
                contaCarteira.getConta().getId()
        );
    }

    public static Transferencia toTransferenciaEntityInterna(Conta contaOrigem, Conta contaDestino, CriarTransferenciaRequest request) {
        return Transferencia.builder()
                .contaOrigem(contaOrigem)
                .contaDestino(contaDestino)
                .tipo("TRANSFERENCIA_INTERNA")
                .valor(request.valor())
                .categoria(request.categoria())
                .data(LocalDateTime.parse(request.dataHoraLocal()))
                .build();
    }

    public static Transferencia toTransferenciaEntityExterna(Conta contaOrigem, Long numeroContaDestinoExterna, CriarTransferenciaRequest request) {
        return Transferencia.builder()
                .contaOrigem(contaOrigem)
                .numeroContaDestino(numeroContaDestinoExterna)
                .tipo("TRANSFERENCIA_EXTERNA")
                .valor(request.valor())
                .categoria(request.categoria())
                .data(LocalDateTime.parse(request.dataHoraLocal()))
                .build();
    }

    public static void aplicarTransferenciaInterna(Conta contaOrigem, Conta contaDestino, CriarTransferenciaRequest request) {
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(request.valor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(request.valor()));
    }

    public static void aplicarTransferenciaExterna(Conta contaOrigem, CriarTransferenciaRequest request) {
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(request.valor()));
    }

    public static CriarTransferenciaResponse toCriarTransferenciaResponse(Transferencia pagamento) {
        return new CriarTransferenciaResponse(
                pagamento.getId(),
                pagamento.getValor(),
                pagamento.getData(),
                pagamento.getContaOrigem().getId(),
                pagamento.getContaDestino() != null ? pagamento.getContaDestino().getId() : pagamento.getNumeroContaDestino().toString(),
                pagamento.getCategoria()
        );
    }

    public static List<TransacaoContaResponse> listarTransacoesContaResponse(
            List<Deposito> depositos,
            List<Saque> saques,
            List<Conta_Carteira> contaCarteiraList,
            List<Transferencia> transferenciasEnviadas,
            List<Transferencia> transferenciasRecebidas,
            List<CriarCarteira> criarCarteiraList,
            List<DeletarCarteira> deletarCarteiraList,
            List<PagamentoBoleto> pagamentoBoletoList,
            List<PagamentoDebito> pagamentoDebitoList,
            List<Pix> pixEnviado,
            List<Pix> pixRecebido
    ) {
        List<TransacaoContaResponse> lista = new ArrayList<>();

        lista.addAll(
                depositos.stream()
                        .map(d -> new TransacaoContaResponse(
                                d.getTipo(),
                                d.getData(),
                                new DepositoResponse(d.getData(), d.getConta().getId(), d.getConta().getNumero(), d.getValor())
                        ))
                        .toList()
        );

        lista.addAll(
                saques.stream()
                        .map(s -> new TransacaoContaResponse(
                                s.getTipo(),
                                s.getData(),
                                new SaqueResponse(s.getData(), s.getConta().getId(), s.getConta().getNumero(), s.getValor().negate())
                        ))
                        .toList()
        );

        lista.addAll(
                contaCarteiraList.stream()
                        .map(cc -> new TransacaoContaResponse(
                                cc.getTipo(),
                                cc.getData(),
                                new ContaCarteiraResponse(cc.getData(), cc.getConta().getId(), cc.getConta().getNumero(), cc.getValor(), cc.getCarteira().getId())
                        ))
                        .toList()
        );

        lista.addAll(
                transferenciasEnviadas.stream()
                        .map(p -> new TransacaoContaResponse(
                                p.getTipo(),
                                p.getData(),
                                new TransferenciaResponse(
                                        p.getData(),
                                        p.getContaOrigem().getId(),
                                        p.getContaOrigem().getNumero(),
                                        p.getValor(),
                                        p.getContaDestino() != null ? p.getContaDestino().getId() : null,
                                        p.getNumeroContaDestino(),
                                        p.getCategoria()
                                )
                        ))
                        .toList()
        );

        lista.addAll(
                criarCarteiraList.stream()
                        .map(criarCarteira -> new TransacaoContaResponse(
                                criarCarteira.getTipo(),
                                criarCarteira.getData(),
                                new CarteiraResponse(
                                        criarCarteira.getId(),
                                        criarCarteira.getValor(),
                                        criarCarteira.getConta().getId()
                                )
                        )).toList()
        );

        lista.addAll(
                deletarCarteiraList.stream()
                        .map(deletarCarteira -> new TransacaoContaResponse(
                                deletarCarteira.getTipo(),
                                deletarCarteira.getData(),
                                new CarteiraResponse(
                                        deletarCarteira.getId(),
                                        deletarCarteira.getValor(),
                                        deletarCarteira.getConta().getId()
                                )
                        )).toList()
        );

        lista.addAll(
                pagamentoBoletoList.stream()
                        .map(pagamentoBoleto -> new TransacaoContaResponse(
                                pagamentoBoleto.getTipo(),
                                pagamentoBoleto.getData(),
                                new PagamentoBoletoResponse(
                                        pagamentoBoleto.getId(),
                                        pagamentoBoleto.getValor(),
                                        pagamentoBoleto.getConta().getId(),
                                        pagamentoBoleto.getCodigoBarras(),
                                        pagamentoBoleto.getNomeBeneficiario(),
                                        pagamentoBoleto.getInstituicaoFinanceira(),
                                        pagamentoBoleto.getCategoria()
                                )
                        )).toList()
        );

        lista.addAll(
                pagamentoDebitoList.stream()
                        .map(pagamentoDebito -> new TransacaoContaResponse(
                                pagamentoDebito.getTipo(),
                                pagamentoDebito.getData(),
                                new PagamentoDebitoResponse(
                                        pagamentoDebito.getId(),
                                        pagamentoDebito.getValor(),
                                        pagamentoDebito.getConta().getId(),
                                        pagamentoDebito.getNomeEstabelecimento(),
                                        pagamentoDebito.getCategoria()
                                )
                        )).toList()
        );

        lista.addAll(
                pixEnviado.stream()
                        .map(pix -> new TransacaoContaResponse(
                                pix.getTipo(),
                                pix.getData(),
                                new PixResponse(
                                        pix.getId(),
                                        pix.getValor(),
                                        pix.getConta().getId(),
                                        pix.getChavePixDestino(),
                                        pix.getCategoria()
                                )
                        )).toList()
        );

        lista.addAll(
                transferenciasRecebidas.stream()
                        .map(p -> new TransacaoContaResponse(
                                "TRANSFERENCIA_RECEBIDA",
                                p.getData(),
                                new TransferenciaResponse(
                                        p.getData(),
                                        p.getContaOrigem().getId(),
                                        p.getContaOrigem().getNumero(),
                                        p.getValor(),
                                        p.getContaDestino() != null ? p.getContaDestino().getId() : null,
                                        p.getNumeroContaDestino(),
                                        p.getCategoria()
                                )
                        ))
                        .toList()
        );

        lista.addAll(
                pixRecebido.stream()
                        .map(pix -> new TransacaoContaResponse(
                                "PIX_RECEBIDO",
                                pix.getData(),
                                new PixResponse(
                                        pix.getId(),
                                        pix.getValor(),
                                        pix.getConta().getId(),
                                        pix.getChavePixDestino(),
                                        pix.getCategoria()
                                )
                        ))
                        .toList()
        );

        lista.sort(Comparator.comparing(TransacaoContaResponse::data));
        return lista;
    }

    public static List<TransacaoCarteiraResponse> listarTransacoesCarteiraResponse(
            List<Conta_Carteira> contaCarteiraList
    ) {
        return new ArrayList<>(
                contaCarteiraList.stream().map(cc -> new TransacaoCarteiraResponse(
                        cc.getConta().getId(),
                        cc.getConta().getNumero(),
                        cc.getCarteira().getId(),
                        cc.getTipo(),
                        cc.getData(),
                        cc.getValor()
                )).toList());
    }

    public static CriarCarteira toCriarCarteiraEntity(CriarCarteiraRequest request, Conta conta) {
        return CriarCarteira
                .builder()
                .conta(conta)
                .tipo("CRIAR_CARTEIRA")
                .valor(request.saldo())
                .data(LocalDateTime.parse(request.dataHoraLocal()))
                .build();
    }


    public static DeletarCarteira toDeletarCarteiraEntity(Conta conta, Carteira carteira, DeletarCarteiraRequest request) {
        return DeletarCarteira
                .builder()
                .conta(conta)
                .tipo("DELETAR_CARTEIRA")
                .valor(carteira.getSaldo())
                .data(LocalDateTime.parse(request.dataHoraLocal()))
                .build();
    }

    public static PagamentoBoleto toPagamentoBoletoEntity(Conta conta, CriarPagamentoBoletoRequest request) {
        return PagamentoBoleto
                .builder()
                .conta(conta)
                .tipo("PAGAMENTO_BOLETO")
                .valor(request.valor())
                .codigoBarras(request.codigoBarras())
                .dataVencimento(request.dataVencimento())
                .nomeBeneficiario(request.nomeBeneficiario())
                .instituicaoFinanceira(request.instituicaoFinanceira())
                .data(LocalDateTime.parse(request.dataHoraLocal()))
                .categoria(request.categoria())
                .build();
    }

    public static CriarPagamentoBoletoResponse toCriarPagamentoBoletoResponse(PagamentoBoleto pagamentoBoleto) {
        return new CriarPagamentoBoletoResponse(
                pagamentoBoleto.getId(),
                pagamentoBoleto.getValor(),
                pagamentoBoleto.getData(),
                pagamentoBoleto.getCodigoBarras(),
                pagamentoBoleto.getDataVencimento(),
                pagamentoBoleto.getNomeBeneficiario(),
                pagamentoBoleto.getInstituicaoFinanceira(),
                pagamentoBoleto.getCategoria()
        );
    }

    public static PagamentoDebito toPagamentoDebitoEntity(CriarPagamentoDebitoRequest request, Conta conta) {
        return PagamentoDebito
                .builder()
                .conta(conta)
                .tipo("PAGAMENTO_DEBITO")
                .valor(request.valor())
                .nomeEstabelecimento(request.nomeEstabelecimento())
                .data(LocalDateTime.parse(request.dataHoraLocal()))
                .categoria(request.categoria())
                .build();
    }

    public static CriarPagamentoDebitoResponse toCriarPagamentoDebitoResponse(PagamentoDebito pagamentoDebito) {
        return new CriarPagamentoDebitoResponse(
                pagamentoDebito.getId(),
                pagamentoDebito.getTipo(),
                pagamentoDebito.getData(),
                pagamentoDebito.getValor(),
                pagamentoDebito.getNomeEstabelecimento(),
                pagamentoDebito.getCategoria()
        );
    }

    public static Pix toPixEntity(CriarTransferenciaPixRequest request, Conta conta) {
        return Pix
                .builder()
                .conta(conta)
                .tipo("PIX")
                .valor(request.valor())
                .chavePixDestino(request.chavePixDestino())
                .data(LocalDateTime.parse(request.dataHoraLocal()))
                .categoria(request.categoria())
                .build();
    }

    public static CriarTransferenciaPixResponse toCriarTransferenciaPixResponse(Pix pix, Conta conta) {
        return new CriarTransferenciaPixResponse(
                pix.getId(),
                pix.getTipo(),
                pix.getData(),
                pix.getValor(),
                pix.getChavePixDestino(),
                conta.getUsuario().getNome(),
                pix.getCategoria()
        );
    }
}

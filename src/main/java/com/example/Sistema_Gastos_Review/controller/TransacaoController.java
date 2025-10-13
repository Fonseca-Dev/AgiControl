package com.example.Sistema_Gastos_Review.controller;

import com.example.Sistema_Gastos_Review.dto.request.*;
import com.example.Sistema_Gastos_Review.dto.response.BaseResponse;
import com.example.Sistema_Gastos_Review.service.TransacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/{idUsuario}/contas/{idConta}")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping("/transacoes/saques")
    public ResponseEntity<BaseResponse> criarSaque(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestBody CriarSaqueContaRequest request){
        BaseResponse response = transacaoService.criarSaqueConta(idUsuario, idConta, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/transacoes/depositos")
    public ResponseEntity<BaseResponse> criarDeposito(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestBody CriarDepositoContaRequest request){
        BaseResponse response = transacaoService.criarDepositoConta(idUsuario, idConta, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/carteiras/{idCarteira}/transacoes/depositos")
    public ResponseEntity<BaseResponse> criarDepositoNaCarteira(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @PathVariable String idCarteira,
            @RequestBody CriarDepositoNaCarteiraRequest request
            ){
        BaseResponse response = transacaoService.criarDepositoNaCarteira(idUsuario, idConta, idCarteira, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/carteiras/{idCarteira}/transacoes/saques")
    public ResponseEntity<BaseResponse> criarSaqueNaCarteira(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @PathVariable String idCarteira,
            @RequestBody CriarSaqueNaCarteiraRequest request
    ){
        BaseResponse response = transacaoService.criarSaqueNaCarteira(idUsuario, idConta, idCarteira, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/transacoes/transferencias")
    public ResponseEntity<BaseResponse> criarTransferencia(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestBody CriarTransferenciaRequest request
            ){
        BaseResponse response = transacaoService.criarTransferencia(idUsuario, idConta, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/transacoes")
    public ResponseEntity<BaseResponse> listarTransacoesPorConta(
            @PathVariable String idUsuario,
            @PathVariable String idConta){
        BaseResponse response = transacaoService.listarTransacoesPorConta(idUsuario, idConta);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/carteiras/{idCarteira}/transacoes")
    public ResponseEntity<BaseResponse> listarTransacoesPorCarteira(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @PathVariable String idCarteira){
        BaseResponse response = transacaoService.listarTransacoesPorCarteira(idUsuario, idConta, idCarteira);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/transacoes/categorias-mais-usadas")
    public ResponseEntity<BaseResponse> categoriasMaisUsadasPorConta(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestParam int ano,
            @RequestParam int mes){
        BaseResponse response = transacaoService.categoriasMaisUsadasPorContaMesAno(idUsuario, idConta, ano, mes);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/transacoes/pagamentosBoleto")
    public ResponseEntity<BaseResponse> criarPagamentoBoleto(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestBody CriarPagamentoBoletoRequest request
            ) {
        BaseResponse response = transacaoService.criarPagamentoBoleto(idUsuario, idConta, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/transacoes/pagamentosDebito")
    public ResponseEntity<BaseResponse> criarPagamentoDebito(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestBody CriarPagamentoDebitoRequest request
    ){
        BaseResponse response = transacaoService.criarPagamentoDebito(idUsuario, idConta, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/transacoes/transferenciasPix")
    public ResponseEntity<BaseResponse> criarTransferenciaPix(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestBody CriarTransferenciaPixRequest request
    ){
        BaseResponse response = transacaoService.criarTransferenciaPix(idUsuario, idConta, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/gastosPorAno")
    public ResponseEntity<BaseResponse> calcularGastoDaContaPorAno(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestParam int ano
    ){
        BaseResponse response = transacaoService.calcularGastoDaContaPorAno(idUsuario, idConta, ano);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/gastosPorMes")
    public ResponseEntity<BaseResponse> calcularGastoDaContaPorMes(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestParam int ano,
            @RequestParam int mes
    ){
        BaseResponse response = transacaoService.calcularGastoDaContaPorMes(idUsuario, idConta, ano, mes);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/resumoGastoDaContaPorMes")
    public ResponseEntity<BaseResponse> calcularGastoDaContaPorMesPorTipoTransacao(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestParam int ano,
            @RequestParam int mes
    ){
        BaseResponse response = transacaoService.calcularGastoDaContaPorMesPorTipoTransacao(idUsuario, idConta, ano, mes);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/gastosPorDia")
    public ResponseEntity<BaseResponse> calcularGastoDaContaPorDia(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestParam int ano,
            @RequestParam int mes,
            @RequestParam int dia
    ){
        BaseResponse response = transacaoService.calcularGastoDaContaPorDia(idUsuario, idConta, ano, mes, dia);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/transacoes/por-dia")
    public ResponseEntity<BaseResponse> listarTransacoesPorCategoriaPorDia(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestParam int ano,
            @RequestParam int mes,
            @RequestParam int dia,
            @RequestParam String categoria
    ){
        BaseResponse response = transacaoService.listarTransacoesPorCategoriaPorDia(idUsuario, idConta, categoria, ano, mes, dia);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/transacoes/por-mes")
    public ResponseEntity<BaseResponse> listarTransacoesPorCategoriaPorMes(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestParam int ano,
            @RequestParam int mes,
            @RequestParam String categoria
    ){
        BaseResponse response = transacaoService.listarTransacoesPorCategoriaPorMes(idUsuario, idConta, categoria, ano, mes);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/transacoes/{idTransacao}")
    public ResponseEntity<BaseResponse> buscarTransacao(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @PathVariable String idTransacao
    ){
        BaseResponse response = transacaoService.buscarTransacao(idUsuario, idConta, idTransacao);
        return ResponseEntity.status(response.status()).body(response);
    }




}

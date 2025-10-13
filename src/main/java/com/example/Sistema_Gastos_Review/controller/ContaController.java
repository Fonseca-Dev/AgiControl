package com.example.Sistema_Gastos_Review.controller;

import com.example.Sistema_Gastos_Review.dto.request.CriarChavePixRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarContaRequest;
import com.example.Sistema_Gastos_Review.dto.response.BaseResponse;
import com.example.Sistema_Gastos_Review.service.ContaService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuarios/{idUsuario}/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> criarConta(
            @PathVariable String idUsuario,
            @RequestBody CriarContaRequest request){
        BaseResponse response = contaService.criarConta(idUsuario, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deletarConta(
            @PathVariable String idUsuario,
            @PathVariable String id) {
        BaseResponse response = contaService.deletarConta(idUsuario, id);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> buscarContas(
            @PathVariable String idUsuario){
        BaseResponse response = contaService.buscarContas(idUsuario);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/{idConta}")
    public ResponseEntity<BaseResponse> criarChavePix(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestBody CriarChavePixRequest request
            ) {
        BaseResponse response = contaService.criarChavePix(idUsuario, idConta, request);
        return ResponseEntity.status(response.status()).body(response);
    }
}

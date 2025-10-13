package com.example.Sistema_Gastos_Review.controller;

import com.example.Sistema_Gastos_Review.dto.request.AlterarCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.request.DeletarCarteiraRequest;
import com.example.Sistema_Gastos_Review.dto.response.BaseResponse;
import com.example.Sistema_Gastos_Review.service.CarteiraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios/{idUsuario}/contas/{idConta}/carteiras")
public class CarteiraController {

    private final CarteiraService carteiraService;

    public CarteiraController(CarteiraService carteiraService) {
        this.carteiraService = carteiraService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> criarCarteira(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @RequestBody CriarCarteiraRequest request) {
        BaseResponse response = carteiraService.criarCarteira(idUsuario, idConta, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PutMapping("/{idCarteira}")
    public ResponseEntity<BaseResponse> alterarCarteira(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @PathVariable String idCarteira,
            @RequestBody AlterarCarteiraRequest request) {
        BaseResponse response = carteiraService.alterarCarteira(idUsuario, idConta, idCarteira, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @DeleteMapping("/{idCarteira}")
    public ResponseEntity<BaseResponse> deletarCarteira(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @PathVariable String idCarteira,
            @RequestBody DeletarCarteiraRequest request
    ) {
        BaseResponse response = carteiraService.deletarCarteira(idUsuario, idConta, idCarteira, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> buscarCarteirasPorConta(
            @PathVariable String idUsuario,
            @PathVariable String idConta
    ){
        BaseResponse response = carteiraService.buscarCarteirasPorConta(idUsuario, idConta);
        return ResponseEntity.status(response.status()).body(response);
    }

    @GetMapping("/{idCarteira}")
    public ResponseEntity<BaseResponse> buscarCarteiraPeloID(
            @PathVariable String idUsuario,
            @PathVariable String idConta,
            @PathVariable String idCarteira
    ){
        BaseResponse response = carteiraService.buscarCarteiraPeloID(idUsuario, idConta, idCarteira);
        return ResponseEntity.status(response.status()).body(response);
    }
}

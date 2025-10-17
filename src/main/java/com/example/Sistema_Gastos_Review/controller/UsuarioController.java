package com.example.Sistema_Gastos_Review.controller;

import com.example.Sistema_Gastos_Review.dto.request.AlterarUsuarioRequest;
import com.example.Sistema_Gastos_Review.dto.request.ConfirmarSenhaRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarUsuarioRequest;
import com.example.Sistema_Gastos_Review.dto.request.LoginUsuarioRequest;
import com.example.Sistema_Gastos_Review.dto.response.BaseResponse;
import com.example.Sistema_Gastos_Review.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
        origins = {"https://fonseca-dev.github.io", "http://localhost:5500"},
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> criarUsuario(@RequestBody CriarUsuarioRequest request) {
        BaseResponse response = usuarioService.criarUsuario(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PutMapping("/{id}/contas/{idConta}")
    public ResponseEntity<BaseResponse> alterarUsuario(
            @PathVariable String id,
            @PathVariable String idConta,
            @RequestBody AlterarUsuarioRequest request) {
        BaseResponse response = usuarioService.alterarUsuario(id, idConta, request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deletarUsuario(@PathVariable String id) {
        BaseResponse response = usuarioService.deletarUsuario(id);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> loginPorEmailESenha(@RequestBody LoginUsuarioRequest request) {
        BaseResponse response = usuarioService.loginPorEmailESenha(request);
        return ResponseEntity.status(response.status()).body(response);
    }

    @PostMapping("/{idUsuario}/verificara-senha")
    public ResponseEntity<BaseResponse> confirmarSenha(
            @PathVariable String idUsuario,
            @RequestBody ConfirmarSenhaRequest request
    ) {
        BaseResponse response = usuarioService.confirmarSenha(idUsuario, request);
        return ResponseEntity.status(response.status()).body(response);
    }
}


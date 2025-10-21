package com.example.Sistema_Gastos_Review.service;

import com.example.Sistema_Gastos_Review.dto.request.AlterarUsuarioRequest;
import com.example.Sistema_Gastos_Review.dto.request.ConfirmarSenhaRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarUsuarioRequest;
import com.example.Sistema_Gastos_Review.dto.request.LoginUsuarioRequest;
import com.example.Sistema_Gastos_Review.dto.response.BaseResponse;
import com.example.Sistema_Gastos_Review.dto.response.LoginUsuarioResponse;
import com.example.Sistema_Gastos_Review.entity.Conta;
import com.example.Sistema_Gastos_Review.entity.Usuario;
import com.example.Sistema_Gastos_Review.mapper.ContaMapper;
import com.example.Sistema_Gastos_Review.mapper.UsuarioMapper;
import com.example.Sistema_Gastos_Review.repository.ContaRepository;
import com.example.Sistema_Gastos_Review.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final ContaRepository contaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, ContaRepository contaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.contaRepository = contaRepository;
    }

    public BaseResponse criarUsuario(CriarUsuarioRequest request) {
        if (Objects.isNull(request)) {
            return new BaseResponse("Request esta nulo.", HttpStatus.BAD_REQUEST, null);
        }
        Optional<Usuario> encontrado = usuarioRepository.findByEmail(request.email());
        if (encontrado.isPresent()) {
            return new BaseResponse("Usuario ja cadastrado com o email informado.", HttpStatus.CONFLICT, null);
        }
        Usuario novoUsuario = UsuarioMapper.toEntity(request);
        usuarioRepository.save(novoUsuario);

        // 2️⃣ Gerar próximo número de conta
        Long proximoNumero = contaRepository.findTopByOrderByNumeroDesc()
                .map(c -> c.getNumero() + 1)
                .orElse(1000L); // começa de 1000 se não houver contas

        // 3️⃣ Criar a conta corrente
        Conta novaConta = ContaMapper.criarContaCorrente(novoUsuario, proximoNumero);
        contaRepository.save(novaConta);
        return new BaseResponse("Usuario cadastrado com sucesso.", HttpStatus.CREATED, UsuarioMapper.toCriarUsuarioReponse(novoUsuario));
    }

    public BaseResponse alterarUsuario(String id, String idConta, AlterarUsuarioRequest request) {
        Optional<Usuario> encontrado = usuarioRepository.findById(id);
        if (encontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Optional<Usuario> encontradoPeloEmailRequest = usuarioRepository.findByEmail(request.email());
        if (encontradoPeloEmailRequest.isPresent() && !request.email().equalsIgnoreCase(encontrado.get().getEmail())) {
            return new BaseResponse(
                    "Email ja cadastrado por outro usuario.",
                    HttpStatus.CONFLICT,
                    null);
        }
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if (contaEncontrada.isEmpty()) {
            return new BaseResponse("Conta nao encontrada!",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();

        Usuario alterado = encontrado.get();
        alterado.setNome(request.nome());
        alterado.setEmail(request.email());
        conta.setChavePix(request.email());
        alterado.setSenha(request.senha());
        usuarioRepository.save(alterado);
        contaRepository.save(conta);

        return new BaseResponse(
                "Usuario alterado com sucesso.",
                HttpStatus.OK,
                UsuarioMapper.toAlterarUsuarioResponse(alterado));
    }

    public BaseResponse deletarUsuario(String id) {
        Optional<Usuario> encontrado = usuarioRepository.findById(id);
        if (encontrado.isPresent()) {
            encontrado.get().setEstado("DELETADO");
            usuarioRepository.save(encontrado.get());
            return new BaseResponse(
                    "Usuario deletado com sucesso",
                    HttpStatus.OK,
                    UsuarioMapper.toDeletarUsuarioResponse(encontrado.get()));
        }
        return new BaseResponse(
                "Usuario nao encontrado.",
                HttpStatus.NOT_FOUND,
                null);
    }

    public BaseResponse loginPorEmailESenha(LoginUsuarioRequest request) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(request.email());
        if (usuarioEncontrado.isEmpty()) {
            return new BaseResponse(
                    "E-mail ou senha incorreta.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = usuarioEncontrado.get();
        if (!usuario.getSenha().equalsIgnoreCase(request.senha())) {
            return new BaseResponse(
                    "E-mail ou senha incorreta.",
                    HttpStatus.CONFLICT,
                    null);
        }

        LoginUsuarioResponse loginUsuarioResponse = new LoginUsuarioResponse(usuario.getId(), usuario.getNome());

        return new BaseResponse(
                "Login efetuado com suceso.",
                HttpStatus.OK,
                loginUsuarioResponse);
    }

    public BaseResponse confirmarSenha(String idUsuario, ConfirmarSenhaRequest request) {
        Optional<Usuario> encontrado = usuarioRepository.findById(idUsuario);
        if (encontrado.isEmpty()) {
            return new BaseResponse(
                    "Usuario não encontrado",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        Usuario usuario = encontrado.get();
        if (!usuario.getSenha().equalsIgnoreCase(request.senha())) {
            return new BaseResponse(
                    "Senha incorreta.",
                    HttpStatus.CONFLICT,
                    null
            );
        }
        return new BaseResponse(
                "Senha válida.",
                HttpStatus.OK,
                null
        );
    }

}

package com.example.Sistema_Gastos_Review.service;

import com.example.Sistema_Gastos_Review.dto.request.CriarChavePixRequest;
import com.example.Sistema_Gastos_Review.dto.request.CriarContaRequest;
import com.example.Sistema_Gastos_Review.dto.response.BaseResponse;
import com.example.Sistema_Gastos_Review.dto.response.BuscarContasReponse;
import com.example.Sistema_Gastos_Review.dto.response.CriarChavePixResponse;
import com.example.Sistema_Gastos_Review.entity.Conta;
import com.example.Sistema_Gastos_Review.entity.Usuario;
import com.example.Sistema_Gastos_Review.mapper.ContaMapper;
import com.example.Sistema_Gastos_Review.mapper.UsuarioMapper;
import com.example.Sistema_Gastos_Review.repository.ContaRepository;
import com.example.Sistema_Gastos_Review.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
public class ContaService {
    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;

    public ContaService(ContaRepository contaRepository, UsuarioRepository usuarioRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public BaseResponse criarConta(String idUsuario, CriarContaRequest request){
        if(Objects.isNull(request)){
            return new BaseResponse("Request esta nulo.",HttpStatus.BAD_REQUEST,null);
        }
        Optional<Usuario> encontrado = usuarioRepository.findById(idUsuario);
        if(encontrado.isEmpty()){
            return new BaseResponse("Usuario nao encontrado.", HttpStatus.NOT_FOUND, null);
        }
        Usuario usuario = encontrado.get();
        if(usuario.getEstado().equalsIgnoreCase("DELETADA")){
            return new BaseResponse("Usuario deletado.", HttpStatus.CONFLICT, UsuarioMapper.toDeletarUsuarioResponse(usuario));
        }

        // 2️⃣ Gerar próximo número de conta
        Long proximoNumero = contaRepository.findTopByOrderByNumeroDesc()
                .map(c -> c.getNumero() + 1)
                .orElse(1000L); // começa de 1000 se não houver contas
        Optional<Conta> encontrada = contaRepository.findByUsuarioIdAndTipo(idUsuario, request.tipo());
        if(request.tipo().equalsIgnoreCase("corrente")){
            if(encontrada.isEmpty()){
                return new BaseResponse("Conta corrente do usuario não encontrada.",HttpStatus.NOT_FOUND, null);
            }
            return new BaseResponse("Usuario ja possui uma conta desse tipo.",HttpStatus.CONFLICT, ContaMapper.toCriarContaResponse(encontrada.get()));
        }
        if (encontrada.isPresent()){
            return new BaseResponse("Usuario ja possui uma conta desse tipo.", HttpStatus.CONFLICT, ContaMapper.toCriarContaResponse(encontrada.get()));
        }
        Conta nova = ContaMapper.toEntity(request.tipo().toUpperCase(),usuario,proximoNumero);
        contaRepository.save(nova);
        return new BaseResponse(
                "Conta criada com sucesso.",
                HttpStatus.CREATED,
                ContaMapper.toCriarContaResponse(nova));
    }

    public BaseResponse deletarConta(String idUsuario, String idConta){
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if(usuarioEncontrado.isEmpty()){
            return new BaseResponse(
                    "Delacao de conta negada! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Usuario usuario = usuarioEncontrado.get();
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if(contaEncontrada.isEmpty()){
            return new BaseResponse(
                    "Delacao de conta negada! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();
        if(!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())){
            return new BaseResponse(
                    "Delacao de conta negada! Conta nao pertence ao usuario informado.",
                    HttpStatus.CONFLICT,
                    null);
        }
        conta.setEstado("DELETADA");
        contaRepository.save(conta);
        return new BaseResponse(
                "Conta deleta com sucesso.",
                HttpStatus.OK,
                ContaMapper.toDeletarContaResponse(conta));
    }

    public BaseResponse buscarContas(String idUsuario){
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if(usuarioEncontrado.isEmpty()){
            return new BaseResponse(
                    "Busca de contas negada! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Usuario usuario = usuarioEncontrado.get();
        List<Conta> contas = contaRepository.findByUsuarioId(idUsuario);
        if(contas.isEmpty()){
            return new BaseResponse(
                    "Nenhuma conta encontrada.",
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        return new BaseResponse(
                "Contas encontradas.",
                HttpStatus.OK,
                ContaMapper.toBuscarContaResponse(contas));
    }

    public BaseResponse criarChavePix (String idUsuario, String idConta, CriarChavePixRequest request){
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(idUsuario);
        if(usuarioEncontrado.isEmpty()){
            return new BaseResponse(
                    "Criação de chave pix negada! Usuario nao encontrado.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Usuario usuario = usuarioEncontrado.get();
        Optional<Conta> contaEncontrada = contaRepository.findById(idConta);
        if(contaEncontrada.isEmpty()){
            return new BaseResponse(
                    "Criação de chave pix negada! Conta nao encontrada.",
                    HttpStatus.NOT_FOUND,
                    null);
        }
        Conta conta = contaEncontrada.get();
        if(!conta.getUsuario().getId().equalsIgnoreCase(usuario.getId())){
            return new BaseResponse(
                    "Criação de chave pix",
                    HttpStatus.CONFLICT,
                    null);
        }

        Optional<Conta> chavePixEncontrada = contaRepository.findByChavePix(request.chavePix());
        if(chavePixEncontrada.isPresent()){
            return new BaseResponse(
                    "Criação de chave pix negada! Chave já cadastrada.",
                    HttpStatus.CONFLICT,
                    null
            );
        }

        conta.setChavePix(request.chavePix());
        contaRepository.save(conta);
        return new BaseResponse(
                "Chave pix cadastrada com sucesso!",
                HttpStatus.CREATED,
                ContaMapper.toCriarChavePixResponse(conta)
        );
    }
}



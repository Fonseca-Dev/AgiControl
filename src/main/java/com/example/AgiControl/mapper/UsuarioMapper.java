package com.example.Sistema_Gastos_Review.mapper;

import com.example.Sistema_Gastos_Review.dto.request.CriarUsuarioRequest;
import com.example.Sistema_Gastos_Review.dto.response.AlterarUsuarioResponse;
import com.example.Sistema_Gastos_Review.dto.response.CriarUsuarioResponse;
import com.example.Sistema_Gastos_Review.dto.response.DeletarUsuarioResponse;
import com.example.Sistema_Gastos_Review.entity.Usuario;

import java.time.LocalDateTime;


public class UsuarioMapper {
    public static Usuario toEntity(CriarUsuarioRequest request) {
        return Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(request.senha())
                .estado("ATIVO")
                .dataCriacao(LocalDateTime.now())
                .build();
    }

    public static CriarUsuarioResponse toCriarUsuarioReponse(Usuario usuario) {
        return new CriarUsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getEstado());
    }

    public static AlterarUsuarioResponse toAlterarUsuarioResponse(Usuario usuario) {
        return new AlterarUsuarioResponse(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getEstado()
        );
    }

    public static DeletarUsuarioResponse toDeletarUsuarioResponse(Usuario usuario){
        return new DeletarUsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getEstado()
        );
    }
}

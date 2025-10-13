package com.example.Sistema_Gastos_Review.entity;

import com.example.Sistema_Gastos_Review.repository.TransacaoComCategoria;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("PAGAMENTO_BOLETO")
public class PagamentoBoleto extends Transacao implements TransacaoComCategoria {

    @NotBlank
    private String codigoBarras;
    @NotNull
    private LocalDateTime dataVencimento;
    @NotBlank
    private String nomeBeneficiario;
    @NotBlank
    private String instituicaoFinanceira;
    @NotBlank
    private String categoria;

    //ManyToOne para criação do relacionamento entre as entidades
    //JoinColumn para criação de uma coluna para identificar a chave estrangeira
    @ManyToOne
    @JoinColumn(name = "id_conta",nullable = false)
    private Conta conta;
}

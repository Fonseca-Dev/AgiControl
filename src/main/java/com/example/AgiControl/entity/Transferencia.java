package com.example.Sistema_Gastos_Review.entity;

import com.example.Sistema_Gastos_Review.repository.TransacaoComCategoria;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("PAGAMENTO")
public class Transferencia extends Transacao implements TransacaoComCategoria {

    //ManyToOne para criação do relacionamento entre as entidades
    //JoinColumn para criação de uma coluna para identificar a chave estrangeira
    @ManyToOne
    @JoinColumn(name = "id_contaOrigem",nullable = false)
    private Conta contaOrigem;
    @ManyToOne
    @JoinColumn(name = "id_contaDestino",nullable = true)
    private Conta contaDestino;


    private Long numeroContaDestino;
    @NotBlank
    private String categoria;

}

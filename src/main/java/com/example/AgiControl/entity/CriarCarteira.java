package com.example.Sistema_Gastos_Review.entity;

import jakarta.persistence.*;
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
@DiscriminatorValue("CRIAR_CARTEIRA")
public class CriarCarteira extends Transacao{
    //ManyToOne para criação do relacionamento entre as entidades
    //JoinColumn para criação de uma coluna para identificar a chave estrangeira
    @ManyToOne
    @JoinColumn(name = "id_conta",nullable = false)
    private Conta conta;

    @OneToOne
    @JoinColumn(name = "id_carteira", nullable = false, unique = true)
    private Carteira carteira;
}

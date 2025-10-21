package com.example.Sistema_Gastos_Review.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /*Utilizei a anotação abaixo para que não seja permitido
    Garante no banco a unicidade (mesmo se alguém inserir manualmente).
    Impede nulos, o que faz sentido pra um número de conta. */
    @Column(unique = true, nullable = false)
    private Long numero;

    @NotNull
    private BigDecimal saldo;

    @NotBlank
    private String tipo;

    @NotBlank
    private String estado;

    @Version
    private Long version; // campo para controle de versão (lock otimista)

    private String chavePix;


    @ManyToOne
    @JoinColumn(name = "id_usuario")
    Usuario usuario;

    /*
    @OneToMany indica que a entidade atual (Conta) possui uma coleção de objetos de outra entidade
    (por exemplo, Transacao).
    mappedBy = "conta" diz qual atributo da outra entidade é responsável pelo relacionamento.
     */
    @OneToMany(mappedBy = "conta")
    /*
    Essa anotação é usada pelo Jackson para serializar/deserializar objetos JSON sem criar loop infinito.
     */
    @JsonIgnoreProperties("conta") // evita loop
    private List<Deposito> depositos;

    @OneToMany(mappedBy = "conta")
    @JsonIgnoreProperties("conta") // evita loop
    private List<Saque> saques;

    @OneToMany(mappedBy = "conta")
    @JsonIgnoreProperties("conta") // evita loop
    private List<Conta_Carteira> trasacoesContaCarteiras;

    @OneToMany(mappedBy = "contaOrigem")
    @JsonIgnoreProperties("conta") // evita loop
    private List<Transferencia> pagamentoTransferencias;

    @OneToMany(mappedBy = "conta")
    @JsonIgnoreProperties("conta")
    private List<Carteira> carteiras;

    @OneToMany(mappedBy = "conta")
    @JsonIgnoreProperties("conta")
    private List<CriarCarteira> criarCarteiras;

    @OneToMany(mappedBy = "conta")
    @JsonIgnoreProperties("conta")
    private List<DeletarCarteira> deletarCarteiras;

    @OneToMany(mappedBy = "conta")
    @JsonIgnoreProperties("conta")
    private List<PagamentoBoleto> pagamentoBoletos;

    @OneToMany(mappedBy = "conta")
    @JsonIgnoreProperties("conta")
    private List<PagamentoDebito> pagamentoDebitos;

    @OneToMany(mappedBy = "conta")
    @JsonIgnoreProperties("conta")
    private List<Pix> pixList;

}

package com.example.demo.Cadastro;

import com.example.demo.Cadastro.Constante.Quantidade;
import com.example.demo.comeco.Empresa;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ponto")
@Getter
@Setter
public class Ponto {
    /**
     * Chave primária em UUID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    protected UUID codigo;

    private String setor;

    private String equipamento;

    @ManyToOne
    private Empresa empresa;

    private Quantidade quantidade;

    private LocalDateTime dataHoraLubrificacao;

    private LocalDate dataProxLubrificacao;

    private Integer quantidadeDeGraxa;

    @Column(name = "componentes do equipamento")
    private String componentes;

    private String operacao;

    @Column(name = "observacaoes")
    private String obs;

}

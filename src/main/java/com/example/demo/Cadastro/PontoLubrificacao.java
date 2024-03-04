package com.example.demo.Cadastro;

import com.example.demo.Cadastro.Constante.UnidadeMedida;
import com.example.demo.Lubrificantes.Lubrificante;
import com.example.demo.comeco.Empresa;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "pontoLubrificacao")
@Getter
@Setter
public class PontoLubrificacao {
    /**
     * Chave primária em UUID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    protected UUID codigo;

    private String setor;

    private String ponto;

    private String equipamento;

    private String tag;

    @ManyToOne
    private Empresa empresa;

    private LocalDate dataHoraLubrificacao;

    private LocalDate dataProxLubrificacao;

    private Integer quantidadeDeGraxa;

    private Integer quantidadeDeLubrificante;

    private UnidadeMedida unidadeMedida;

    @Column(name = "componentes_do_equipamento")
    private String componentes;

    private String operacao;

    @Column(name = "observacaoes")
    private String obs;

    @ManyToOne
    private Lubrificante lubrificante;

}

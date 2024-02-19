package com.example.demo.comeco;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "empresa")
@Getter @Setter
public class Empresa {

    /**
     * Chave primária em UUID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    protected UUID codigo;

    private String nome;

//    public Empresa(String nome ) {
//        this.nome = nome;
//
//    }

    public Empresa() {

    }

}

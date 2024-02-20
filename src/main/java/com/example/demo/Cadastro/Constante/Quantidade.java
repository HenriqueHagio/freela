package com.example.demo.Cadastro.Constante;

public enum Quantidade {
    GRAMAS("Gramas"),
    LITROS("Litros");

    private String descricao;

    // Constructor
    Quantidade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

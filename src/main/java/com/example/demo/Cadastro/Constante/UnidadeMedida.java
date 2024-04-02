package com.example.demo.Cadastro.Constante;

public enum UnidadeMedida {
    GRAMAS("Gramas"),
    LITROS("Litros");

    private String descricao;

    // Constructor
    UnidadeMedida(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

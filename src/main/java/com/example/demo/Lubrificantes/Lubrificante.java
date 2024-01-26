package com.example.demo.Lubrificantes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Lubrificante {
    private final StringProperty codigo;
    private final StringProperty descricao;

    public Lubrificante(String codigo, String descricao) {
        this.codigo = new SimpleStringProperty(codigo);
        this.descricao = new SimpleStringProperty(descricao);
    }

    public String getCodigo() {
        return codigo.get();
    }

    public StringProperty codigoProperty() {
        return codigo;
    }

    public String getDescricao() {
        return descricao.get();
    }

    public StringProperty descricaoProperty() {
        return descricao;
    }

    public String getNome() {
        return descricao.get();
    }
}

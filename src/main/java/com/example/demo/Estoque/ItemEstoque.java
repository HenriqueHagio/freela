package com.example.demo.Estoque;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ItemEstoque {
    private String nomeProduto;
    private IntegerProperty quantidadeProperty = new SimpleIntegerProperty();
    private String unidade;
    private String lubrificante;

    public ItemEstoque(String nomeProduto, int quantidade, String unidade) {
        this.nomeProduto = nomeProduto;
        this.quantidadeProperty.set(quantidade);
        this.unidade = unidade;
    }

    public ItemEstoque() {
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public int getQuantidade() {
        return quantidadeProperty.get();
    }

    public void setQuantidade(int quantidade) {
        this.quantidadeProperty.set(quantidade);
    }

    public IntegerProperty quantidadeProperty() {
        return quantidadeProperty;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getLubrificante() {
        return lubrificante;
    }

    public void setLubrificante(String lubrificanteSelecionado) {
        this.lubrificante = lubrificanteSelecionado;
    }
}

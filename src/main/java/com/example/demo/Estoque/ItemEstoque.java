package com.example.demo.Estoque;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemEstoque {
    private String nomeProduto;
    private DoubleProperty quantidadeProperty = new SimpleDoubleProperty();
    private String unidade;
    private String lubrificante;

    public ItemEstoque(String nomeProduto, Double quantidade, String unidade) {
        this.nomeProduto = nomeProduto;
        this.quantidadeProperty.set(quantidade);
        this.unidade = unidade;
    }

    public ItemEstoque() {
    }

    public String getNomeProduto() {
        return nomeProduto;
    }


    public void setQuantidade(int quantidade) {
        this.quantidadeProperty.set(quantidade);
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

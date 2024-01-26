package com.example.demo.Estoque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GerenciadorEstoque {
    private final ObservableList<ItemEstoque> listaEstoque = FXCollections.observableArrayList();

    public ObservableList<ItemEstoque> getListaEstoque() {
        return listaEstoque;
    }

}

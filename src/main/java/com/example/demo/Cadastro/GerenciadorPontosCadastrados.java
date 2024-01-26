package com.example.demo.Cadastro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GerenciadorPontosCadastrados {
    private static final ObservableList<PontoLubrificacao> listaPontosCadastrados = FXCollections.observableArrayList();

    public static ObservableList<PontoLubrificacao> getListaPontosCadastrados() {
        return listaPontosCadastrados;
    }

}

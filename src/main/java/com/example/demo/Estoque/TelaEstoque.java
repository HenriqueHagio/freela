package com.example.demo.Estoque;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

import static javafx.application.Application.launch;

public class TelaEstoque {

    private TableView<ItemEstoque> table;

    public TelaEstoque(TableView<ItemEstoque> table) {
        this.table = table;
        initializeTable();
    }

    public TelaEstoque() {
        this(new TableView<>());
    }

    private void initializeTable() {
        if (table != null) {
            TableColumn<ItemEstoque, String> nomeColuna = new TableColumn<>("Nome do Produto");
            nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));

            TableColumn<ItemEstoque, Integer> quantidadeColuna = new TableColumn<>("Quantidade");
            quantidadeColuna.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

            TableColumn<ItemEstoque, String> unidadeColuna = new TableColumn<>("Unidade");
            unidadeColuna.setCellValueFactory(new PropertyValueFactory<>("unidade"));

            table.getColumns().addAll(nomeColuna, quantidadeColuna, unidadeColuna);
        }
    }

    public void atualizarEstoque(List<ItemEstoque> itens) {
        ObservableList<ItemEstoque> data = FXCollections.observableArrayList(itens);
        table.setItems(data);
    }

    public void mostrarTela() {
        launch();
    }

    public void start(Stage estoqueStage) {
    }
}

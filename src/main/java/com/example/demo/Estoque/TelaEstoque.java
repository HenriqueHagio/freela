// TelaEstoque.java
package com.example.demo.Estoque;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public final class TelaEstoque extends Application {

    private ObservableList<ItemEstoque> listaEstoque = FXCollections.observableArrayList();
    private TableView<ItemEstoque> table;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        table = new TableView<>();

        TableColumn<ItemEstoque, String> nomeColumn = new TableColumn<>("Nome");
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));

        TableColumn<ItemEstoque, Integer> quantidadeColumn = new TableColumn<>("Quantidade");
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        table.getColumns().addAll(nomeColumn, quantidadeColumn);

        VBox root = new VBox();

        TextField searchField = new TextField();
        searchField.setPromptText("Pesquisar Lubrificante");

        Button searchButton = new Button("Pesquisar");
        searchButton.setOnAction(event -> filtrarLubrificantes(searchField.getText()));

        root.getChildren().addAll(searchField, searchButton, table);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void filtrarLubrificantes(String searchTerm) {
        if (searchTerm.isEmpty()) {
            table.setItems(listaEstoque);
        } else {
            List<ItemEstoque> filteredList = listaEstoque.stream()
                    .filter(item -> item.getNomeProduto().toLowerCase().contains(searchTerm.toLowerCase()))
                    .collect(Collectors.toList());
            table.setItems(FXCollections.observableArrayList(filteredList));
        }
    }

    public void atualizarEstoque(List<ItemEstoque> items) {
        listaEstoque.addAll(items);
    }
}

package com.example.demo.Lubrificantes;

import com.example.demo.Estoque.LeitorExcel;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class TelaPesquisaLubrificantes extends Application {

    private List<Lubrificante> listaLubrificantes;
    private LubrificanteSelecionadoListener listener;
    private Lubrificante inputLubrificante;

    public TelaPesquisaLubrificantes(LubrificanteSelecionadoListener listener, Lubrificante inputLubrificante) {
        this.listener = listener;
        this.inputLubrificante = inputLubrificante;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        listaLubrificantes = LeitorExcel.getListaLubrificantes();

        if (listaLubrificantes != null) {
            TableView<Lubrificante> table = new TableView<>();

            TableColumn<Lubrificante, String> codigoColumn = new TableColumn<>("Código");
            codigoColumn.setCellValueFactory(cellData -> {
                String codigo = String.valueOf(cellData.getValue().getCodigo());
                return new SimpleStringProperty(codigo);
            });
            TableColumn<Lubrificante, String> descricaoColumn = new TableColumn<>("Descrição");
            descricaoColumn.setCellValueFactory(cellData -> {
                String codigo = cellData.getValue().getDescricao();
                return new SimpleStringProperty(codigo);
            });

            table.getColumns().addAll(codigoColumn, descricaoColumn);

            ObservableList<Lubrificante> data = FXCollections.observableArrayList(listaLubrificantes);
            table.setItems(data);

            table.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && table.getSelectionModel().getSelectedItem() != null) {
                    Lubrificante lubrificanteSelecionado = table.getSelectionModel().getSelectedItem();
                    String descricaoLubrificante = lubrificanteSelecionado.getDescricao();

                    if (listener != null && inputLubrificante != null) {
                        inputLubrificante.setText(descricaoLubrificante);
                        listener.onLubrificanteSelecionado(lubrificanteSelecionado);
                    }

                    primaryStage.close();
                }
            });

            VBox root = new VBox();

            TextField filtroField = new TextField();
            filtroField.setPromptText("Filtrar por descrição");

            filtroField.textProperty().addListener((observable, oldValue, newValue) -> {
                String filtro = newValue.toLowerCase().trim();

                ObservableList<Lubrificante> lubrificantesFiltrados = FXCollections.observableArrayList();

                for (Lubrificante lubrificante : listaLubrificantes) {
                    if (lubrificante.getDescricao().toLowerCase().contains(filtro)) {
                        lubrificantesFiltrados.add(lubrificante);
                    }
                }

                table.setItems(lubrificantesFiltrados);
            });

            root.getChildren().addAll(filtroField, table);

            Scene scene = new Scene(root, 400, 300);
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            System.out.println("Erro ao ler o arquivo.");
        }
    }

    public void setLubrificanteSelecionadoListener(LubrificanteSelecionadoListener listener) {
        this.listener = listener;
    }
}

package com.example.demo.Lubrificantes;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class TelaLubrificantes extends Application {

    private Stage stage;

    public TelaLubrificantes(Stage stage) {
        this.stage = stage;
    }



    public void start() {
        Lubrificante lubrificante = new Lubrificante();
        List<Lubrificante> lubrificantes = lubrificante.recuperarTodos();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Lubrificantes");
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

        int rowNum = 0;
        for (Lubrificante l : lubrificantes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(l.getCodigo());
            row.createCell(1).setCellValue(l.getDescricao());
        }


        ObservableList<Lubrificante> data = FXCollections.observableArrayList(lubrificantes);
        FilteredList<Lubrificante> filteredData = new FilteredList<>(data);

        table.setItems(filteredData);

        TextField searchField = new TextField();
        searchField.setPromptText("Pesquisar...");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(lubrificante1 -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return String.valueOf(lubrificante1.getCodigo()).toLowerCase().contains(lowerCaseFilter) ||
                        lubrificante1.getDescricao().toLowerCase().contains(lowerCaseFilter);
            });
        });

        VBox root = new VBox(10, searchField, table);
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Lista de Lubrificantes");
        stage.show();

    }

    @Override
    public void start(Stage stage) throws Exception {
        // Implementação opcional, dependendo do seu fluxo de inicialização
    }

    public static void main(String[] args) {
        launch(args);
    }
}

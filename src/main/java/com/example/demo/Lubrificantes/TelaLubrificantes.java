package com.example.demo.Lubrificantes;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TelaLubrificantes extends Application {

    private Stage stage;
    private String filePath;

    public TelaLubrificantes(Stage stage, String filePath) {
        this.stage = stage;
        this.filePath = filePath;
    }

    public TelaLubrificantes() {

    }

    public void start(Stage lubrificantesStage, String filePath) {
        if (this.filePath != null) {
            try (FileInputStream file = new FileInputStream(new File("C:\\Users\\Lucas\\OneDrive\\Documentos\\Codigos Lubvel\\BD_PRODUTOS_LUBVEL.xlsx"))) {
                Workbook workbook = WorkbookFactory.create(file);
                Sheet sheet = workbook.getSheetAt(0);

                TableView<Lubrificante> table = new TableView<>();
                TableColumn<Lubrificante, String> codigoColumn = new TableColumn<>("Código");
                codigoColumn.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
                TableColumn<Lubrificante, String> descricaoColumn = new TableColumn<>("Descrição");
                descricaoColumn.setCellValueFactory(cellData -> cellData.getValue().descricaoProperty());

                table.getColumns().addAll(codigoColumn, descricaoColumn);

                List<Lubrificante> listaLubrificantes = new ArrayList<>();

                Iterator<Row> rowIterator = sheet.iterator();
                rowIterator.next();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();

                    Cell codigoCell = row.getCell(0);
                    String codigo;
                    if (codigoCell.getCellType() == CellType.NUMERIC) {
                        codigo = String.valueOf((int) codigoCell.getNumericCellValue());
                    } else {
                        codigo = codigoCell.getStringCellValue();
                    }

                    Cell descricaoCell = row.getCell(1);
                    String descricao;
                    if (descricaoCell.getCellType() == CellType.NUMERIC) {
                        descricao = String.valueOf((int) descricaoCell.getNumericCellValue());
                    } else {
                        descricao = descricaoCell.getStringCellValue();
                    }

                    Lubrificante lubrificante = new Lubrificante(codigo, descricao);
                    listaLubrificantes.add(lubrificante);
                }

                ObservableList<Lubrificante> data = FXCollections.observableArrayList(listaLubrificantes);
                FilteredList<Lubrificante> filteredData = new FilteredList<>(data);

                table.setItems(filteredData);

                TextField searchField = new TextField();
                searchField.setPromptText("Pesquisar...");

                searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(lubrificante -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        String lowerCaseFilter = newValue.toLowerCase();

                        return lubrificante.getCodigo().toLowerCase().contains(lowerCaseFilter) ||
                                lubrificante.getDescricao().toLowerCase().contains(lowerCaseFilter);
                    });
                });

                VBox root = new VBox(10, searchField, table);
                Scene scene = new Scene(root, 600, 400);
                stage.setScene(scene);
                stage.setTitle("Lista de Lubrificantes");
                stage.show();

                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("O caminho do arquivo é nulo.");
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Implementação opcional, dependendo do seu fluxo de inicialização
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.example.demo.Estoque;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TelaCadastroEstoque extends Application {

    private static final String FILE_PATH = "C:\\Users\\Lucas\\OneDrive\\Documentos\\BD_PRODUTOS_LUBVEL.xlsx";
    private List<ItemEstoque> listaNovosProdutos = new ArrayList<>();
    private VBox produtosCadastradosVBox;
    private TextField quantityField;
    private ComboBox<String> unidadeComboBox;
    private ComboBox<String> lubrificantesComboBox;
    private TextField lubrificanteSearchField;
    private List<String> nomesLubrificantes = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cadastro de Estoque");

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label titleLabel = new Label("Cadastro de Produtos");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        Label quantityLabel = new Label("Quantidade:");
        quantityField = new TextField();
        quantityField.setPromptText("Digite a quantidade");

        Label unitLabel = new Label("Unidade:");
        unidadeComboBox = new ComboBox<>(FXCollections.observableArrayList("Litros", "Gramas"));
        unidadeComboBox.setValue("Litros");

        Label lubrificantesLabel = new Label("Lubrificantes:");
        lubrificantesComboBox = new ComboBox<>();
        lubrificanteSearchField = new TextField();
        lubrificanteSearchField.setPromptText("Pesquisar lubrificante");
        lubrificanteSearchField.textProperty().addListener((observable, oldValue, newValue) -> pesquisarLubrificantes(newValue));

        Button addButton = new Button("Adicionar Produto");
        addButton.setOnAction(e -> adicionarProduto());

        Button saveButton = new Button("Salvar Todos");
        saveButton.setOnAction(e -> salvarTodos());

        produtosCadastradosVBox = new VBox(5);
        produtosCadastradosVBox.setPadding(new Insets(10));
        produtosCadastradosVBox.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        grid.addRow(0, quantityLabel, quantityField);
        grid.addRow(1, unitLabel, unidadeComboBox);
        grid.addRow(2, lubrificantesLabel, lubrificanteSearchField, lubrificantesComboBox);

        vbox.getChildren().addAll(titleLabel, grid, addButton, saveButton, new Label("Produtos Cadastrados:"), produtosCadastradosVBox);
        borderPane.setCenter(vbox);

        Scene scene = new Scene(borderPane, 600, 400);  // Ajuste as dimensões conforme necessário
        primaryStage.setScene(scene);
        primaryStage.show();

        // Carregar lubrificantes do Excel
        carregarLubrificantesDoExcel();
    }

    private void pesquisarLubrificantes(String keyword) {
        List<String> lubrificantesFiltrados = nomesLubrificantes.stream()
                .filter(lubrificante -> lubrificante.toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        Collections.sort(lubrificantesFiltrados);
        lubrificantesComboBox.setItems(FXCollections.observableArrayList(lubrificantesFiltrados));
    }

    private void adicionarProduto() {
        String nome = lubrificantesComboBox.getValue();  // Use o valor do ComboBox diretamente como o nome do produto
        String quantidadeStr = quantityField.getText();
        String unidade = unidadeComboBox.getValue().substring(0, 1);
        String lubrificanteSelecionado = lubrificantesComboBox.getValue();

        if (lubrificanteSelecionado != null && isNumeric(quantidadeStr)) {
            double quantidade = Double.parseDouble(quantidadeStr);
            ItemEstoque novoItem = new ItemEstoque(nome, (int) quantidade, unidade);
            novoItem.setLubrificante(lubrificanteSelecionado);
            listaNovosProdutos.add(novoItem);
            limparCampos();
            atualizarProdutosCadastrados();
            System.out.println("Produto: " + nome + ", Quantidade: " + quantidade + " " + unidade + " adicionado à lista.");
        } else {
            System.out.println("Selecione um lubrificante válido e preencha uma quantidade válida.");
        }
    }

    private void salvarTodos() {
        if (!listaNovosProdutos.isEmpty()) {
            TelaEstoque telaEstoque = new TelaEstoque();
            for (ItemEstoque item : listaNovosProdutos) {
                telaEstoque.atualizarEstoque(Collections.singletonList(item));
            }
            listaNovosProdutos.clear();
            atualizarProdutosCadastrados();
            System.out.println("Todos os produtos foram cadastrados com sucesso!");
        } else {
            System.out.println("Nenhum produto para salvar.");
        }
    }

    private void limparCampos() {
        quantityField.clear();
        lubrificantesComboBox.getSelectionModel().clearSelection();
        lubrificanteSearchField.clear();
    }

    private void atualizarProdutosCadastrados() {
        produtosCadastradosVBox.getChildren().clear();
        for (ItemEstoque item : listaNovosProdutos) {
            Text text = new Text(item.getNomeProduto() + " - " + item.getQuantidade() + " " + item.getUnidade());
            if (item.getLubrificante() != null) {
                text.setText(text.getText() + " - Lubrificante: " + item.getLubrificante());
            }
            produtosCadastradosVBox.getChildren().add(text);
        }
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void carregarLubrificantesDoExcel() {
        try {
            Workbook workbook = WorkbookFactory.create(new File(FILE_PATH));
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                List<String> valoresCelula = new ArrayList<>();
                for (Cell cell : row) {
                    valoresCelula.add(obterValorCelula(cell));
                }
                String linhaCompleta = String.join(" - ", valoresCelula);
                nomesLubrificantes.add(linhaCompleta);
            }

            Collections.sort(nomesLubrificantes);
            lubrificantesComboBox.setItems(FXCollections.observableArrayList(nomesLubrificantes));

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String obterValorCelula(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }
}

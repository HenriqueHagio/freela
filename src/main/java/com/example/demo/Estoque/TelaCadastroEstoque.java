package com.example.demo.Estoque;

import com.example.demo.Cadastro.Constante.UnidadeMedida;
import com.example.demo.Hibernate.Entidade;
import com.example.demo.Hibernate.HibernateEntidade;
import com.example.demo.Lubrificantes.Lubrificante;
import com.example.demo.comeco.Empresa;
import com.example.demo.comeco.Usuario;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TelaCadastroEstoque extends Application {

    private List<ItemEstoque> listaNovosProdutos = new ArrayList<>();
    private VBox produtosCadastradosVBox;
    private TextField quantityField;
    private ComboBox<UnidadeMedida> unidadeComboBox;
    private ComboBox<String> lubrificantesComboBox;
    private TextField lubrificanteSearchField;

    Usuario usuario = new Usuario();

    Lubrificante lubrificante = new Lubrificante();

    List<Lubrificante> lubrificantesFiltrados = new ArrayList<>();
    Entidade<Object> dao = new HibernateEntidade<>();

    Empresa empresaAdmin = new Empresa();

    public TelaCadastroEstoque(Usuario usuario) {
        this.usuario = usuario;
    }



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
        unidadeComboBox = new ComboBox<>();
        unidadeComboBox.getItems().addAll(UnidadeMedida.values());
        unidadeComboBox.setValue(UnidadeMedida.GRAMAS);



        Label lubrificantesLabel = new Label("Lubrificantes:");
        lubrificantesComboBox = new ComboBox<>();
        lubrificanteSearchField = new TextField();
        lubrificanteSearchField.setPromptText("Pesquisar lubrificante");
        Button buscarButton = new Button("Buscar");
        buscarButton.setOnAction(event -> {
            String searchTerm = lubrificanteSearchField.getText();
            pesquisarLubrificantes(searchTerm);
            lubrificantesComboBox.getItems().clear();
            List<String> ls = new ArrayList<>();
            for(Lubrificante l : lubrificantesFiltrados){
                ls.add(l.getDescricao());
                lubrificantesComboBox.setItems(FXCollections.observableArrayList(ls));
            }

        });
        Label labelEmpresa = new Label("Selecione a empresa:");
        ComboBox<String> comboBoxEmpresa = new ComboBox<>();
        List<Empresa> empresas = new Empresa().recuperarTodos();
        List<String> ls = new ArrayList<>();
        for(Empresa l : empresas){
            ls.add(l.getNome());
            comboBoxEmpresa.setItems(FXCollections.observableArrayList(ls));
        }
        comboBoxEmpresa.getItems().addAll();

        comboBoxEmpresa.setOnAction(event -> {
            String selectedItem = comboBoxEmpresa.getSelectionModel().getSelectedItem();
            empresaAdmin = new Empresa().buscarPessoaPorNome(selectedItem);

        });

        Button addButton = new Button("Adicionar Produto");
        addButton.setOnAction(e -> adicionarProduto());

        Button saveButton = new Button("Salvar Todos");
        saveButton.setOnAction(e -> salvarTodos());

        Button importButton = new Button("Importar XML");
        importButton.setOnAction(e -> importarXML(primaryStage));

        produtosCadastradosVBox = new VBox(5);
        produtosCadastradosVBox.setPadding(new Insets(10));
        produtosCadastradosVBox.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        grid.addRow(0, quantityLabel, quantityField);
        grid.addRow(1, unitLabel, unidadeComboBox);
        grid.addRow(2, lubrificantesLabel, lubrificanteSearchField, lubrificantesComboBox, buscarButton);
        grid.addRow(3, labelEmpresa, comboBoxEmpresa);

        vbox.getChildren().addAll(titleLabel, grid, addButton, saveButton, importButton, new Label("Produtos Cadastrados:"), produtosCadastradosVBox);
        borderPane.setCenter(vbox);

        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void pesquisarLubrificantes(String keyword) {
        lubrificantesFiltrados = lubrificante.listarecuperarPorNome(keyword);
//        lubrificantesComboBox.setItems();
    }

    private void adicionarProduto() {
        String nome = lubrificantesComboBox.getValue();
        String quantidadeStr = quantityField.getText();

        Lubrificante lubrificanteSelecionado = lubrificante.recuperarPorNome(lubrificantesComboBox.getValue());
        Produto produto = new Produto();

        if (lubrificanteSelecionado != null && isNumeric(quantidadeStr)) {
            produto.setQuantidade(Double.parseDouble(quantidadeStr));
            produto.setLubrificante(lubrificanteSelecionado);
            produto.setUnidadeMedida(unidadeComboBox.getValue());
            if(!usuario.getRole().equals("admin"))
                produto.setEmpresa(usuario.getPessoa().getEmpresa());
            else produto.setEmpresa(empresaAdmin);
            ItemEstoque novoItem = new ItemEstoque(nome, produto.getQuantidade(), produto.getUnidadeMedida().getDescricao());
            novoItem.setLubrificante(lubrificanteSelecionado.getDescricao());
            dao.salvar(produto);
            listaNovosProdutos.add(novoItem);
            limparCampos();
            atualizarProdutosCadastrados();
            exibirMensagem("Produto adicionado", "Produto: " + nome + ", " +
                    "Quantidade: " + produto.getQuantidade() + " " + produto.getUnidadeMedida() + " adicionado à lista.");
        } else {
            exibirMensagemErro("Erro", "Selecione um lubrificante válido e preencha uma quantidade válida.");
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
            exibirMensagem("Produtos salvos", "Todos os produtos foram cadastrados com sucesso!");
        } else {
            exibirMensagem("Nenhum produto para salvar", "Nenhum produto para salvar.");
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
            Text text = new Text(item.getNomeProduto() + " - " + item.getQuantidadeProperty().getName() + " " + item.getUnidade());
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

    private void importarXML(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolher arquivo XML");
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            XMLReader xmlReader = new XMLReader();  // Criando uma instância de XMLReader
            List<ItemEstoque> produtosDoXML = xmlReader.lerArquivoXML(file.getPath());
            listaNovosProdutos.addAll(produtosDoXML);
            atualizarProdutosCadastrados();
            exibirMensagem("Produtos importados", "Produtos importados do XML com sucesso!");
        }
    }

    private void exibirMensagem(String titulo, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

    private void exibirMensagemErro(String titulo, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

}

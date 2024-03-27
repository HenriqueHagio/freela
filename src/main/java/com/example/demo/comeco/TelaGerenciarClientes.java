package com.example.demo.comeco;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class TelaGerenciarClientes extends Application {

    private TableView<Usuario> tabelaClientes;
    private ObservableList<Usuario> listaClientes;

    private TelaCriarNovoUsuario telaCriarNovoUsuario;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gerenciamento de Clientes");

        inicializarTabelaClientes();

        Button adicionarButton = new Button("Adicionar Cliente");
        adicionarButton.setOnAction(e -> telaCriarNovoUsuario.start(new Stage()));

        Button editarButton = new Button("Editar Cliente");
        editarButton.setOnAction(e -> editarCliente());

        Button excluirButton = new Button("Excluir Cliente");
        excluirButton.setOnAction(e -> excluirCliente());

        Button visualizarButton = new Button("Visualizar Informações");
        visualizarButton.setOnAction(e -> visualizarInformacoesCliente());

        HBox botoesLayout = new HBox(10);
        botoesLayout.getChildren().addAll(adicionarButton, editarButton, excluirButton, visualizarButton);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(tabelaClientes, botoesLayout);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Inicializa a lista de clientes
        ObservableList<Usuario> clientes = new Usuario().recuperarClientes();
        listaClientes =  clientes;


        tabelaClientes.setItems(listaClientes);

        // Inicializa a tela para criar novo usuário
        telaCriarNovoUsuario = new TelaCriarNovoUsuario(this);
    }

    private void inicializarTabelaClientes() {
        tabelaClientes = new TableView<>();

        // Coluna Empresa deve ser a primeira
        TableColumn<Usuario, String> colunaEmpresa = new TableColumn<>("Empresa");
        colunaEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));

        TableColumn<Usuario, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Usuario, String> colunaSobrenome = new TableColumn<>("Sobrenome");
        colunaSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));

        TableColumn<Usuario, String> colunaEmail = new TableColumn<>("E-mail");
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Usuario, String> colunaSenha = new TableColumn<>("Senha");
        colunaSenha.setCellValueFactory(new PropertyValueFactory<>("senha"));

        tabelaClientes.getColumns().addAll(colunaEmpresa, colunaNome, colunaSobrenome, colunaEmail, colunaSenha);
    }

    private void editarCliente() {
        Usuario clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();

        if (clienteSelecionado != null) {
            // Criação dos campos de edição
            TextField nomeField = new TextField(clienteSelecionado.getPessoa().getNome());
            TextField sobrenomeField = new TextField(clienteSelecionado.getPessoa().getNome());
            TextField emailField = new TextField(clienteSelecionado.getPessoa().getEmail());
            PasswordField senhaField = new PasswordField();
            senhaField.setPromptText("Nova Senha");
            TextField empresaField = new TextField(clienteSelecionado.getPessoa().getEmpresa().getNome());

            // Layout para os campos de edição
            VBox editarLayout = new VBox(10);
            editarLayout.getChildren().addAll(
                    new Label("Nome:"), nomeField,
                    new Label("Sobrenome:"), sobrenomeField,
                    new Label("E-mail:"), emailField,
                    new Label("Nova Senha:"), senhaField,
                    new Label("Empresa:"), empresaField
            );

            // Diálogo de edição
            Alert editarDialog = new Alert(Alert.AlertType.CONFIRMATION);
            editarDialog.setTitle("Editar Cliente");
            editarDialog.setHeaderText(null);
            editarDialog.getDialogPane().setContent(editarLayout);

            Optional<ButtonType> result = editarDialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Atualiza as informações do cliente
                clienteSelecionado.getPessoa().setNome(nomeField.getText());
                clienteSelecionado.getPessoa().setSobrenome(sobrenomeField.getText());
                clienteSelecionado.getPessoa().setEmail(emailField.getText());

                // Verifica se a nova senha foi fornecida e a atualiza
                if (!senhaField.getText().isEmpty()) {
                    clienteSelecionado.setPassword(senhaField.getText());
                }

//                clienteSelecionado.getPessoa().setEmpresa(empresaField.getText());

                // Atualiza a tabela
                atualizarTabela();
            }
        } else {
            // Nenhum cliente selecionado para edição
            alertaSelecaoCliente();
        }
    }

    private void excluirCliente() {
        Usuario clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();

        if (clienteSelecionado != null) {
            // Simula uma operação de exclusão (substitua com sua lógica real)
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Excluir Cliente");
            alert.setHeaderText(null);
            alert.setContentText("Deseja realmente excluir o cliente: " + clienteSelecionado.getPessoa().getNome() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                listaClientes.remove(clienteSelecionado);
                // Atualiza a tabela
                atualizarTabela();
            }
        } else {
            // Nenhum cliente selecionado para exclusão
            alertaSelecaoCliente();
        }
    }

    private void visualizarInformacoesCliente() {
        Usuario clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();

        if (clienteSelecionado != null) {
            // Exibe as informações do cliente em uma janela de diálogo
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informações do Cliente");
            alert.setHeaderText(null);
            alert.setContentText(clienteSelecionado.toString());
            alert.showAndWait();
        } else {
            // Nenhum cliente selecionado para visualização de informações
            alertaSelecaoCliente();
        }
    }

    private void alertaSelecaoCliente() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText("Selecione um cliente da tabela.");
        alert.showAndWait();
    }

    public void adicionarNovoCliente(Usuario novoCliente) {
        if (tabelaClientes == null) {
            inicializarTabelaClientes();
        }

        listaClientes.add(novoCliente);
        // Atualiza a tabela
        atualizarTabela();

        // Exibe as informações do novo cliente em uma janela de diálogo
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Novo Usuário Cadastrado");
        alert.setHeaderText(null);
        alert.setContentText(novoCliente.toString());
        alert.showAndWait();
    }

    private void atualizarTabela() {
        tabelaClientes.setItems(FXCollections.observableArrayList(listaClientes));
    }

//    public static class Usuario {
//        private String nome;
//        private String sobrenome;
//        private String email;
//        private String senha;
//        private String empresa;

//        public Usuario(String nome, String sobrenome, String email, String senha, String empresa) {
//            this.nome = nome;
//            this.sobrenome = sobrenome;
//            this.email = email;
//            this.senha = senha;
//            this.empresa = empresa;
//        }


}

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

    private TableView<Cliente> tabelaClientes;
    private ObservableList<Cliente> listaClientes;

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
        listaClientes = FXCollections.observableArrayList();
        listaClientes.addAll(
                new Cliente("John", "Doe", "john.doe@example.com", "password", "Company A"),
                new Cliente("Jane", "Doe", "jane.doe@example.com", "secret", "Company B")
        );

        tabelaClientes.setItems(listaClientes);

        // Inicializa a tela para criar novo usuário
        telaCriarNovoUsuario = new TelaCriarNovoUsuario(this);
    }

    private void inicializarTabelaClientes() {
        tabelaClientes = new TableView<>();

        // Coluna Empresa deve ser a primeira
        TableColumn<Cliente, String> colunaEmpresa = new TableColumn<>("Empresa");
        colunaEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));

        TableColumn<Cliente, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Cliente, String> colunaSobrenome = new TableColumn<>("Sobrenome");
        colunaSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));

        TableColumn<Cliente, String> colunaEmail = new TableColumn<>("E-mail");
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Cliente, String> colunaSenha = new TableColumn<>("Senha");
        colunaSenha.setCellValueFactory(new PropertyValueFactory<>("senha"));

        tabelaClientes.getColumns().addAll(colunaEmpresa, colunaNome, colunaSobrenome, colunaEmail, colunaSenha);
    }

    private void editarCliente() {
        Cliente clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();

        if (clienteSelecionado != null) {
            // Criação dos campos de edição
            TextField nomeField = new TextField(clienteSelecionado.getNome());
            TextField sobrenomeField = new TextField(clienteSelecionado.getSobrenome());
            TextField emailField = new TextField(clienteSelecionado.getEmail());
            PasswordField senhaField = new PasswordField();
            senhaField.setPromptText("Nova Senha");
            TextField empresaField = new TextField(clienteSelecionado.getEmpresa());

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
                clienteSelecionado.setNome(nomeField.getText());
                clienteSelecionado.setSobrenome(sobrenomeField.getText());
                clienteSelecionado.setEmail(emailField.getText());

                // Verifica se a nova senha foi fornecida e a atualiza
                if (!senhaField.getText().isEmpty()) {
                    clienteSelecionado.setSenha(senhaField.getText());
                }

                clienteSelecionado.setEmpresa(empresaField.getText());

                // Atualiza a tabela
                atualizarTabela();
            }
        } else {
            // Nenhum cliente selecionado para edição
            alertaSelecaoCliente();
        }
    }

    private void excluirCliente() {
        Cliente clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();

        if (clienteSelecionado != null) {
            // Simula uma operação de exclusão (substitua com sua lógica real)
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Excluir Cliente");
            alert.setHeaderText(null);
            alert.setContentText("Deseja realmente excluir o cliente: " + clienteSelecionado.getNome() + "?");

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
        Cliente clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();

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

    public void adicionarNovoCliente(Cliente novoCliente) {
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

    public static class Cliente {
        private String nome;
        private String sobrenome;
        private String email;
        private String senha;
        private String empresa;

        public Cliente(String nome, String sobrenome, String email, String senha, String empresa) {
            this.nome = nome;
            this.sobrenome = sobrenome;
            this.email = email;
            this.senha = senha;
            this.empresa = empresa;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getSobrenome() {
            return sobrenome;
        }

        public void setSobrenome(String sobrenome) {
            this.sobrenome = sobrenome;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }

        public String getEmpresa() {
            return empresa;
        }

        public void setEmpresa(String empresa) {
            this.empresa = empresa;
        }

        @Override
        public String toString() {
            return "Nome: " + nome +
                    "\nSobrenome: " + sobrenome +
                    "\nEmpresa: " + empresa +
                    "\nE-mail: " + email +
                    "\nSenha: " + senha;
        }
    }
}

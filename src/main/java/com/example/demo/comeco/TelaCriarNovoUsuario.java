package com.example.demo.comeco;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class TelaCriarNovoUsuario {

    private TelaGerenciarClientes telaGerenciarClientes;

    public TelaCriarNovoUsuario(TelaGerenciarClientes telaGerenciarClientes) {
        this.telaGerenciarClientes = telaGerenciarClientes;
    }

    public TelaCriarNovoUsuario() {
        // Remova a inicialização desnecessária da telaGerenciarClientes aqui
    }

    public void start(Stage stage) {
        stage.setTitle("Criar Novo Usuário");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label labelNome = new Label("Nome:");
        TextField campoNome = new TextField();

        Label labelSobrenome = new Label("Sobrenome:");
        TextField campoSobrenome = new TextField();

        Label labelUsername = new Label("Username:");
        TextField campoUsername = new TextField();

        Label labelEmail = new Label("E-mail:");
        TextField campoEmail = new TextField();

        // Solicita o nome da empresa antes da senha
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Cadastro de Usuário");
        dialog.setHeaderText(null);
        dialog.setContentText("Digite o nome da empresa:");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String empresa = result.get();

            Label labelSenha = new Label("Senha:");
            PasswordField campoSenha = new PasswordField();

            Label labelConfirmarSenha = new Label("Confirmar Senha:");
            PasswordField campoConfirmarSenha = new PasswordField();

            Button cadastrarButton = new Button("Cadastrar");
            cadastrarButton.setOnAction(event -> cadastrarNovoUsuario(
                    campoNome.getText(),
                    campoSobrenome.getText(),
                    campoUsername.getText(),
                    campoEmail.getText(),
                    campoSenha.getText(),
                    campoConfirmarSenha.getText(),
                    empresa,
                    stage
            ));

            layout.getChildren().addAll(labelNome, campoNome, labelSobrenome, campoSobrenome, labelUsername, campoUsername,
                    labelEmail, campoEmail, labelSenha, campoSenha, labelConfirmarSenha, campoConfirmarSenha, cadastrarButton);

            Scene scene = new Scene(layout, 400, 300);
            stage.setScene(scene);
            stage.show();
        }
    }

    private void cadastrarNovoUsuario(String nome, String sobrenome, String username, String email, String senha, String confirmarSenha, String empresa, Stage stage) {
        if (senha.equals(confirmarSenha)) {
            if (telaGerenciarClientes != null) {
                TelaGerenciarClientes.Cliente novoCliente = new TelaGerenciarClientes.Cliente(nome, sobrenome, email, senha, empresa);
                telaGerenciarClientes.adicionarNovoCliente(novoCliente);  // Adiciona o novo cliente à lista e atualiza a tabela
                stage.close();
            } else {
                System.out.println("Erro: TelaGerenciarClientes não foi inicializada corretamente.");
            }
        } else {
            System.out.println("Erro: As senhas não coincidem. Tente novamente.");
        }
    }
}
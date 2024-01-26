package com.example.demo.comeco;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TelaModificarSenhaUsuario extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Modificar Senha de Usuário");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label labelSenhaAtual = new Label("Senha Atual:");
        PasswordField campoSenhaAtual = new PasswordField();

        Label labelNovaSenha = new Label("Nova Senha:");
        PasswordField campoNovaSenha = new PasswordField();

        Label labelConfirmarSenha = new Label("Confirmar Nova Senha:");
        PasswordField campoConfirmarSenha = new PasswordField();

        Button modificarButton = new Button("Modificar Senha");
        modificarButton.setOnAction(event -> modificarSenhaUsuario(
                campoSenhaAtual.getText(),
                campoNovaSenha.getText(),
                campoConfirmarSenha.getText(),
                primaryStage
        ));

        layout.getChildren().addAll(labelSenhaAtual, campoSenhaAtual, labelNovaSenha, campoNovaSenha, labelConfirmarSenha, campoConfirmarSenha, modificarButton);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void modificarSenhaUsuario(String senhaAtual, String novaSenha, String confirmarSenha, Stage stage) {
        // Lógica para modificar a senha do usuário
        // Substitua isso pela sua lógica de modificação de senha

        System.out.println("Senha Atual: " + senhaAtual);
        System.out.println("Nova Senha: " + novaSenha);
        System.out.println("Confirmar Senha: " + confirmarSenha);

        // Feche a janela após a modificação da senha
        stage.close();
    }
}

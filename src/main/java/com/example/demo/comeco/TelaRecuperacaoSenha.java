package com.example.demo.comeco;

import com.fasterxml.jackson.core.JsonGenerator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class TelaRecuperacaoSenha extends Application {

    private JsonGenerator primaryStage;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Recuperação de Senha");
        primaryStage.setScene(createScene());
        primaryStage.show();
    }

    private Scene createScene() {
        VBox recoveryLayout = new VBox(20);
        recoveryLayout.setAlignment(Pos.CENTER);
        recoveryLayout.setPadding(new Insets(20));
        recoveryLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Label titleLabel = new Label("Esqueceu a Senha?");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label instructionsLabel = new Label("Digite seu e-mail para recuperar a senha:");
        instructionsLabel.setStyle("-fx-font-size: 16px;");

        TextField emailField = new TextField();
        emailField.setPromptText("Digite seu e-mail");
        emailField.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-radius: 5px; -fx-border-radius: 5px;");

        Button recoverButton = new Button("Recuperar Senha");
        recoverButton.setStyle("--fx-background-color: #4CAF50; -fx-text-fill: #070000; -fx-padding: 10px;");
        recoverButton.setOnAction(event -> {
            String email = emailField.getText();
            if (isValidEmail(email)) {
                // Implemente a lógica de recuperação de senha aqui
                // Envie um e-mail de recuperação, redefina a senha, etc.
                showSuccessMessage("Um e-mail de recuperação foi enviado para: " + email);
                try {
                    primaryStage.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                showErrorMessage("Por favor, insira um e-mail válido.");
            }
        });

        recoveryLayout.getChildren().addAll(titleLabel, instructionsLabel, emailField, recoverButton);

        return new Scene(recoveryLayout, 400, 250);
    }

    private boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}");
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

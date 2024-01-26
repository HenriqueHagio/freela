package com.example.demo.Cadastro;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class TelaPontosCadastrados extends Application {

    private List<String> pontosCadastrados;

    public TelaPontosCadastrados(List<String> pontosCadastrados) {
        this.pontosCadastrados = pontosCadastrados;
    }

    public TelaPontosCadastrados() {
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pontos Cadastrados");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20px;"); // Estilo de fundo e espaçamento interno

        Label labelTitulo = new Label("Pontos Cadastrados");
        labelTitulo.setStyle("-fx-font-size: 24px; -fx-text-fill: #070000; -fx-font-weight: bold;"); // Estilo do título
        layout.getChildren().add(labelTitulo);

        if (pontosCadastrados != null && !pontosCadastrados.isEmpty()) {
            for (String ponto : pontosCadastrados) {
                Button btnPonto = new Button(ponto);
                btnPonto.setStyle("-fx-background-color: #e73737; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;"); // Estilo do botão
                btnPonto.setOnAction(event -> exibirDetalhesPonto(ponto));
                layout.getChildren().add(btnPonto);
            }
        } else {
            Label labelVazia = new Label("Nenhum ponto cadastrado.");
            labelVazia.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;"); // Estilo do texto
            layout.getChildren().add(labelVazia);
        }

        Button voltarButton = new Button("Voltar");
        voltarButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;"); // Estilo do botão Voltar
        voltarButton.setOnAction(event -> primaryStage.close());
        layout.getChildren().add(voltarButton);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void exibirDetalhesPonto(String ponto) {
        Stage detalhesStage = new Stage();
        detalhesStage.setTitle("Detalhes do Ponto");

        VBox detalhesLayout = new VBox(10);
        detalhesLayout.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20px;"); // Estilo de fundo e espaçamento interno

        Label labelPonto = new Label("Detalhes do Ponto: " + ponto);
        labelPonto.setStyle("-fx-font-size: 18px; -fx-text-fill: #008CBA; -fx-font-weight: bold;"); // Estilo do título dos detalhes
        detalhesLayout.getChildren().add(labelPonto);

        Scene detalhesScene = new Scene(detalhesLayout, 300, 200);
        detalhesStage.setScene(detalhesScene);
        detalhesStage.show();
    }

    public static void main(String[] args) {
        List<String> pontos = List.of("Ponto A", "Ponto B", "Ponto C", "Ponto D", "Ponto E");
        launch(new TelaPontosCadastrados(pontos), args);
    }

    private static void launch(TelaPontosCadastrados telaPontosCadastrados, String[] args) {
    }
}

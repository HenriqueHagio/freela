package com.example.demo.Cadastro;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20px;"); // Estilo de fundo e espaçamento interno

        // Adicionando um ImageView para a logo no canto superior direito
        ImageView logoImageView = new ImageView(new Image("C:\\Users\\Lucas\\OneDrive\\Documentos\\Codigos Lubvel\\demo (2)\\src\\main\\java\\com\\example\\demo\\Principal\\img.png"));
        logoImageView.setFitHeight(50);  // Ajuste a altura conforme necessário
        logoImageView.setPreserveRatio(true);

        // Criando um HBox para organizar a imagem no canto superior direito
        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER_RIGHT);
        logoBox.getChildren().add(logoImageView);
        layout.setTop(logoBox);

        Label labelTitulo = new Label("Pontos Cadastrados");
        labelTitulo.setStyle("-fx-font-size: 24px; -fx-text-fill: #070000; -fx-font-weight: bold;"); // Estilo do título
        layout.setLeft(labelTitulo);

        VBox pontosVBox = new VBox(10);
        pontosVBox.setAlignment(Pos.CENTER);

        if (pontosCadastrados != null && !pontosCadastrados.isEmpty()) {
            for (String ponto : pontosCadastrados) {
                Button btnPonto = new Button(ponto);
                btnPonto.setStyle("-fx-background-color: #e73737; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;"); // Estilo do botão
                btnPonto.setOnAction(event -> exibirDetalhesPonto(ponto));
                pontosVBox.getChildren().add(btnPonto);
            }
        } else {
            Label labelVazia = new Label("Nenhum ponto cadastrado.");
            labelVazia.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;"); // Estilo do texto
            pontosVBox.getChildren().add(labelVazia);
        }

        layout.setCenter(pontosVBox);

        Button voltarButton = new Button("Voltar");
        voltarButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;"); // Estilo do botão Voltar
        voltarButton.setOnAction(event -> primaryStage.close());

        HBox bottomBox = new HBox(10);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.getChildren().add(voltarButton);
        layout.setBottom(bottomBox);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void exibirDetalhesPonto(String ponto) {
        // Implemente a lógica para exibir os detalhes do ponto, se necessário.
    }

    public static void main(String[] args) {
        List<String> pontos = List.of("PontoLubrificacao A", "PontoLubrificacao B", "PontoLubrificacao C", "PontoLubrificacao D", "PontoLubrificacao E");
        launch(new TelaPontosCadastrados(pontos), args);
    }

    private static void launch(TelaPontosCadastrados telaPontosCadastrados, String[] args) {
    }
}

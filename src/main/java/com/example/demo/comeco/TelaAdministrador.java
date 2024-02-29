package com.example.demo.comeco;

import com.example.demo.Cadastro.TelaCadastramento;
import com.example.demo.Cadastro.TelaPontosCadastrados;
import com.example.demo.Estoque.TelaCadastroEstoque;
import com.example.demo.Estoque.TelaEstoque;
import com.example.demo.Lubrificantes.TelaLubrificantes;
import javafx.application.Application;
import javafx.geometry.Insets;
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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

public class TelaAdministrador extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setupUI(primaryStage);
    }

    private void setupUI(Stage primaryStage) {
        primaryStage.setTitle("Painel do Administrador");

        BorderPane adminLayout = new BorderPane();
        adminLayout.setPadding(new Insets(20));

        // Adicionando um ImageView para um ícone ou logotipo
        ImageView logoImageView = new ImageView(new Image("C:\\Users\\Lucas\\OneDrive\\Documentos\\Codigos Lubvel\\demo (2)\\src\\main\\java\\com\\example\\demo\\Principal\\img.png"));
        logoImageView.setFitHeight(50);  // Ajuste a altura conforme necessário
        logoImageView.setPreserveRatio(true);

        // Criando um HBox para organizar a imagem no topo
        HBox logoBox = new HBox();
        logoBox.setAlignment(Pos.TOP_RIGHT);  // Alinhando a imagem no canto superior esquerdo
        logoBox.getChildren().add(logoImageView);

        Label adminLabel = new Label("Bem-vindo ao Painel do Administrador");
        adminLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        BorderPane.setAlignment(adminLabel, Pos.TOP_CENTER);
        adminLayout.setTop(adminLabel);

        VBox menuVBox = new VBox(10);
        menuVBox.setAlignment(Pos.CENTER);

        Button gerenciarClientesButton = createStyledButton("Gerenciar Clientes", this::handleManageClients);
        Button cadastrarPontosButton = createStyledButton("Cadastramento de Pontos", this::abrirTelaCadastramento);
        Button pontosButton = createStyledButton("Pontos", this::abrirTelaPontos);
        Button lubrificantesButton = createStyledButton("Lubrificantes", this::abrirTelaLubrificantes);
        Button estoqueButton = createStyledButton("Estoque", this::abrirTelaEstoque);
        Button cadastroEstoqueButton = createStyledButton("Cadastro de Estoque", () -> {
            try {
                abrirTelaCadastroEstoque();
            } catch (IOException | InvalidFormatException e) {
                throw new RuntimeException(e);
            }
        });

        menuVBox.getChildren().addAll(
                gerenciarClientesButton,
                cadastrarPontosButton,
                pontosButton,
                lubrificantesButton,
                estoqueButton,
                cadastroEstoqueButton
        );

        adminLayout.setCenter(menuVBox);

        Scene adminScene = new Scene(adminLayout, 600, 400);
        primaryStage.setScene(adminScene);
        primaryStage.show();
    }

    private void handleManageClients() {
        TelaGerenciarClientes telaGerenciarClientes = new TelaGerenciarClientes();
        Stage gerenciarClientesStage = new Stage();
        telaGerenciarClientes.start(gerenciarClientesStage);
    }

    private void abrirTelaCadastramento() {
        TelaCadastramento telaCadastramento = new TelaCadastramento(stage -> primaryStage.show());
        Stage cadastroPontosStage = new Stage();
        telaCadastramento.start(cadastroPontosStage);
    }

    private void abrirTelaPontos() {
        TelaPontosCadastrados telaPontos = new TelaPontosCadastrados();
        Stage pontosStage = new Stage();
        telaPontos.start(pontosStage);
    }

    private void abrirTelaLubrificantes() {
        Stage lubrificantesStage = new Stage();
        // Substitua o caminho do arquivo conforme necessário
        String filePath = "C:\\Users\\Lucas\\OneDrive\\Documentos\\BD_PRODUTOS_LUBVEL.xlsx";
        TelaLubrificantes telaLubrificantes = new TelaLubrificantes(lubrificantesStage, filePath);


        telaLubrificantes.start();
    }

    private void abrirTelaEstoque() {
        TelaEstoque telaEstoque = new TelaEstoque();
        Stage estoqueStage = new Stage();
        telaEstoque.start(estoqueStage);
    }

    private void abrirTelaCadastroEstoque() throws IOException, InvalidFormatException {
        TelaCadastroEstoque telaCadastroEstoque = new TelaCadastroEstoque();
        Stage cadastroEstoqueStage = new Stage();
        telaCadastroEstoque.start(cadastroEstoqueStage);
    }

    private Button createStyledButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setMinWidth(200);
        button.getStyleClass().addAll("custom-button");
        button.setOnAction(event -> action.run());
        return button;
    }
}

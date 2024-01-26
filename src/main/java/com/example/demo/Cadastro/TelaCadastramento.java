package com.example.demo.Cadastro;

import com.example.demo.Lubrificantes.Lubrificante;
import com.example.demo.Lubrificantes.LubrificanteSelecionadoListener;
import com.example.demo.Lubrificantes.TelaPesquisaLubrificantes;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
public class TelaCadastramento extends Application implements LubrificanteSelecionadoListener {
    private VBox layout;
    private String setor;
    private String equipamento;
    private List<String> pontosLubrificacao = new ArrayList<>();
    private List<TextField> inputsLubrificante = new ArrayList<>();
    private List<TextField> inputsTagPonto = new ArrayList<>();
    private List<TextField> inputsQuantidadeLubrificante = new ArrayList<>();
    private List<DatePicker> inputsDataLubrificacao = new ArrayList<>();
    private List<TextField> inputsObservacoes = new ArrayList<>();
    private TextField campoCodigo;
    private TextField campoDescricao;
    private Lubrificante lubrificante;
    private Consumer<Stage> onCadastroConcluido;
    private List<LocalDateTime> datasProximaTrocaLubrificante = new ArrayList<>();
    private List<LocalDateTime> datasLubrificacao = new ArrayList<>();
    private static final int INTERVALO_LUBRIFICACAO_DIAS = 30;

    public TelaCadastramento(Consumer<Stage> onCadastroConcluido) {
        this.onCadastroConcluido = onCadastroConcluido;
    }

    public TelaCadastramento() {
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cadastro de Pontos");

        // Adicionando um ImageView para um ícone ou logotipo
        ImageView logoImageView = new ImageView(new Image("C:\\Users\\Lucas\\OneDrive\\Documentos\\Codigos Lubvel\\demo (2)\\src\\main\\java\\com\\example\\demo\\Principal\\img.png"));
        logoImageView.setFitHeight(50);  // Ajuste a altura conforme necessário
        logoImageView.setPreserveRatio(true);

        // Criando um HBox para organizar a imagem no topo
        HBox logoBox = new HBox();
        logoBox.setAlignment(Pos.TOP_RIGHT);  // Alinhando a imagem no canto superior esquerdo
        logoBox.getChildren().add(logoImageView);

        Label labelSetor = new Label("Setor:");
        labelSetor.setStyle("-fx-font-size: 18;");  // Ajuste o tamanho conforme necessário
        TextField inputSetor = new TextField();

        Label labelEquipamento = new Label("Equipamento:");
        labelEquipamento.setStyle("-fx-font-size: 17;");  // Ajuste o tamanho conforme necessário
        TextField inputEquipamento = new TextField();

        Label labelQuantidadePontos = new Label("Quantidade de Pontos:");
        labelQuantidadePontos.setStyle("-fx-font-size: 16;");  // Ajuste o tamanho conforme necessário
        TextField inputQuantidadePontos = new TextField();

        campoCodigo = new TextField();
        campoDescricao = new TextField();

        Button confirmarButton = new Button("Confirmar");
        confirmarButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        confirmarButton.setOnAction(event -> processarConfirmacao(primaryStage, inputSetor.getText(), inputEquipamento.getText(), inputQuantidadePontos.getText()));

        layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #f2f2f2;");
        layout.getChildren().addAll(
                logoBox,  // Adiciona o HBox com a imagem
                labelSetor, inputSetor,
                labelEquipamento, inputEquipamento,
                labelQuantidadePontos, inputQuantidadePontos,
                confirmarButton
        );

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void processarConfirmacao(Stage primaryStage, String setor, String equipamento, String quantidadePontosTexto) {
        if (validarCampos(setor, equipamento, quantidadePontosTexto)) {
            int quantidadePontos = Integer.parseInt(quantidadePontosTexto);
            this.setor = setor;
            this.equipamento = equipamento;
            configurarTelaCadastroPontos(primaryStage, quantidadePontos);
        }
    }

    private boolean validarCampos(String setor, String equipamento, String quantidadePontosTexto) {
        if (setor.isEmpty() || equipamento.isEmpty() || quantidadePontosTexto.isEmpty()) {
            exibirAlerta("Preencha todos os campos antes de prosseguir.");
            return false;
        }

        try {
            int quantidadePontos = Integer.parseInt(quantidadePontosTexto);
            if (quantidadePontos <= 0) {
                exibirAlerta("A quantidade de pontos deve ser um número positivo.");
                return false;
            }
        } catch (NumberFormatException e) {
            exibirAlerta("Insira um número válido para a quantidade de pontos.");
            return false;
        }

        return true;
    }

    private void configurarTelaCadastroPontos(Stage primaryStage, int quantidadePontos) {
        layout.getChildren().clear();
        datasProximaTrocaLubrificante.clear();

        Label setorLabel = new Label("Setor: " + setor);
        Label equipamentoLabel = new Label("Equipamento: " + equipamento);
        setorLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        equipamentoLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Adicionando um ImageView para um ícone ou logotipo
        ImageView logoImageView = new ImageView(new Image("C:\\Users\\Lucas\\OneDrive\\Documentos\\Codigos Lubvel\\demo (2)\\src\\main\\java\\com\\example\\demo\\Principal\\img.png"));
        logoImageView.setFitHeight(50);  // Ajuste a altura conforme necessário
        logoImageView.setPreserveRatio(true);

        // Criando um HBox para organizar a imagem no topo
        HBox logoBox = new HBox();
        logoBox.setAlignment(Pos.CENTER);  // Alinhando a imagem no canto superior direito
        logoBox.getChildren().add(logoImageView);

        VBox layoutCadastroPontos = new VBox(10);
        layoutCadastroPontos.setStyle("-fx-background-color: #ffffff; -fx-padding: 10px; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
        layoutCadastroPontos.getChildren().addAll(setorLabel, equipamentoLabel);

        GridPane gridPontos = new GridPane();
        gridPontos.setHgap(15); // Espaçamento horizontal
        gridPontos.setVgap(25); // Espaçamento vertical

        int coluna = 0;
        int linha = 0;

        int numeroColunas = 6;

        for (int i = 1; i <= quantidadePontos; i++) {
            datasLubrificacao.add(LocalDateTime.now());
            VBox pontoLayout = criarLayoutPonto(i);

            gridPontos.add(pontoLayout, coluna, linha);
            coluna++;

            // Verifica se a coluna atual excede o número desejado de colunas na tela
            if (coluna == numeroColunas) {
                coluna = 0; // Reinicia a contagem de colunas
                linha++;    // Move para a próxima linha
            }
        }

        Button cadastrarButton = new Button("Cadastrar");
        cadastrarButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        cadastrarButton.setOnAction(event -> processarCadastro(primaryStage));

        layoutCadastroPontos.getChildren().add(gridPontos);
        layoutCadastroPontos.getChildren().add(cadastrarButton);
        layout.getChildren().add(layoutCadastroPontos);
    }



    private VBox criarLayoutPonto(int pontoNumero) {
        Label labelSetor = new Label("Setor: " + setor);
        Label labelEquipamento = new Label("Equipamento: " + equipamento);
        labelSetor.setStyle("-fx-font-size: 15;");
        labelEquipamento.setStyle("-fx-font-size: 15;");

        Label labelPonto = new Label("Ponto " + pontoNumero + ":");
        labelPonto.setStyle("-fx-font-size: 17; -fx-font-weight: bold;");

        TextField inputPonto = new TextField();

        Label labelTagPonto = new Label("Tag do Ponto:");
        TextField inputTagPonto = new TextField();
        inputsTagPonto.add(inputTagPonto);

        Label labelLubrificante = new Label("Lubrificante Total:");
        TextField inputLubrificante = new TextField();
        inputLubrificante.setEditable(false);
        inputsLubrificante.add(inputLubrificante);

        Label labelQuantidade = new Label("Quantidade:");
        TextField inputQuantidade = new TextField();
        inputsQuantidadeLubrificante.add(inputQuantidade);

        Label labelDataHoraLubrificacao = new Label("Data e Hora Lubrificação:");
        DatePicker datePicker = new DatePicker();
        inputsDataLubrificacao.add(datePicker);

        Label labelObservacoes = new Label("Observações:");
        TextField inputObservacoes = new TextField();
        inputObservacoes.setPromptText("Adicione observações, se necessário");
        inputsObservacoes.add(inputObservacoes);

        ComboBox<String> comboBoxUnidade = new ComboBox<>();
        comboBoxUnidade.getItems().addAll("Gramas", "Litros");
        comboBoxUnidade.setValue("Gramas");

        Button selecionarLubrificanteButton = new Button("Selecionar Lubrificante");
        selecionarLubrificanteButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
        selecionarLubrificanteButton.setOnAction(event -> processarSelecaoLubrificante(pontoNumero));

        VBox pontoLayout = new VBox(10);
        pontoLayout.setStyle("-fx-background-color: #f9f9f9; -fx-padding: 10px; -fx-border-color: #000000; -fx-border-width: 1px; -fx-border-radius: 5px;");
        pontoLayout.getChildren().addAll(
                labelPonto, inputPonto,
                labelSetor, labelEquipamento,  // Adicionando os rótulos de Setor e Equipamento
                labelTagPonto, inputTagPonto,
                labelLubrificante, inputLubrificante,
                labelQuantidade, new HBox(5, inputQuantidade, comboBoxUnidade),
                labelDataHoraLubrificacao, datePicker,
                labelObservacoes, inputObservacoes,
                selecionarLubrificanteButton
        );

        return pontoLayout;
    }


    private void processarCadastro(Stage primaryStage) {
        if (pontosLubrificacao.isEmpty()) {
            exibirAlerta("Insira pelo menos um ponto de lubrificação.");
        } else {
            for (int i = 0; i < pontosLubrificacao.size(); i++) {
                String pontoString = pontosLubrificacao.get(i);
                LocalDateTime dataLubrificacao = null;

                if (inputsDataLubrificacao.get(i) instanceof DatePicker) {
                    DatePicker datePicker = (DatePicker) inputsDataLubrificacao.get(i);
                    dataLubrificacao = datePicker.getValue().atStartOfDay();
                }

                // Recuperando observações do campo correspondente
                String observacoes = inputsObservacoes.get(i).getText();

                if (lubrificante != null) { // Adicione essa verificação
                    try {
                        LocalDateTime dataProximaTroca = dataLubrificacao.plusDays(INTERVALO_LUBRIFICACAO_DIAS);
                        datasProximaTrocaLubrificante.add(dataProximaTroca);
                    } catch (Exception e) {
                        exibirAlerta("Insira uma data válida para lubrificação.");
                        return;
                    }

                    // Adicionando observações ao ponto de lubrificação
                    pontosLubrificacao.add("Setor: " + setor + ", Equipamento: " + equipamento + ", Ponto: " + inputsLubrificante.get(i).getText() +
                            ", Tag: " + inputsTagPonto.get(i).getText() +
                            ", Lubrificante: " + lubrificante.getCodigo() +
                            ", Quantidade: 1 " + "Gramas" +
                            ", Data e Hora Lubrificação: " + inputsDataLubrificacao.get(i).getValue() +
                            ", Observações: " + observacoes);
                } else {
                    exibirAlerta("Selecione um lubrificante antes de cadastrar.");
                }

            }
            mostrarMensagemCadastro();
            adicionarBotaoVoltar(primaryStage);
            verificarProximidadeTrocaLubrificante();
        }
    }

    private void adicionarBotaoVoltar(Stage primaryStage) {
        Button voltarButton = new Button("Voltar");
        voltarButton.setStyle("-fx-background-color: #757575; -fx-text-fill: white;");
        voltarButton.setOnAction(event -> {
            if (onCadastroConcluido != null) {
                onCadastroConcluido.accept(primaryStage);
            }
            datasLubrificacao.clear();
        });

        layout.getChildren().add(voltarButton);
    }

    private void processarSelecaoLubrificante(int pontoNumero) {
        int index = pontoNumero - 1;
        TelaPesquisaLubrificantes telaPesquisa = new TelaPesquisaLubrificantes(
                this,
                inputsLubrificante.get(index)
        );
        Stage stagePesquisa = new Stage();
        telaPesquisa.setLubrificanteSelecionadoListener(lubrificanteSelecionado -> {
            inputsLubrificante.get(index).setText(lubrificanteSelecionado.getDescricao());
            inputsQuantidadeLubrificante.get(index).setText("1");
            pontosLubrificacao.add("Setor: " + setor + ", Equipamento: " + equipamento + ", Ponto: " + inputsLubrificante.get(index).getText() +
                    ", Tag: " + inputsTagPonto.get(index).getText() +
                    ", Lubrificante: " + lubrificanteSelecionado.getCodigo() +
                    ", Quantidade: 1 " + "Gramas" +
                    ", Data e Hora Lubrificação: " + inputsDataLubrificacao.get(index).getValue() +
                    ", Observações: " + inputsObservacoes.get(index).getText());
        });
        telaPesquisa.start(stagePesquisa);
    }

    private void mostrarMensagemCadastro() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Pontos cadastrados com sucesso!");
        alert.show();
    }

    private void exibirAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.show();
    }

    private void verificarProximidadeTrocaLubrificante() {
        LocalDateTime dataAtual = LocalDateTime.now();
        Notifications notificationBuilder;
        notificationBuilder = Notifications.create()
                .title("Proximidade da Troca de Lubrificante")
                .text("Está próximo da hora de trocar o lubrificante em algum ponto.")
                .hideAfter(Duration.seconds(36000 / 60));

        for (int i = 0; i < datasProximaTrocaLubrificante.size(); i++) {
            LocalDateTime dataProximaTroca = datasProximaTrocaLubrificante.get(i);
            long minutosDiferenca = ChronoUnit.MINUTES.between(dataAtual, dataProximaTroca);

            if (minutosDiferenca > 0 && minutosDiferenca <= 30) {
                notificationBuilder.showWarning();
                break;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void onLubrificanteSelecionado(Lubrificante lubrificante) {
        this.lubrificante = lubrificante;
        campoCodigo.setText(lubrificante.getCodigo());
        campoDescricao.setText(lubrificante.getDescricao());
    }
}
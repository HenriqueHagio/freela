package com.example.demo.Cadastro;

import com.example.demo.Cadastro.Constante.UnidadeMedida;
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
import lombok.Getter;
import lombok.Setter;
import org.controlsfx.control.Notifications;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TelaCadastramento extends Application implements LubrificanteSelecionadoListener {
    PontoLubrificacao pontoLubrificacao = new PontoLubrificacao();
    private VBox layout;
    private final List<PontoLubrificacao> pontosLubrificacao = new ArrayList<>();
    private final List<Lubrificante> lubrificantes = new ArrayList<>();
    private final List<TextField> inputsQuantidadeLubrificante = new ArrayList<>();
    private TextField campoCodigo;
    private TextField campoDescricao;
    private Lubrificante lubrificante = new Lubrificante();
    private Consumer<Stage> onCadastroConcluido;
    private final List<LocalDateTime> datasProximaTrocaLubrificante = new ArrayList<>();
    private final List<LocalDateTime> datasLubrificacao = new ArrayList<>();
    private static final int INTERVALO_LUBRIFICACAO_DIAS = 30;

    private VBox pontoLayout;


    public TelaCadastramento(Consumer<Stage> onCadastroConcluido) {
        this.onCadastroConcluido = onCadastroConcluido;
    }

    public TelaCadastramento() {
    }
    @Getter @Setter
    public class PontoObj{
        private String setor;
        private String equipamento;
        private String tagPonto;
        private String lubrificante;
        private int quantidadeLub;
        private UnidadeMedida unidadeMedida;
        private LocalDate dataLub;
        private LocalDate dataproxLub;
        private int quantidadeGraxa;
        private String componentesEquip;
        private String operacao;
        private String obs;
    }





    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cadastro de Pontos");

        // Adicionando um ImageView para um ícone ou logotipo
        ImageView logoImageView = new ImageView(new Image("D:\\freela\\src\\main\\java\\com\\example\\demo\\Principal\\img.png"));
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

            for(int i = 1; i <= Integer.parseInt(quantidadePontosTexto); i++){
                pontoLubrificacao.setSetor(setor);
                pontoLubrificacao.setEquipamento(equipamento);
                pontosLubrificacao.add(pontoLubrificacao);
            }
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

        Label setorLabel = new Label("Setor: " + pontoLubrificacao.getSetor());
        Label equipamentoLabel = new Label("Equipamento: " + pontoLubrificacao.getEquipamento());
        setorLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        equipamentoLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Adicionando um ImageView para um ícone ou logotipo
        ImageView logoImageView = new ImageView(new Image("D:\\freela\\src\\main\\java\\com\\example\\demo\\Principal\\img.png"));
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
            pontoLayout = criarLayoutPonto(i);
            gridPontos.add(pontoLayout, coluna, linha);
            coluna++;
            if (coluna == numeroColunas) {
                coluna = 0;
                linha++;
            }



        }

        Button cadastrarButton = new Button("Cadastrar");
        cadastrarButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        cadastrarButton.setOnAction(event -> processarCadastro(primaryStage, quantidadePontos));

        layoutCadastroPontos.getChildren().add(gridPontos);
        layoutCadastroPontos.getChildren().add(cadastrarButton);
        layout.getChildren().add(layoutCadastroPontos);
    }

    private VBox criarLayoutPonto(int pontoNumero) {


        Label labelSetor = new Label("Setor: " + pontoLubrificacao.getSetor());
        Label labelEquipamento = new Label("Equipamento: " + pontoLubrificacao.getEquipamento());
        labelSetor.setStyle("-fx-font-size: 15;");
        labelEquipamento.setStyle("-fx-font-size: 15;");

        Label labelPonto = new Label("PontoLubrificacao " + pontoNumero + ":");
        labelPonto.setStyle("-fx-font-size: 17; -fx-font-weight: bold;");

        TextField inputPonto = new TextField();

        Label labelTagPonto = new Label("Tag do PontoLubrificacao:");
        TextField inputTagPonto = new TextField();

        Label labelLubrificante = new Label("Lubrificante Total:");
        TextField inputLubrificante = new TextField();

        Label labelQuantidade = new Label("Quantidade:");
        TextField inputQuantidade = new TextField();
        String quantidadeText = inputQuantidade.getText();
//        int quantidade = 0;
//        if (quantidadeText.matches("-?\\b\\d+\\b")) { // Verifica se o texto contém apenas dígitos
//            quantidade = Integer.parseInt(quantidadeText);
//            pontoLubrificacao.setQuantidadeDeLubrificante(quantidade);
//        } else {
//            exibirAlerta("Somente inteiros permitidos");
//        }


        Label labelDataHoraLubrificacao = new Label("Data e Hora Lubrificação:");
        DatePicker datePicker = new DatePicker();



        Label labelProximaDataLubrificacao = new Label("Próxima Data Lubrificação:");
        DatePicker proximaDatePicker = new DatePicker();
        LocalDate proximaData = proximaDatePicker.getValue();


        Label labelQuantidadeGraxa = new Label("Quantidade de Graxa:");
        TextField inputQuantidadeGraxa = new TextField();
        String quantidadeText2 = inputQuantidadeGraxa.getText();
//        int quantidadeGraxa = 0;
//        if (quantidadeText.matches("-?\\b\\d+\\b")) { // Verifica se o texto contém apenas dígitos
//            quantidadeGraxa = Integer.parseInt(quantidadeText2);
//        } else {
//            exibirAlerta("Somente inteiros permitidos");
//        }


        Label labelComponentesEquipamento = new Label("Componentes do Equipamento:");
        TextField inputComponentesEquipamento = new TextField();

        Label labelOperacao = new Label("Operação:");
        TextField inputOperacao = new TextField();

        Label labelObservacoes = new Label("Observações:");
        TextField inputObservacoes = new TextField();
        inputObservacoes.setPromptText("Adicione observações, se necessário");



        ComboBox<UnidadeMedida> comboBoxUnidade = new ComboBox<>();
        comboBoxUnidade.getItems().addAll(UnidadeMedida.values());
        comboBoxUnidade.setValue(UnidadeMedida.GRAMAS);

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
                labelProximaDataLubrificacao, proximaDatePicker,
                labelQuantidadeGraxa, inputQuantidadeGraxa,
                labelComponentesEquipamento, inputComponentesEquipamento,
                labelOperacao, inputOperacao,
                labelObservacoes, inputObservacoes,
                selecionarLubrificanteButton
        );

        return pontoLayout;
    }

    private void processarCadastro(Stage primaryStage, int quantidadePontos) {
        if (pontosLubrificacao.isEmpty()) {
            exibirAlerta("Insira pelo menos um ponto de lubrificação.");
        } else {
            for (int i = 0; i < quantidadePontos; i++) {
                TextField inputPonto  = (TextField) pontoLayout.getChildren().get(1);
                pontosLubrificacao.get(i).setPonto(inputPonto.getText());
                TextField inputTag  = (TextField) pontoLayout.getChildren().get(5);
                pontosLubrificacao.get(i).setTag(inputTag.getText());
                TextField inputQtdLub  = (TextField) pontoLayout.getChildren().get(9);
                pontosLubrificacao.get(i).setQuantidadeDeLubrificante(Integer.parseInt(inputQtdLub.getText()));
                TextField inputQtdGraxa  = (TextField) pontoLayout.getChildren().get(15);
                pontosLubrificacao.get(i).setQuantidadeDeGraxa(Integer.parseInt(inputQtdGraxa.getText()));
                TextField inputComp  = (TextField) pontoLayout.getChildren().get(17);
                pontosLubrificacao.get(i).setComponentes(inputComp.getText());
                TextField inputOp  = (TextField) pontoLayout.getChildren().get(19);
                pontosLubrificacao.get(i).setOperacao(inputOp.getText());
                TextField inputObs  = (TextField) pontoLayout.getChildren().get(21);
                pontosLubrificacao.get(i).setObs(inputObs.getText());
                DatePicker dataLub = (DatePicker) pontoLayout.getChildren().get(11);
                pontosLubrificacao.get(i).setDataHoraLubrificacao(dataLub.getValue());
                DatePicker dataProx = (DatePicker) pontoLayout.getChildren().get(13);
                pontosLubrificacao.get(i).setDataProxLubrificacao(dataProx.getValue());

//                if (lubrificante != null) {
//                    try {
//                        LocalDate dataProximaTroca = pontosLubrificacao.get(i).getDataProxLubrificacao().plusDays(INTERVALO_LUBRIFICACAO_DIAS);
//                        datasProximaTrocaLubrificante.add(LocalDateTime.from(dataProximaTroca));
//                    } catch (Exception e) {
//                        exibirAlerta("Insira uma data válida para a próxima lubrificação.");
//                        return;
//                    }
//                    Entidade<Object> dao = new HibernateEntidade<>();
//
//                    try {
//                        dao.salvarAdicional(Collections.singletonList(pontosLubrificacao));
//                        Alert alert = new Alert((Alert.AlertType.INFORMATION));
//                        alert.setTitle("Sucesso");
//                        alert.setContentText("PontoLubrificacao Cadastrado com sucesso");
//                        alert.show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    exibirAlerta("Selecione um lubrificante antes de cadastrar.");
//                }
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
                    lubrificantes.get(index)
            );
            Stage stagePesquisa = new Stage();
            telaPesquisa.setLubrificanteSelecionadoListener(lubrificanteSelecionado -> {
                lubrificantes.get(index).setText(lubrificanteSelecionado.getDescricao());
                inputsQuantidadeLubrificante.get(index).setText("1");
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
            Notifications notificationBuilder = Notifications.create()
                    .title("Próximas Trocas de Lubrificante")
                    .hideAfter(Duration.seconds(10))
                    .position(Pos.TOP_RIGHT);

            for (int i = 0; i < datasProximaTrocaLubrificante.size(); i++) {
                LocalDateTime dataProximaTroca = datasProximaTrocaLubrificante.get(i);

                long diasRestantes = ChronoUnit.DAYS.between(dataAtual, dataProximaTroca);
                if (diasRestantes <= 7) {
                    notificationBuilder.text("PontoLubrificacao " + (i + 1) + ": Faltam " + diasRestantes + " dias para a próxima troca de lubrificante.");
                    notificationBuilder.showWarning();
                }
            }
        }

        @Override
        public void onLubrificanteSelecionado(Lubrificante lubrificanteSelecionado) {
            this.lubrificante = lubrificanteSelecionado;
        }

        public static void main(String[] args) {
            launch(args);
        }
    }

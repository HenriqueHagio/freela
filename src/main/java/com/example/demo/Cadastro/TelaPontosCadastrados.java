package com.example.demo.Cadastro;

import com.example.demo.Cadastro.Constante.UnidadeMedida;
import com.example.demo.Hibernate.Entidade;
import com.example.demo.Hibernate.HibernateEntidade;
import com.example.demo.comeco.Empresa;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TelaPontosCadastrados extends Application {

    PontoLubrificacao pontoLubrificacao = new PontoLubrificacao();

    static Empresa empresa = new Empresa();

    private List<PontoLubrificacao> pontosCadastrados = new ArrayList<>();

    private Boolean editavel = false;

    Entidade<Object> dao = new HibernateEntidade<>();

    VBox layout = new VBox(10);

    public TelaPontosCadastrados(List<PontoLubrificacao> pontosCadastrados) {
        this.pontosCadastrados = pontosCadastrados;
    }

    public TelaPontosCadastrados(Empresa empresa) {
        this.empresa = empresa;
        this.pontosCadastrados = pontoLubrificacao.buscarPontosPorEmpresa(empresa);
    }

    public TelaPontosCadastrados() {

    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pontos Cadastrados");

        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20px;"); // Estilo de fundo e espaçamento interno

        // Adicionando um ImageView para a logo no canto superior direito
        ImageView logoImageView = new ImageView(new Image("file:src/main/java/com/example/demo/Principal/img.png"));
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
            for (PontoLubrificacao ponto : pontosCadastrados) {
                Button btnPonto = new Button(ponto.getPonto());
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

    private void exibirDetalhesPonto(PontoLubrificacao ponto) {
        Stage detalhesStage = new Stage();
        detalhesStage.setTitle("Detalhes do Ponto");


        layout.setStyle("-fx-background-color: #f9f9f9; -fx-padding: 10px; -fx-border-color: #000000; -fx-border-width: 1px; -fx-border-radius: 5px;");

        // Adicione os componentes de detalhes do ponto ao layout
        Label labelSetor = new Label("Setor: " + ponto.getSetor());
        Label labelEquipamento = new Label("Equipamento: " + ponto.getEquipamento());


        Label labelPonto = new Label("PontoLubrificacao:");
        TextField inputPonto = new TextField();
        inputPonto.setText(ponto.getPonto());
        inputPonto.setEditable(editavel);
        ponto.setPonto(inputPonto.getText());

        Label labelTagPonto = new Label("Tag do Ponto:");
        TextField inputTagPonto = new TextField();
        inputTagPonto.setText(ponto.getTag());
        inputTagPonto.setEditable(editavel);
        ponto.setTag(inputTagPonto.getText());

        Label labelLubrificante = new Label("Lubrificante Total:");
        TextField inputLubrificante = new TextField();
        inputLubrificante.setEditable(false);



        Label labelQuantidade = new Label("Quantidade:");
        TextField inputQuantidade = new TextField();
        inputQuantidade.setText(ponto.getQuantidadeDeLubrificante().toString());
        inputQuantidade.setEditable(editavel);
        ponto.setQuantidadeDeLubrificante(Integer.parseInt(inputQuantidade.getText()));

        ComboBox<UnidadeMedida> comboBoxUnidade = new ComboBox<>();
        comboBoxUnidade.getItems().addAll(UnidadeMedida.values());
        comboBoxUnidade.setValue(ponto.getUnidadeMedida());
        comboBoxUnidade.setEditable(editavel);
        ponto.setUnidadeMedida(comboBoxUnidade.getValue());

        Label labelDataHoraLubrificacao = new Label("Data e Hora Lubrificação:");
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(ponto.getDataHoraLubrificacao().toLocalDate());
        datePicker.setEditable(editavel);
        LocalTime horaAtual = LocalTime.now();
        LocalDateTime dataHoraLub = LocalDateTime.of(datePicker.getValue(), horaAtual);
        ponto.setDataHoraLubrificacao(dataHoraLub);

        Label labelProximaDataLubrificacao = new Label("Próxima Data Lubrificação:");
        DatePicker proximaDatePicker = new DatePicker();
        proximaDatePicker.setValue(ponto.getDataProxLubrificacao());
        proximaDatePicker.setEditable(editavel);
        ponto.setDataProxLubrificacao(proximaDatePicker.getValue());

        Label labelQuantidadeGraxa = new Label("Quantidade de Graxa:");
        TextField inputQuantidadeGraxa = new TextField();
        inputQuantidadeGraxa.setText(ponto.getQuantidadeDeGraxa().toString());
        inputQuantidadeGraxa.setEditable(editavel);
        ponto.setQuantidadeDeGraxa(Integer.parseInt(inputQuantidadeGraxa.getText()));

        Label labelComponentesEquipamento = new Label("Componentes do Equipamento:");
        TextField inputComponentesEquipamento = new TextField();
        inputComponentesEquipamento.setText(ponto.getComponentes());
        inputComponentesEquipamento.setEditable(editavel);
        ponto.setComponentes(inputComponentesEquipamento.getText());

        Label labelOperacao = new Label("Operação:");
        TextField inputOperacao = new TextField();
        inputOperacao.setText(ponto.getOperacao());
        inputOperacao.setEditable(editavel);
        ponto.setOperacao(inputOperacao.getText());

        Label labelObservacoes = new Label("Observações:");
        TextField inputObservacoes = new TextField();
        inputObservacoes.setPromptText("Adicione observações, se necessário");
        inputObservacoes.setText(ponto.getObs());
        inputObservacoes.setEditable(editavel);
        ponto.setObs(inputObservacoes.getText());

        Button editarButton = new Button("Editar");
        editarButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        editarButton.setOnAction(event -> {
            editavel = !editavel; // Alternar entre editável e não editável
            setCamposEditaveis(editavel, inputPonto, inputTagPonto, inputLubrificante, inputQuantidade,
                    inputQuantidadeGraxa, inputComponentesEquipamento, inputOperacao, inputObservacoes);
            setDatasEditaveis(editavel, datePicker, proximaDatePicker);
            setComboBoxEditavel(editavel, comboBoxUnidade);

        });
        Button salvarButton = new Button("Salvar Alterações");
        salvarButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        salvarButton.setOnAction(event -> {
            salvar(ponto);
            alerta();
            detalhesStage.close();
        });

        layout.getChildren().addAll(
                labelPonto, inputPonto,
                labelSetor, labelEquipamento,
                labelTagPonto, inputTagPonto,
                labelLubrificante, inputLubrificante,
                labelQuantidade, new HBox(5, inputQuantidade, comboBoxUnidade),
                labelDataHoraLubrificacao, datePicker,
                labelProximaDataLubrificacao, proximaDatePicker,
                labelQuantidadeGraxa, inputQuantidadeGraxa,
                labelComponentesEquipamento, inputComponentesEquipamento,
                labelOperacao, inputOperacao,
                labelObservacoes, inputObservacoes, editarButton, salvarButton
        );


        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        Scene scene = new Scene(scrollPane);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        detalhesStage.setX(primaryScreenBounds.getMinX());
        detalhesStage.setY(primaryScreenBounds.getMinY());
        detalhesStage.setWidth(primaryScreenBounds.getWidth());
        detalhesStage.setHeight(primaryScreenBounds.getHeight());

        detalhesStage.setScene(scene);

        detalhesStage.show();
    }

    public static void main(String[] args) {
        PontoLubrificacao ponto = new PontoLubrificacao();
        List<PontoLubrificacao> pontos = ponto.buscarPontosPorEmpresa(empresa) ;
        launch(new TelaPontosCadastrados(pontos), args);
    }

    private static void launch(TelaPontosCadastrados telaPontosCadastrados, String[] args) {
    }

    private void setCamposEditaveis(boolean editable, TextField... fields) {
        for (TextField field : fields) {
            field.setEditable(editable);
        }
    }
    private void setDatasEditaveis(boolean editable, DatePicker... datePickers) {
        for (DatePicker datePicker : datePickers) {
            datePicker.setEditable(editable);
        }
    }

    private void setComboBoxEditavel(boolean editable, ComboBox<UnidadeMedida> comboBox) {
        comboBox.setDisable(!editable);
    }

    private void salvar(PontoLubrificacao ponto){


        TextField inputPonto  = (TextField) layout.getChildren().get(1);
        ponto.setPonto(inputPonto.getText());

        TextField inputTag  = (TextField) layout.getChildren().get(5);
        ponto.setTag(inputTag.getText());

        HBox caixa  = (HBox) layout.getChildren().get(9);
        TextField inputQtdLub  = (TextField) caixa.getChildren().get(0);
        ponto.setQuantidadeDeLubrificante(Integer.parseInt(inputQtdLub.getText()));

        ComboBox<UnidadeMedida> comboBoxUnidade = (ComboBox<UnidadeMedida>) caixa.getChildren().get(1);
        ponto.setUnidadeMedida(comboBoxUnidade.getValue());

        TextField inputQtdGraxa  = (TextField) layout.getChildren().get(15);
        ponto.setQuantidadeDeGraxa(Integer.parseInt(inputQtdGraxa.getText()));

        TextField inputComp  = (TextField) layout.getChildren().get(17);
        ponto.setComponentes(inputComp.getText());

        TextField inputOp  = (TextField) layout.getChildren().get(19);
        ponto.setOperacao(inputOp.getText());

        TextField inputObs  = (TextField) layout.getChildren().get(21);
        ponto.setObs(inputObs.getText());

        DatePicker dataLub = (DatePicker) layout.getChildren().get(11);
        LocalTime horaAtual = LocalTime.now();
        LocalDateTime dataHoraLub = LocalDateTime.of(dataLub.getValue(), horaAtual);
        ponto.setDataHoraLubrificacao(dataHoraLub);

        DatePicker dataProx = (DatePicker) layout.getChildren().get(13);
        ponto.setDataProxLubrificacao(dataProx.getValue());
        dao.atualizar(ponto);

    }
    public void alerta(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Ponto atualizado com sucesso!");
        alert.show();
    }
}

package com.example.demo.Cadastro;

import com.example.demo.Cadastro.Constante.UnidadeMedida;
import com.example.demo.Estoque.Produto;
import com.example.demo.Hibernate.Entidade;
import com.example.demo.Hibernate.HibernateEntidade;
import com.example.demo.Lubrificantes.Lubrificante;
import com.example.demo.Lubrificantes.LubrificanteSelecionadoListener;
import com.example.demo.Lubrificantes.TelaPesquisaLubrificantes;
import com.example.demo.comeco.Empresa;
import com.example.demo.comeco.Usuario;
import javafx.application.Application;
import javafx.collections.FXCollections;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelaCadastramento extends Application implements LubrificanteSelecionadoListener {
    private Usuario usuario;

    private VBox layout;

    private Boolean confere;

    private final List<PontoLubrificacao> pontosLubrificacao = new ArrayList<>();

    private Lubrificante lubrificante = new Lubrificante();

    private Empresa empresaAdmin = new Empresa();

    private Lubrificante lubrificanteSalvar = new Lubrificante();

    private final List<Lubrificante> lubrificantes = lubrificante.recuperarTodos();

    private Consumer<Stage> onCadastroConcluido;



    private final List<LocalDateTime> datasLubrificacao = new ArrayList<>();

    private static final int INTERVALO_LUBRIFICACAO_DIAS = 30;

    Entidade<Object> dao = new HibernateEntidade<>();

    private VBox pontoLayout;  
    
    private List<VBox> pontosLayout = new ArrayList<>();

    private List<LocalDateTime> datasProximaTrocaLubrificante = new ArrayList<>();



    public TelaCadastramento(Consumer<Stage> onCadastroConcluido, Usuario usuario) {
        this.onCadastroConcluido = onCadastroConcluido;
        this.usuario = usuario;
    }

    public TelaCadastramento() {
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

        Label labelEmpresa = new Label("Selecione a empresa:");
        ComboBox<String> comboBoxEmpresa = new ComboBox<>();
        List<Empresa> empresas = new Empresa().recuperarTodos();
        List<String> ls = new ArrayList<>();
        for(Empresa l : empresas){
            ls.add(l.getNome());
            comboBoxEmpresa.setItems(FXCollections.observableArrayList(ls));
        }
        comboBoxEmpresa.getItems().addAll();
//        comboBoxEmpresa.visibleProperty().set(usuario.getRole().equals("admin"));


        Button confirmarButton = new Button("Confirmar");
        confirmarButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        confirmarButton.setOnAction(event -> processarConfirmacao(primaryStage, inputSetor.getText(), inputEquipamento.getText(),
                inputQuantidadePontos.getText(), comboBoxEmpresa.getValue()));

        layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #f2f2f2;");
        layout.getChildren().addAll(
                logoBox,  // Adiciona o HBox com a imagem
                labelSetor, inputSetor,
                labelEquipamento, inputEquipamento,
                labelQuantidadePontos, inputQuantidadePontos,
                labelEmpresa,comboBoxEmpresa, confirmarButton
        );
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void processarConfirmacao(Stage primaryStage, String setor, String equipamento, String quantidadePontosTexto, String empresa) {
        if (validarCampos(setor, equipamento, quantidadePontosTexto)) {
            int quantidadePontos = Integer.parseInt(quantidadePontosTexto);

            for(int i = 1; i <= Integer.parseInt(quantidadePontosTexto); i++){
                PontoLubrificacao ponto = new PontoLubrificacao(); // Criando uma nova instância
                ponto.setSetor(setor);
                ponto.setEquipamento(equipamento);
                empresaAdmin = new Empresa().buscarPessoaPorNome(empresa);
//                if (usuario.getRole().equals("admin"))
                ponto.setEmpresa(empresaAdmin);
//                else ponto.setEmpresa(usuario.getPessoa().getEmpresa());
                pontosLubrificacao.add(ponto);
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

        Label setorLabel = new Label("Setor: " + pontosLubrificacao.get(0).getSetor());
        Label equipamentoLabel = new Label("Equipamento: " + pontosLubrificacao.get(0).getEquipamento());
        setorLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        equipamentoLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Adicionando um ImageView para um ícone ou logotipo
        ImageView logoImageView = new ImageView(new Image("file:src/main/java/com/example/demo/Principal/img.png"));
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
//            try {
//                if(pontoLayout != null)
//                    pontosLayout.add(pontoLayout);
//            }
//            catch (Exception e){}
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
        Label labelSetor = new Label("Setor: " + pontosLubrificacao.get(0).getSetor());
        Label labelEquipamento = new Label("Equipamento: " + pontosLubrificacao.get(0).getEquipamento());
        labelSetor.setStyle("-fx-font-size: 15;");
        labelEquipamento.setStyle("-fx-font-size: 15;");

        Label labelPonto = new Label("PontoLubrificacao " + pontoNumero + ":");
        labelPonto.setStyle("-fx-font-size: 17; -fx-font-weight: bold;");

        TextField inputPonto = new TextField();

        Label labelTagPonto = new Label("Tag do Ponto:");
        TextField inputTagPonto = new TextField();

        Label labelLubrificante = new Label("Lubrificante Total:");
        TextField inputLubrificante = new TextField();
        inputLubrificante.setEditable(false);

        Label labelQuantidade = new Label("Quantidade:");
        TextField inputQuantidade = new TextField();
//        String quantidadeText = inputQuantidade.getText();
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
        selecionarLubrificanteButton.setOnAction(event -> processarSelecaoLubrificante(pontoNumero, inputLubrificante));


        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: #f9f9f9; -fx-padding: 10px; -fx-border-color: #000000; -fx-border-width: 1px; -fx-border-radius: 5px;");
        layout.getChildren().addAll(
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
        pontosLayout.add(layout);

        return layout;
    }

    private void processarCadastro(Stage primaryStage, int quantidadePontos) {
        if (pontosLubrificacao.isEmpty()) {
            exibirAlerta("Insira pelo menos um ponto de lubrificação.");
        } else {
            for (int i = 0; i < quantidadePontos; i++) {
                PontoLubrificacao ponto = pontosLubrificacao.get(i);
                VBox layout = pontosLayout.get(i);
                
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


                ponto.setLubrificante(lubrificanteSalvar);
                Produto produto = new Produto().recuperarPorLubrificante(lubrificanteSalvar);
                tratarUnidadeMedida(comboBoxUnidade.getValue(),produto, ponto);
                if (lubrificante != null) {
                    try {
                        LocalDate dataProximaTroca = ponto.getDataProxLubrificacao().plusDays(INTERVALO_LUBRIFICACAO_DIAS);
                        LocalDateTime dataHoraProximaTroca = dataProximaTroca.atStartOfDay();
                        datasProximaTrocaLubrificante.add(dataHoraProximaTroca);
                    } catch (Exception e) {
                        exibirAlerta("Insira uma data válida para a próxima lubrificação.");
                        return;
                    }

                } else {
                    exibirAlerta("Selecione um lubrificante antes de cadastrar.");
                }
            }
            if (confere) {
                mostrarMensagemCadastro();
                adicionarBotaoVoltar(primaryStage);
                verificarProximidadeTrocaLubrificante();
            }

        }
    }

    private void adicionarBotaoVoltar(Stage primaryStage) {
        Button voltarButton = new Button("Voltar");
        voltarButton.setStyle("-fx-background-color: #757575; -fx-text-fill: white;");
        voltarButton.setOnAction(event -> {
            if (onCadastroConcluido != null) {
                onCadastroConcluido.accept(primaryStage);
            }
            primaryStage.close();
        });

        layout.getChildren().add(voltarButton);
    }

    private void processarSelecaoLubrificante(int pontoNumero, TextField inputLubrificante) {

        TelaPesquisaLubrificantes telaPesquisa = new TelaPesquisaLubrificantes(
                this,
                lubrificantes.get(pontoNumero -1),
                empresaAdmin
        );
        Stage stagePesquisa = new Stage();
        telaPesquisa.setLubrificanteSelecionadoListener(lubrificanteSelecionado -> {
            lubrificanteSalvar = lubrificanteSelecionado;
            inputLubrificante.setText(lubrificanteSelecionado.getDescricao()); // Atualiza o texto do TextField
        });
        telaPesquisa.start(stagePesquisa);
    }

    private void mostrarMensagemCadastro() {
        try{
            for(PontoLubrificacao p : pontosLubrificacao)
                dao.salvar(p);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText(null);
            alert.setContentText("Pontos cadastrados com sucesso!");
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        for (LocalDateTime dataProximaTroca : datasProximaTrocaLubrificante) {
            long diasRestantes = ChronoUnit.DAYS.between(dataAtual, dataProximaTroca);
            if (diasRestantes <= 7) {
                notificationBuilder.text("PontoLubrificacao: Faltam " + diasRestantes + " dias para a próxima troca de lubrificante.");
                notificationBuilder.showWarning();
            }
        }
    }
    private void calcularEstoque(Produto produto, PontoLubrificacao ponto){

        double quantidade = produto.getQuantidade();
        double quantidadePonto = ponto.getQuantidadeDeLubrificante();
        double total = quantidade - quantidadePonto;
        produto.setQuantidade(total);
        dao.atualizar(produto);
    }

    private void tratarUnidadeMedida(UnidadeMedida unidadeMedida, Produto produto, PontoLubrificacao ponto){
        Pattern pattern = Pattern.compile("\\b(\\d+)(kg|L)\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(produto.getLubrificante().getDescricao());
        while (matcher.find()) {
            String valor = matcher.group(1);
            String unidade = matcher.group(2);

            if (unidade.equalsIgnoreCase("kg") && unidadeMedida.equals(unidadeMedida.GRAMAS )) {
                calcularEstoque(produto, ponto);
                confere = true;
            } else if (unidade.equalsIgnoreCase("L") && unidadeMedida.equals(unidadeMedida.LITROS)) {
                calcularEstoque(produto, ponto);
                confere = true;
            } else {
                exibirAlerta("Selecione uma unidade de medida combativel com a do lubrificante");
                confere = false;
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

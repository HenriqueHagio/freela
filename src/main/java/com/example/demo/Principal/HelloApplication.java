package com.example.demo.Principal;

import com.example.demo.Cadastro.PontoLubrificacao;
import com.example.demo.Cadastro.TelaCadastramento;
import com.example.demo.Cadastro.TelaPontosCadastrados;
import com.example.demo.Estoque.TelaCadastroEstoque;
import com.example.demo.Estoque.TelaEstoque;
import com.example.demo.Lubrificantes.TelaCadastrarLubrificantes;
import com.example.demo.Lubrificantes.TelaLubrificantes;
import com.example.demo.comeco.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class HelloApplication extends Application {
    static Usuario usuario = new Usuario();
    private static TelaEstoque telaEstoque;

    static String texto = "";


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Lubrificação - Login");

        // Criando um VBox para o layout de login
        VBox loginLayout = new VBox(20);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        loginLayout.setPadding(new Insets(20));

        // Adicionando um ImageView para um ícone ou logotipo
        ImageView logoImageView = new ImageView(new Image("file:src/main/java/com/example/demo/Principal/img.png"));
        logoImageView.setFitHeight(100);  // Ajuste a altura conforme necessário
        logoImageView.setPreserveRatio(true);

        // Adicionando o título
        Label titleLabel = new Label("Bem-vindo ao Sistema de Lubrificação");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        loginLayout.getChildren().addAll(logoImageView, titleLabel);

        TextField usernameField = createStyledTextField();
        PasswordField passwordField = createStyledPasswordField();

        // HBox para os botões "Registre-se" e "Login"
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(
                createStyledButton("Login"),
                createStyledButton("Registre-se")
                );

        Button loginButton = (Button) buttonsBox.getChildren().get(0);
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();


            if (authenticate(username, password)) {
                Usuario tipoUsuario = usuario.buscarUsuarioPorNome(username);

                List<PontoLubrificacao> ponto = new PontoLubrificacao().buscarPontosPorEmpresaLimit(usuario.getPessoa().getEmpresa());
                for(int i = 0; i < ponto.size(); i++){
                    int dias = (int) ChronoUnit.DAYS.between(ponto.get(i).getDataProxLubrificacao(), LocalDate.now());
                    texto += "A proxima lubrificação do ponto " + ponto.get(i).getPonto() + " esta agendada para " + dias + " dia(s), \n";
                }
                try {
                    if (tipoUsuario.getRole() == "admin") {
                        showAdminScreen(primaryStage, usuario);
                    } else {showMainMenu(primaryStage, username);}
                }
                catch (Exception e) {}
            } else {
                showError("Credenciais inválidas. Tente novamente.");
            }
        });

        Button registerButton = (Button) buttonsBox.getChildren().get(1);
        registerButton.setOnAction(event -> {
            showNewUserScreen(primaryStage);
        });


        Hyperlink forgotPasswordLink = new Hyperlink("Esqueceu a senha?");
        forgotPasswordLink.setOnAction(event -> {
            try {
                showForgotPasswordDialog();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        loginLayout.getChildren().addAll(usernameField, passwordField, buttonsBox, forgotPasswordLink);


        Scene scene = new Scene(loginLayout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TextField createStyledTextField() {
        TextField textField = new TextField();
        textField.setPromptText("Usuário");
        textField.setStyle("-fx-background-color: #f2f2f2; -fx-border-color: #e6e6e6; -fx-padding: 8px;");
        return textField;
    }

    private PasswordField createStyledPasswordField() {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Senha");
        passwordField.setStyle("-fx-background-color: #f2f2f2; -fx-border-color: #e6e6e6; -fx-padding: 8px;");
        return passwordField;
    }

    private Button createStyledButton(String value) {
        Button button = new Button(value);
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-padding: 10px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px;"));
        return button;
    }

    private static boolean authenticate(String username, String password) {
        // Lógica para buscar informações do usuário (incluindo a senha criptografada) a partir do banco de dados

        usuario = usuario.buscarUsuarioPorNome(username);

        // Verifica se o usuário foi encontrado e se a senha corresponde à senha armazenada no banco de dados
        if (usuario != null) {
            String hashedPassword = usuario.getPassword(); // Obtém a senha criptografada do usuário
            return BCrypt.checkpw(password, hashedPassword); // Verifica se a senha fornecida corresponde à senha criptografada
        }
        return false; // Retorna falso se o usuário não foi encontrado
    }


    private static boolean isAdmin(String username) {
        return username.equals(Credenciais.ADMIN);
    }

    private static void showError(String message) {
        // Método para exibir mensagens de erro
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private static void showForgotPasswordDialog() throws IOException {
        // Método para exibir um diálogo para recuperar senha
        TelaRecuperacaoSenha telaRecuperacaoSenha = new TelaRecuperacaoSenha();
        Stage recoveryStage = new Stage();
        telaRecuperacaoSenha.start(recoveryStage);
    }
    private static void showNewUserScreen(Stage primaryStage){
        TelaCriarNovoUsuario telaCriarNovoUsuario = new TelaCriarNovoUsuario();
        telaCriarNovoUsuario.start(primaryStage, usuario);
    }

    private static void showAdminScreen(Stage primaryStage, Usuario username) {
        TelaAdministrador telaAdmin = new TelaAdministrador();
        telaAdmin.start(primaryStage, username);
    }

    public static void showMainMenu(Stage primaryStage, String username) {
        // Exibindo o menu principal após o login bem-sucedido
        primaryStage.setTitle("Sistema de Lubrificação - Menu Principal");

        Label titleLabel = new Label("Sistema de Lubrificação - Lubvel Lubrificantes");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-padding: 10px 0;");
        Label proxLub;
        try {
            proxLub = new Label(texto.substring(0, texto.length() - 3));

        } catch (Exception e) {
            proxLub = new Label("");
        }
        titleLabel.setStyle("-fx-font-size: 22px;  -fx-padding: 10px 0;");

        Button cadastrarPontosButton = criarBotao("Cadastramento de Pontos");
        cadastrarPontosButton.setOnAction(event -> {
            // Exemplo: Voltando para o menu principal
            TelaCadastramento telaCadastramento = new TelaCadastramento(t -> primaryStage.show(), usuario);

            Stage cadastroPontosStage = new Stage();
            telaCadastramento.start(cadastroPontosStage);
        });

        Button pontosButton = criarBotao("Pontos");
        pontosButton.setOnAction(event -> {
            TelaPontosCadastrados telaPontos = new TelaPontosCadastrados(usuario.getPessoa().getEmpresa());
            Stage pontosStage = new Stage();
            if (!usuario.getRole().equals("admin"))
                telaPontos.start(pontosStage);
            else telaPontos.telaAdm(pontosStage);
        });
        Button lubrificantesButton = criarBotao("Lubrificantes");
        lubrificantesButton.setOnAction(event -> {
            Stage lubrificantesStage = new Stage();


            TelaLubrificantes telaLubrificantes = new TelaLubrificantes(lubrificantesStage);


            telaLubrificantes.start();

        });

        Button estoqueButton = criarBotao("Estoque");
        estoqueButton.setOnAction(event -> {
            telaEstoque = new TelaEstoque();
            Stage estoqueStage = new Stage();
            telaEstoque.start(estoqueStage);

        });


        VBox menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setStyle("-fx-background-color: #f4f4f4;");
        menuLayout.getChildren().addAll(
                titleLabel,
                proxLub,
                cadastrarPontosButton,
                pontosButton,
                lubrificantesButton,
                estoqueButton
        );
        if (usuario.getRole().equals("admin")) {
            Button cadastrarLubButton = criarBotao("Cadastrar Lubrificante");
            cadastrarLubButton.setOnAction(event -> {
                TelaCadastrarLubrificantes telaCadastrarLubrificantes = new TelaCadastrarLubrificantes();
                Stage estoqueStage = new Stage();
                telaCadastrarLubrificantes.start(estoqueStage);

            });
            menuLayout.getChildren().add(cadastrarLubButton);
        }
        if (usuario.getRole().equals("admin")) {
            Button cadastroEstoqueButton = criarBotao("Cadastro de Estoque");
            cadastroEstoqueButton.setOnAction(event -> {
                TelaCadastroEstoque telaCadastroEstoque = new TelaCadastroEstoque(usuario);
                Stage cadastroEstoqueStage = new Stage();
                telaCadastroEstoque.start(cadastroEstoqueStage);
            });
            menuLayout.getChildren().add(cadastroEstoqueButton);
        }

        if (isAdmin(username)) {
            Button gerenciarUsuariosButton = criarBotao("Gerenciar Usuários");
            gerenciarUsuariosButton.setOnAction(event -> showGerenciamentoUsuarios(primaryStage));
            menuLayout.getChildren().add(gerenciarUsuariosButton);
        }

        Scene mainScene = new Scene(menuLayout, 500, 500);
        primaryStage.setScene(mainScene);
    }

    private static Button criarBotao(String texto) {
        Button button = new Button(texto);
        button.setMinWidth(200);
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-padding: 10px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px;"));
        return button;
    }

    private static void showGerenciamentoUsuarios(Stage primaryStage) {
        TelaGerenciarClientes telaGerenciamentoUsuarios = new TelaGerenciarClientes();
        Stage gerenciamentoUsuariosStage = new Stage();
        telaGerenciamentoUsuarios.start(gerenciamentoUsuariosStage, usuario);
    }
}

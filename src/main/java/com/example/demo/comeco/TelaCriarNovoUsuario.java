package com.example.demo.comeco;

import com.example.demo.Hibernate.Entidade;
import com.example.demo.Hibernate.HibernateEntidade;
import com.example.demo.Principal.HelloApplication;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class TelaCriarNovoUsuario {

    private TelaGerenciarClientes telaGerenciarClientes;

    public TelaCriarNovoUsuario(TelaGerenciarClientes telaGerenciarClientes) {
        this.telaGerenciarClientes = telaGerenciarClientes;
    }

    public TelaCriarNovoUsuario() {
        // Remova a inicialização desnecessária da telaGerenciarClientes aqui
    }

    public void start(Stage stage, Usuario admin) {
        stage.setTitle("Criar Novo Usuário");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label labelNome = new Label("Nome:");
        TextField campoNome = new TextField();

        Label labelSobrenome = new Label("Sobrenome:");
        TextField campoSobrenome = new TextField();

        Label labelUsername = new Label("Username:");
        TextField campoUsername = new TextField();

        Label labelEmail = new Label("E-mail:");
        TextField campoEmail = new TextField();

        // Solicita o nome da empresa antes da senha
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Cadastro de Usuário");
        dialog.setHeaderText(null);
        dialog.setContentText("Digite o nome da empresa:");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String empresa = result.get();

            Label labelSenha = new Label("Senha:");
            PasswordField campoSenha = new PasswordField();

            Label labelConfirmarSenha = new Label("Confirmar Senha:");
            PasswordField campoConfirmarSenha = new PasswordField();

            Button cadastrarButton = new Button("Cadastrar");
            cadastrarButton.setOnAction(event -> cadastrarNovoUsuario(
                    admin,
                    campoNome.getText(),
                    campoSobrenome.getText(),
                    campoUsername.getText(),
                    campoEmail.getText(),
                    campoSenha.getText(),
                    campoConfirmarSenha.getText(),
                    empresa,
                    stage
            ));

            layout.getChildren().addAll(labelNome, campoNome, labelSobrenome, campoSobrenome, labelUsername, campoUsername,
                    labelEmail, campoEmail, labelSenha, campoSenha, labelConfirmarSenha, campoConfirmarSenha, cadastrarButton);

            Scene scene = new Scene(layout, 400, 300);
            stage.setScene(scene);
            stage.show();
        }
    }
    private void  salvarNovoUsuario(Usuario admin, String nome, String sobrenome, String username, String email, String senha, String confirmarSenha, String empresa, Stage stage){
        String senhaHash = BCrypt.hashpw(senha, BCrypt.gensalt());
        Usuario usuario = new Usuario();
        Empresa empresaNova = new Empresa();
        Empresa buscaEmpresa = new Empresa().buscarPessoaPorNome(empresa);
        Pessoa pessoa = new Pessoa();
        Entidade<Object> dao = new HibernateEntidade<>();

        // Verifica se a Empresa ja existe
        if(buscaEmpresa == null) {
            empresaNova.setNome(empresa);
            pessoa.setEmpresa(empresaNova);
            dao.salvar(empresaNova);
        }
        else {
            pessoa.setEmpresa(buscaEmpresa);
        }

        pessoa.setNome(nome);
        pessoa.setSobrenome(sobrenome);
        pessoa.setEmail(email);
        usuario.setUsername(username);
        usuario.setPassword(senhaHash);
        usuario.setPessoa(pessoa);


        try {

            dao.salvar(pessoa);
            usuario.setRole("cliente");
            dao.salvar(usuario);
            Alert alert = new Alert((Alert.AlertType.INFORMATION));
            alert.setTitle("Sucesso");
            alert.setContentText("Usuario Criado com Sucesso");
            alert.show();
            try{
                if(!admin.getRole().equals("admin")){
                    HelloApplication helloApplication = new HelloApplication();
                    helloApplication.start(stage);
                }
            }catch (Exception ignored) {}
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void showError(String message) {
        // Método para exibir mensagens de erro
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private void cadastrarNovoUsuario(Usuario admin, String nome, String sobrenome, String username, String email, String senha, String confirmarSenha, String empresa, Stage stage) {
        if (senha.equals(confirmarSenha)) {
            Usuario verificarUsuario = new Usuario().buscarUsuarioPorNome(username);
            Pessoa verificarPessoa = new Pessoa().buscarPessoaPorEmail(email);
            if (verificarUsuario == null) {
                if(verificarPessoa == null)
                  salvarNovoUsuario(admin, nome, sobrenome, username, email, senha, confirmarSenha, empresa, stage);
                else showError("Email já existe");
            }else showError("Usuario já existe");

        } else {
            showError("Erro: As senhas não coincidem. Tente novamente.");
        }
    }
}

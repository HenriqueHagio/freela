package com.example.demo.comeco;

import com.example.demo.Hibernate.Entidade;
import com.example.demo.Hibernate.HibernateEntidade;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TelaGerenciarClientes {

    private TableView<Object> tabelaClientes;

    private ObservableList<Object> listaClientes = FXCollections.observableArrayList(new  Usuario().recuperarClientes());

    private TelaCriarNovoUsuario telaCriarNovoUsuario;

    Entidade<Object> dao = new HibernateEntidade<>();



    public void start(Stage primaryStage, Usuario admin) {
        primaryStage.setTitle("Gerenciamento de Clientes");


        inicializarTabelaClientes();

        Button adicionarButton = new Button("Adicionar Cliente");
        adicionarButton.setOnAction(e -> adicionarNovoCliente(admin));

        Button editarButton = new Button("Editar Cliente");
        editarButton.setOnAction(e -> editarCliente());

        Button excluirButton = new Button("Excluir Cliente");
        excluirButton.setOnAction(e -> excluirCliente());

        Button visualizarButton = new Button("Visualizar Informações");
        visualizarButton.setOnAction(e -> visualizarInformacoesCliente());

        HBox botoesLayout = new HBox(10);
        botoesLayout.getChildren().addAll(adicionarButton, editarButton, excluirButton, visualizarButton);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(tabelaClientes, botoesLayout);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Inicializa a lista de clientes

        tabelaClientes.setItems(listaClientes);

        // Inicializa a tela para criar novo usuário
        telaCriarNovoUsuario = new TelaCriarNovoUsuario(this);
    }

    private void inicializarTabelaClientes() {
        tabelaClientes = new TableView<>();
            TableColumn<Object, String> colunaEmpresa = new TableColumn<>("Empresa");
            colunaEmpresa.setCellValueFactory(cellData ->{
                Usuario usuario = (Usuario) cellData.getValue();
                return new SimpleStringProperty(usuario.getPessoa().getEmpresa().getNome());

            });

            TableColumn<Object, String> colunaNome = new TableColumn<>("Nome");
            colunaNome.setCellValueFactory(cellData ->{
                Usuario usuario = (Usuario) cellData.getValue();
                return new SimpleStringProperty(usuario.getPessoa().getNome());

            });

            TableColumn<Object, String> colunaSobrenome = new TableColumn<>("Sobrenome");
            colunaSobrenome.setCellValueFactory(cellData ->{
                Usuario usuario = (Usuario) cellData.getValue();
                return new SimpleStringProperty(usuario.getPessoa().getSobrenome());

            });

            TableColumn<Object, String> colunaEmail = new TableColumn<>("E-mail");
            colunaEmail.setCellValueFactory(cellData ->{
                Usuario usuario = (Usuario) cellData.getValue();
                return new SimpleStringProperty(usuario.getPessoa().getEmail());

            });

            tabelaClientes.getColumns().addAll(colunaEmpresa, colunaNome, colunaSobrenome, colunaEmail);



    }

    private void editarCliente() {
        Usuario clienteSelecionado = (Usuario) tabelaClientes.getSelectionModel().getSelectedItem();


        if (clienteSelecionado != null) {
            // Criação dos campos de edição
            TextField nomeField = new TextField(clienteSelecionado.getPessoa().getNome());


            TextField sobrenomeField = new TextField(clienteSelecionado.getPessoa().getSobrenome());

            TextField emailField = new TextField(clienteSelecionado.getPessoa().getEmail());

            PasswordField senhaField = new PasswordField();
            senhaField.setPromptText("Nova Senha");
            String senhaHash = BCrypt.hashpw(senhaField.getText(), BCrypt.gensalt());


            TextField empresaField = new TextField(clienteSelecionado.getPessoa().getEmpresa().getNome());
            ComboBox<String> comboBoxEmpresa = new ComboBox<>();
            List<Empresa> empresas = new Empresa().recuperarTodos();
            List<String> ls = new ArrayList<>();
            for(Empresa l : empresas){
                ls.add(l.getNome());
                comboBoxEmpresa.setItems(FXCollections.observableArrayList(ls));
            }
            comboBoxEmpresa.getItems().addAll();
            comboBoxEmpresa.setValue(empresaField.getText());



            // Layout para os campos de edição
            VBox editarLayout = new VBox(10);
            editarLayout.getChildren().addAll(
                    new Label("Nome:"), nomeField,
                    new Label("Sobrenome:"), sobrenomeField,
                    new Label("E-mail:"), emailField,
                    new Label("Nova Senha:"), senhaField,
                    new Label("Empresa:"), comboBoxEmpresa
            );

            // Diálogo de edição
            Alert editarDialog = new Alert(Alert.AlertType.CONFIRMATION);
            editarDialog.setTitle("Editar Cliente");
            editarDialog.setHeaderText(null);
            editarDialog.getDialogPane().setContent(editarLayout);

            Optional<ButtonType> result = editarDialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Atualiza as informações do cliente

                clienteSelecionado.getPessoa().setNome(nomeField.getText());
                clienteSelecionado.getPessoa().setSobrenome(sobrenomeField.getText());
                clienteSelecionado.getPessoa().setEmail(emailField.getText());

                clienteSelecionado.getPessoa().setEmpresa(new Empresa().buscarPessoaPorNome(comboBoxEmpresa.getValue()));
                // Verifica se a nova senha foi fornecida e a atualiza
                if (!senhaField.getText().isEmpty()) {
                    clienteSelecionado.setPassword(senhaHash);
                }

                // Atualiza a tabela
                Pessoa pessoa = clienteSelecionado.getPessoa();
                dao.atualizar(pessoa);
                dao.atualizar(clienteSelecionado);
                atualizarTabela();


            }
        } else {
            // Nenhum cliente selecionado para edição
            alertaSelecaoCliente();
        }
    }

    private void excluirCliente() {
        Usuario clienteSelecionado = (Usuario) tabelaClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            // Simula uma operação de exclusão (substitua com sua lógica real)
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Excluir Cliente");
            alert.setHeaderText(null);
            alert.setContentText("Deseja realmente excluir o cliente: " + clienteSelecionado.getPessoa().getNome() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                listaClientes.remove(clienteSelecionado);
                dao.apagar(clienteSelecionado);
                // Atualiza a tabela
                atualizarTabela();
            }
        } else {
            // Nenhum cliente selecionado para exclusão
            alertaSelecaoCliente();
        }
    }

    private void visualizarInformacoesCliente() {
        Usuario clienteSelecionado = new Usuario();

        if (clienteSelecionado != null) {
            // Exibe as informações do cliente em uma janela de diálogo
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informações do Cliente");
            alert.setHeaderText(null);
            alert.setContentText(clienteSelecionado.getPessoa().getNome());
            alert.showAndWait();
        } else {
            // Nenhum cliente selecionado para visualização de informações
            alertaSelecaoCliente();
        }
    }

    private void alertaSelecaoCliente() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText("Selecione um cliente da tabela.");
        alert.showAndWait();
    }

    public void adicionarNovoCliente(Usuario admin) {
        if (tabelaClientes == null) {
            inicializarTabelaClientes();
        }

        telaCriarNovoUsuario.start(new Stage(), admin);
        atualizarTabela();
    }

    private void atualizarTabela() {
        tabelaClientes.getItems().clear();
        ObservableList<Object> lista = FXCollections.observableArrayList(new  Usuario().recuperarClientes());
        tabelaClientes.getItems().addAll(lista);


    }




}

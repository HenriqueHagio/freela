package com.example.demo.Lubrificantes;

import com.example.demo.Hibernate.Entidade;
import com.example.demo.Hibernate.HibernateEntidade;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class TelaCadastrarLubrificantes extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    Entidade<Object> dao = new HibernateEntidade<>();


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cadastro de Lubrificante");

        // Componentes da tela
        Label lblCodigo = new Label("Código:");
        TextField txtCodigo = new TextField();

        Label lblDescricao = new Label("Descrição:");
        TextField txtDescricao = new TextField();

        Button btnSalvar = new Button("Salvar");

        // Ação do botão "Salvar"
        btnSalvar.setOnAction(e -> {
            int codigo = Integer.parseInt(txtCodigo.getText());
            String descricao = txtDescricao.getText();
            Lubrificante lub = new Lubrificante();
            lub.setCodigo(codigo);
            lub.setDescricao(descricao);
            dao.salvar(lub);
            // Aqui você pode salvar os dados em algum lugar (lista, banco de dados, etc.)
            System.out.println("Lubrificante cadastrado: Código " + codigo + ", Descrição: " + descricao);
        });

        // Layout
        VBox root = new VBox(10);
        root.getChildren().addAll(lblCodigo, txtCodigo, lblDescricao, txtDescricao, btnSalvar);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

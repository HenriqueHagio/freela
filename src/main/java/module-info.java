module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.apache.poi.poi;
    requires annotations;
    requires java.sql;
    requires java.desktop;
    requires com.fasterxml.jackson.core;
    requires org.apache.poi.ooxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires java.persistence;
    requires lombok;
    requires org.hibernate.orm.core;
    requires jbcrypt;
    requires java.mail;



    opens com.example.demo;
    exports com.example.demo.Principal;
    exports com.example.demo.Cadastro;
    exports com.example.demo.Estoque;
    opens com.example.demo.Estoque;
    opens com.example.demo.Cadastro;
    exports com.example.demo.Lubrificantes;
    opens com.example.demo.Lubrificantes;
    exports com.example.demo;
    opens com.example.demo.Principal;
    exports com.example.demo.comeco;
    opens com.example.demo.comeco;
}

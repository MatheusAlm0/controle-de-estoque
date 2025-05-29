module br.com.projetoestoque {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics; // 🔹 Permite acesso ao Stage e Scene
    requires java.sql; // 🔹 Suporte a JDBC
    requires java.desktop;

    opens br.com.projetoestoque.controller to javafx.fxml;
    opens br.com.projetoestoque.main to javafx.fxml; // 🔹 Permite carregar FXML corretamente

    // 🔹 Exports para pacotes usados em outras partes do projeto
    exports br.com.projetoestoque.main;
    exports br.com.projetoestoque.model;
    exports br.com.projetoestoque.dao;
}
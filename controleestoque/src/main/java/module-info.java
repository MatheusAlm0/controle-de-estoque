module br.com.projetoestoque {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // 🔹 Adicionando suporte a JDBC

    opens br.com.projetoestoque.controller to javafx.fxml;
    
    // 🔹 Adicione exports para os pacotes onde você usa classes de banco de dados
    exports br.com.projetoestoque.main;
    exports br.com.projetoestoque.model;
    exports br.com.projetoestoque.dao;
}

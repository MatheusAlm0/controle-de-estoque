package br.com.projetoestoque.main;

import br.com.projetoestoque.util.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            DatabaseInitializer.initialize();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Falha ao inicializar o banco de dados!");
        }

        primaryStage = stage;
        scene = new Scene(loadFXML("view/login"));

        stage.setScene(scene);
        stage.setTitle("Controle de Estoque");
        stage.setFullScreen(true); 
        stage.setFullScreenExitHint(""); 
        stage.show();
    }

   private void showAlert(String title, String message) {
     Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        primaryStage.setFullScreen(true); 
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/br/com/projetoestoque/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}

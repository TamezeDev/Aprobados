package org.zeki.aprobados;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Aprobados!");
        stage.setScene(scene);
        stage.show();
    }

    public class Launcher {
        public static void main(String[] args) {
            Application.launch(Main.class, args);
        }
    }
}

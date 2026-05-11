package org.zeki.aprobados;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.zeki.aprobados.helper.PathHelper;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(PathHelper.START_VIEW));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Aprobados!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResource(PathHelper.ICON_IMG)).openStream()));
        stage.show();
    }

    public class Launcher {
        public static void main(String[] args) {
            Application.launch(Main.class, args);
        }
    }
}

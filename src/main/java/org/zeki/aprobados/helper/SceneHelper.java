package org.zeki.aprobados.helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.zeki.aprobados.Main;
import org.zeki.aprobados.exception.CambioEscenaException;

import java.io.IOException;

public final class SceneHelper {

    public static void changeScene(Node node, String url) throws CambioEscenaException {

        try {
            // Get view and set on Stage
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(url));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) node.getScene().getWindow();
            // Keep user size screen
            setWindowSize(stage);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new CambioEscenaException("Error en la ruta de la escena");
        }
    }

    private  static void setWindowSize(Stage stage){

        if (stage.isMaximized()) stage.setMaximized(true);
        else {
            double height = stage.getHeight();
            double width = stage.getWidth();

            stage.setWidth(width);
            stage.setHeight(height);
        }
    }
}

package org.zeki.aprobados.helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.zeki.aprobados.Main;
import org.zeki.aprobados.exception.CambioEscenaException;

import java.io.IOException;

public class SceneHelper {

    public static void cambiarEscena(Node node, String url) throws CambioEscenaException {

        try {
            // Obtener la vista y cargarla en la ventana
            FXMLLoader loader =  new FXMLLoader(Main.class.getResource(url));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new CambioEscenaException("Error en la ruta de la escena");
        }
    }
}

package org.zeki.aprobados.controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.awt.*;
import java.nio.file.Path;

public class AlertController {

    public boolean choiceAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);

        return alert.showAndWait().get() == ButtonType.OK;
    }

    public void messageAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    public void showUpdateDialog(String message, Path downloadedFile) {
        // SHOW INFO TH UPDATE APP
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Actualización requerida");
        alert.setHeaderText("Tu versión está desactualizada");
        alert.setContentText(message + "\n\nDescargado en: " + downloadedFile.toAbsolutePath());

        ButtonType openFolderBtn = new ButtonType("Abrir carpeta de descarga");
        ButtonType exitBtn       = new ButtonType("Salir", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(openFolderBtn, exitBtn);

        alert.showAndWait().ifPresent(btn -> {
            if (btn == openFolderBtn) {
                try {
                    Desktop.getDesktop().open(downloadedFile.getParent().toFile());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Platform.exit();
        });
    }
}

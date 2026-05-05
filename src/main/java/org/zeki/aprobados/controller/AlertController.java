package org.zeki.aprobados.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertController {

    public boolean choiceAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);

        return alert.showAndWait().get() == ButtonType.OK;
    }
}

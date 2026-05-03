package org.zeki.aprobados.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.zeki.aprobados.helper.SceneHelper;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    @FXML
    private Button accesoBtn;

    @FXML
    private Button registroBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actions();
    }

    private void actions() {

        accesoBtn.setOnAction(event -> SceneHelper.cambiarEscena(accesoBtn, AppController.getInstance().getSCENE_PATH().getLOGIN_VIEW()));

    }

}

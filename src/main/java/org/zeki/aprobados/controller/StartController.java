package org.zeki.aprobados.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.zeki.aprobados.helper.SceneHelper;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private Button registerBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actions();
    }

    private void actions() {

        loginBtn.setOnAction(event -> SceneHelper.changeScene(loginBtn, AppController.getInstance().getSCENE_PATH().getLOGIN_VIEW()));

        registerBtn.setOnAction(event -> SceneHelper.changeScene(registerBtn, AppController.getInstance().getSCENE_PATH().getREGISTER_VIEW()));

    }
}

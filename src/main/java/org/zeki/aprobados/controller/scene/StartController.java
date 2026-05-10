package org.zeki.aprobados.controller.scene;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.zeki.aprobados.helper.SceneHelper;
import org.zeki.aprobados.helper.PathHelper;

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

        loginBtn.setOnAction(_ -> SceneHelper.changeScene(loginBtn, PathHelper.LOGIN_VIEW));

        registerBtn.setOnAction(_ -> SceneHelper.changeScene(registerBtn, PathHelper.REGISTER_VIEW));

    }
}

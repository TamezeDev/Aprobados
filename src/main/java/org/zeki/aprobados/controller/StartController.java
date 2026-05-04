package org.zeki.aprobados.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.zeki.aprobados.helper.SceneHelper;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private Button registerBtn;

    @FXML
    private ImageView mainImgView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initGui();
        actions();
    }

    private void initGui() {
    }

    private void actions() {

        loginBtn.setOnAction(event -> SceneHelper.changeScene(loginBtn, AppController.getInstance().getSCENE_PATH().getLOGIN_VIEW()));

        registerBtn.setOnAction(event -> SceneHelper.changeScene(registerBtn, AppController.getInstance().getSCENE_PATH().getREGISTER_VIEW()));

    }

    private void setResponsiveBackground(){

        HBox parent = (HBox) mainImgView.getParent();
        mainImgView.fitHeightProperty().bind(parent.heightProperty());
        mainImgView.fitWidthProperty().bind(parent.widthProperty());
    }

}

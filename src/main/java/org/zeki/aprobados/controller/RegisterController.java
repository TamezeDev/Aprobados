package org.zeki.aprobados.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.zeki.aprobados.helper.SceneHelper;
import org.zeki.aprobados.model.Study;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private Button clearBtn;

    @FXML
    private TextField emailTxt;

    @FXML
    private Label feedbackLabel;

    @FXML
    private ImageView goBackBtn;

    @FXML
    private TextField lastNameTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private PasswordField passTxt;

    @FXML
    private Button registerBtn;

    @FXML
    private PasswordField repeatPassTxt;

    @FXML
    private CheckBox showPassCb;

    @FXML
    private ComboBox<Study> studyCb;

    @FXML
    private TextField visiblePasswordTxt;

    @FXML
    private TextField visibleRepeatPasswordTxt;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        actions();
    }

    private void actions() {
        registerBtn.setOnAction(event -> {

        });

        goBackBtn.setOnMouseClicked(event -> SceneHelper.changeScene(goBackBtn, AppController.getInstance().getSCENE_PATH().getSTART_VIEW()));

    }
}

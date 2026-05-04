package org.zeki.aprobados.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private ImageView closeSessionBtn;

    @FXML
    private Label remaingQuestionsLabel;

    @FXML
    private Label rightQuestionsLabel;

    @FXML
    private VBox studyBtn;

    @FXML
    private VBox testBtn;

    @FXML
    private Label testCompletedLabel;

    @FXML
    private ImageView userMenuBtn;

    @FXML
    private Label wrongQuestionsLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

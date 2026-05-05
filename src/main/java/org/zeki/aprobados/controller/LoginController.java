package org.zeki.aprobados.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.zeki.aprobados.dto.UserLoginDto;
import org.zeki.aprobados.helper.GuiHelper;
import org.zeki.aprobados.helper.SceneHelper;
import org.zeki.aprobados.service.FormularyService;
import org.zeki.aprobados.service.ResultService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private ImageView goBackBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private TextField emailTxt;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button loginBtn;

    @FXML
    private CheckBox showPassCb;

    @FXML
    private PasswordField passTxt;

    @FXML
    private TextField visiblePasswordTxt;

    // COMPONENTS
    private List<TextField> textFields;

    //SERVICES
    private FormularyService formularyService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void instances() {
        textFields = new ArrayList<>();
        formularyService = new FormularyService();
    }

    private void initGUI() {
        groupTextFields();
        visiblePasswordTxt.textProperty().bindBidirectional(passTxt.textProperty());
    }

    private void actions() {
        loginBtn.setOnAction(event -> gotoMainMenu());

        goBackBtn.setOnMousePressed(event -> SceneHelper.changeScene(goBackBtn, AppController.getInstance().getSCENE_PATH().getSTART_VIEW()));

        clearBtn.setOnAction(event -> GuiHelper.clearFields(textFields));

        showPassCb.selectedProperty().addListener((obs, oldValue, selected) -> exchangeVisibilityText(selected));
    }

    private void groupTextFields() {
        // GROUP ALL FIELDS
        textFields.add(emailTxt);
        textFields.add(passTxt);
        textFields.add(visiblePasswordTxt);
    }

    private void exchangeVisibilityText(boolean selected) {
        // EXCHANGE BETWEEN HIDE OR VISIBLE TXT
        visiblePasswordTxt.setVisible(selected);
        visiblePasswordTxt.setManaged(selected);
        passTxt.setVisible(!selected);
        passTxt.setManaged(!selected);
    }

    private boolean validateFields(String email, String pass) {

        // EMPTY VALUES
        for (TextField textField : textFields) {
            ResultService resultEmpty = formularyService.emptyData(textField.getText());
            if (!resultEmpty.isSuccess()) {
                GuiHelper.showFeedback(feedbackLabel, resultEmpty.getMessage());
                return false;
            }
        }

        // VALIDATE EMAIL AND PASS VALUES
        ResultService result = formularyService.getLoginValidation(email, pass);
        if (!result.isSuccess()) {
            GuiHelper.showFeedback(feedbackLabel, result.getMessage());
            return false;
        }

        return true;
    }

    private void gotoMainMenu() {

        String email = emailTxt.getText();
        String pass = passTxt.getText();

        if (!validateFields(email, pass)) return;
        // CHECK LOGIN AND GET MESSAGE OR GOT TO MAIN MENU
        UserLoginDto loginDto = new UserLoginDto(email, pass);
        ResultService resultService = AppController.getInstance().getServerController().getUserService().login(loginDto);
        if (!resultService.isSuccess()) GuiHelper.showFeedback(feedbackLabel, resultService.getMessage());
        else SceneHelper.changeScene(loginBtn, AppController.getInstance().getSCENE_PATH().getMAIN_MENU_VIEW());
    }
}

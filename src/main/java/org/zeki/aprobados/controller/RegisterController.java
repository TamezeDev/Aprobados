package org.zeki.aprobados.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.zeki.aprobados.dto.UserSignupDto;
import org.zeki.aprobados.helper.GuiHelper;
import org.zeki.aprobados.helper.SceneHelper;
import org.zeki.aprobados.model.Study;
import org.zeki.aprobados.service.FormularyService;
import org.zeki.aprobados.service.ResultService;
import org.zeki.aprobados.service.SupabaseClient;
import org.zeki.aprobados.service.UserService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
        loadStudiesOnCb();
        passTxt.textProperty().bindBidirectional(visiblePasswordTxt.textProperty());
        repeatPassTxt.textProperty().bindBidirectional(visibleRepeatPasswordTxt.textProperty());
    }

    private void actions() {
        registerBtn.setOnAction(event -> signUp());

        goBackBtn.setOnMouseClicked(event -> SceneHelper.changeScene(goBackBtn, AppController.getInstance().getSCENE_PATH().getSTART_VIEW()));

        clearBtn.setOnAction(event -> GuiHelper.clearFields(textFields));

        showPassCb.selectedProperty().addListener((obs, oldValue, selected) -> exchangeVisibilityText(selected));
    }

    private void exchangeVisibilityText(boolean selected) {
        // INTERCHANGE BETWEEN HIDE OR VISIBLE TXT
        visiblePasswordTxt.setVisible(selected);
        visiblePasswordTxt.setManaged(selected);
        visibleRepeatPasswordTxt.setVisible(selected);
        visibleRepeatPasswordTxt.setManaged(selected);
        passTxt.setVisible(!selected);
        passTxt.setManaged(!selected);
        repeatPassTxt.setVisible(!selected);
        repeatPassTxt.setManaged(!selected);
    }

    private void groupTextFields() {
        // GROUP ALL TEXT FIELDS
        textFields.add(nameTxt);
        textFields.add(lastNameTxt);
        textFields.add(emailTxt);
        textFields.add(passTxt);
        textFields.add(repeatPassTxt);
        textFields.add(visiblePasswordTxt);
        textFields.add(visibleRepeatPasswordTxt);

    }

    private void loadStudiesOnCb() {
        // CREATE STUDY FIELDS IN COMBO BOX
        Study[] studies = Study.values();
        for (Study study : studies) {
            studyCb.getItems().add(study);
        }
    }

    private boolean validateFields(String email, String pass1, String pass2, int indexCB) {

        // EMPTY VALUES
        for (TextField textField : textFields) {
            ResultService resultEmpty = formularyService.emptyData(textField.getText());
            if (!resultEmpty.isSuccess()) {
                feedbackLabel.setText(resultEmpty.getMessage());
                GuiHelper.showFeedback(feedbackLabel);
                return false;
            }
        }

        // VALIDATE EMAIL, PASSWORD, STUDY VALUES
        ResultService result = formularyService.getSignUpValidation(email, pass1, pass2, indexCB);
        if (!result.isSuccess()) {
            feedbackLabel.setText(result.getMessage());
            GuiHelper.showFeedback(feedbackLabel);
            return false;
        }
        return true;
    }

    private void signUp() {

        int indexCB = studyCb.getSelectionModel().getSelectedIndex();
        String email = emailTxt.getText();
        String name = nameTxt.getText();
        String lastName = lastNameTxt.getText();
        String pass1 = passTxt.getText();
        String pass2 = repeatPassTxt.getText();

        if (!validateFields(email, pass1, pass2, indexCB)) return;

        UserSignupDto userDto = new UserSignupDto(email, pass1, name, lastName, studyCb.getSelectionModel().getSelectedItem().toString());
        ResultService result = AppController.getInstance().getServerController().getUserService().signUp(userDto);

        if (result.isSuccess()) GuiHelper.clearFields(textFields);
        feedbackLabel.setText(result.getMessage());
        GuiHelper.showFeedback(feedbackLabel);
    }


}

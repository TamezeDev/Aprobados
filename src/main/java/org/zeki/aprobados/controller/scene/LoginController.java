package org.zeki.aprobados.controller.scene;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.zeki.aprobados.app.AppContext;
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

        goBackBtn.setOnMousePressed(event -> SceneHelper.changeScene(goBackBtn, AppContext.getInstance().getSCENE_PATH().getSTART_VIEW()));

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
        // TASK ON NEW THREAD
        Task<ResultService> signUpTask = getResultSignUpTask(loginDto);

        new Thread(signUpTask).start();

    }

    private Task<ResultService> getResultSignUpTask(UserLoginDto userDto) {
        Task<ResultService> loginTask = new Task<>() {
            @Override
            protected ResultService call() throws Exception {
                return AppContext.getInstance().getServerManager().getUserService().login(userDto);
            }
        };

        loginTask.setOnSucceeded(ev -> {
            ResultService result = loginTask.getValue();
            if (!result.isSuccess()) GuiHelper.showFeedback(feedbackLabel, result.getMessage());
            else SceneHelper.changeScene(loginBtn, AppContext.getInstance().getSCENE_PATH().getMAIN_MENU_VIEW());
        });

        loginTask.setOnFailed(ev -> {
            Throwable exception = loginTask.getException();
            GuiHelper.showFeedback(feedbackLabel, exception.getMessage());
        });

        return loginTask;
    }

}


package org.zeki.aprobados.controller.scene;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.zeki.aprobados.app.SessionManager;
import org.zeki.aprobados.helper.PathHelper;
import org.zeki.aprobados.helper.SceneHelper;
import org.zeki.aprobados.model.user.Admin;
import org.zeki.aprobados.service.AlertService;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {

    @FXML
    private ImageView closeSessionBtn;

    @FXML
    private VBox docsBtn;

    @FXML
    private Label feedbackLabel;

    @FXML
    private VBox testBtn;

    @FXML
    private Label userNameLabel;

    // COMPONENTS
    private Admin admin;
    // SERVICES
    private AlertService alertService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        instances();
        initGUI();
        actions();
    }

    private void initGUI() {
        setUserData();
    }

    private void instances() {
        alertService = new AlertService();
        admin = SessionManager.getInstance().getAdmin();
    }

    private void actions() {
        closeSessionBtn.setOnMouseClicked(_ -> closeSession());

        testBtn.setOnMouseClicked(_ ->{

        });

        docsBtn.setOnMouseClicked(_ -> SceneHelper.changeScene(docsBtn, PathHelper.ADMIN_FILES_VIEW));
    }

    private void closeSession() {
        // SHOW CLOSE SESSION ALERT
        if (alertService.showCloseSessionAlert()) {
            SessionManager.getInstance().logOut();
            SceneHelper.changeScene(closeSessionBtn, PathHelper.START_VIEW);
        }
    }

    private void setUserData() {
        userNameLabel.setText(admin.getName() + " " + admin.getLastName());
    }
}

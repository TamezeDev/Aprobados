package org.zeki.aprobados.controller.scene;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.zeki.aprobados.app.AppContext;
import org.zeki.aprobados.helper.SceneHelper;
import org.zeki.aprobados.helper.PathHelper;
import org.zeki.aprobados.service.AlertService;
import org.zeki.aprobados.service.ResultService;
import org.zeki.aprobados.service.VersionService;

import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private Button registerBtn;

    // SERVICES
    private VersionService versionService;
    private AlertService alertService;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGUI();
        actions();
    }

    private void initGUI() {
        checkLastVersion();
    }

    private void instances() {
        versionService = new VersionService();
        alertService = new AlertService();
    }

    private void actions() {

        loginBtn.setOnAction(_ -> SceneHelper.changeScene(loginBtn, PathHelper.LOGIN_VIEW));

        registerBtn.setOnAction(_ -> SceneHelper.changeScene(registerBtn, PathHelper.REGISTER_VIEW));

    }

    private void checkLastVersion() {
        // GET INFO ABOUT LAST VERSION
        Task<ResultService> resultVersionTask = new Task<>() {
            @Override
            protected ResultService call() {
                return versionService.checkVersion(AppContext.getInstance().getAppVersion().getLastVersion());
            }
        };
        // TASK OK
        resultVersionTask.setOnSucceeded(_ -> {
            ResultService result = resultVersionTask.getValue();
            if (!result.isSuccess()) alertService.showUpdateDialog(result.getMessage(), result.getDownloadedVersion());
        });
        new Thread(resultVersionTask).start();
    }
}

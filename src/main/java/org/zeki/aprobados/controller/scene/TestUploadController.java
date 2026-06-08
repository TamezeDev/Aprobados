package org.zeki.aprobados.controller.scene;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.zeki.aprobados.app.SessionManager;
import org.zeki.aprobados.helper.GuiHelper;
import org.zeki.aprobados.helper.PathHelper;
import org.zeki.aprobados.helper.SceneHelper;
import org.zeki.aprobados.model.user.Admin;
import org.zeki.aprobados.service.AlertService;
import org.zeki.aprobados.service.ResultService;
import org.zeki.aprobados.service.TestService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class TestUploadController implements Initializable {

    @FXML
    private Button addFileBtn;

    @FXML
    private Button backBtn;

    @FXML
    private ImageView closeSessionBtn;

    @FXML
    private Label feedbackLabel;

    @FXML
    private Label fileFeedbackLabel;

    @FXML
    private Button sendBtn;

    @FXML
    private Label userNameLabel;

    // COMPONENTS
    private Admin admin;
    private File selectedFile;
    // SERVICES
    private AlertService alertService;
    private TestService testService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initUI();
        initListeners();
    }

    private void instances() {
        alertService = new AlertService();
        admin = SessionManager.getInstance().getAdmin();
        testService = new TestService(); // ← añade esto
    }

    private void initUI() {
        setUserData();
    }

    private void initListeners() {
        closeSessionBtn.setOnMouseClicked(_ -> closeSession());
        backBtn.setOnMouseClicked(_ -> SceneHelper.changeScene(closeSessionBtn, PathHelper.ADMIN_MENU_VIEW));

        addFileBtn.setOnMouseClicked(_ -> loadFiles());

        sendBtn.setOnMouseClicked(_ -> {
            if (selectedFile == null) {
                GuiHelper.showFeedback(feedbackLabel, "Selecciona un archivo JSON primero");
                return;
            }
            uploadTest();
        });

    }

    private void loadFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona un documento");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showOpenDialog(addFileBtn.getScene().getWindow());
        if (file != null) {
            selectedFile = file;
            fileFeedbackLabel.setText("Archivo cargado ok: " + file.getName());
        }
    }

    private void closeSession() {
        // SHOW CLOSE SESSION ALERT
        if (alertService.showCloseSessionAlert()) {
            SessionManager.getInstance().logOut();
            SceneHelper.changeScene(closeSessionBtn, PathHelper.START_VIEW);
        }
    }

    private void clearForm() {
        selectedFile = null;
        fileFeedbackLabel.setText("Sin archivo");
    }

    private void setUserData() {
        userNameLabel.setText(admin.getName() + " " + admin.getLastName());
    }

    // -----------NEW THREADS --------------
    private void uploadTest() {
        Task<ResultService> task = new Task<>() {
            @Override
            protected ResultService call() throws Exception {
                String content = new String(java.nio.file.Files.readAllBytes(selectedFile.toPath()));
                JsonObject testJson = JsonParser.parseString(content).getAsJsonObject();
                return testService.uploadTest(testJson, admin.getJwt());
            }
        };

        task.setOnSucceeded(_ -> {
            ResultService result = task.getValue();
            GuiHelper.showFeedback(feedbackLabel, result.getMessage());
            if (result.isSuccess()) clearForm();
        });

        task.setOnFailed(_ -> GuiHelper.showFeedback(feedbackLabel, task.getException().getMessage()));

        new Thread(task).start();
    }
}

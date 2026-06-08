package org.zeki.aprobados.controller.scene;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.zeki.aprobados.app.AppContext;
import org.zeki.aprobados.app.SessionManager;
import org.zeki.aprobados.helper.GuiHelper;
import org.zeki.aprobados.helper.PathHelper;
import org.zeki.aprobados.helper.SceneHelper;
import org.zeki.aprobados.model.syllabus.FileStudy;
import org.zeki.aprobados.model.test.Topic;
import org.zeki.aprobados.model.user.Admin;
import org.zeki.aprobados.service.AlertService;
import org.zeki.aprobados.service.FormularyService;
import org.zeki.aprobados.service.ResultService;
import org.zeki.aprobados.service.StorageService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class FilesUploadController implements Initializable {
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
    private ComboBox<String> documentTypeCb;

    @FXML
    private Button sendBtn;

    @FXML
    private TextField nameTxt;

    @FXML
    private ComboBox<Topic> topicCb;

    @FXML
    private Label userNameLabel;

    @FXML
    private ComboBox<Integer> yearCb;

    // COMPONENTS
    private Admin admin;
    private File selectedFile;
    // SERVICES
    private AlertService alertService;
    private StorageService storageService;
    private FormularyService formularyService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initUI();
        initListeners();
    }

    private void instances() {
        alertService = new AlertService();
        storageService = new StorageService();
        formularyService = new FormularyService();
        admin = SessionManager.getInstance().getAdmin();
    }

    private void initUI() {
        setUserData();
        loadTypeDocumentOnCb();
        loadYearsOnCb();
    }

    private void initListeners() {
        closeSessionBtn.setOnMouseClicked(_ -> closeSession());
        backBtn.setOnMouseClicked(_ -> SceneHelper.changeScene(closeSessionBtn, PathHelper.ADMIN_MENU_VIEW));

        addFileBtn.setOnMouseClicked(_ -> loadFiles());

        sendBtn.setOnMouseClicked(_ -> {
            ResultService validation = formularyService.getFileUploadValidation(
                    yearCb.getValue(),
                    topicCb.getValue(),
                    nameTxt.getText(),
                    documentTypeCb.getValue(),
                    selectedFile
            );
            if (!validation.isSuccess()) {
                GuiHelper.showFeedback(feedbackLabel, validation.getMessage());
                return;
            }
            uploadDocument();
        });

        yearCb.setOnAction(_ -> {
            if (yearCb.getValue() != null) {
                topicCb.getItems().clear();
                loadTopicsOnCb(yearCb.getValue());
            }
        });
    }

    private void loadFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona un documento");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        File file = fileChooser.showOpenDialog(addFileBtn.getScene().getWindow());
        if (file != null) {
            selectedFile = file;
            fileFeedbackLabel.setText("Archivo cargado ok: " + file.getName());
        }
    }

    private void clearForm() {
        yearCb.setValue(null);
        topicCb.setValue(null);
        documentTypeCb.setValue(null);
        nameTxt.clear();
        selectedFile = null;
        fileFeedbackLabel.setText("Sin archivo");
    }

    private void loadYearsOnCb() {
        // CREATE YEARS FIELDS IN COMBO BOX
        yearCb.getItems().add(1);
        yearCb.getItems().add(2);

    }

    private void loadTypeDocumentOnCb() {
        // CREATE DOCUMENT TYPE FIELDS IN COMBO BOX
        documentTypeCb.getItems().add("Oficial");
        documentTypeCb.getItems().add("Resumen");
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


    // -----------NEW THREADS --------------
    private void loadTopicsOnCb(int year) {
        Task<ResultService> resultModulesTask = new Task<>() {
            @Override
            protected ResultService call() {
                return AppContext.getInstance().getServerManager().moduleService().getModules(year, admin.getJwt());
            }
        };
        resultModulesTask.setOnSucceeded(_ -> {
            ResultService result = resultModulesTask.getValue();
            if (result.isSuccess()) {
                result.getTopics().forEach(topic -> topicCb.getItems().add(topic));
            }
            GuiHelper.showFeedback(feedbackLabel, result.getMessage());
        });
        resultModulesTask.setOnFailed(_ -> GuiHelper.showFeedback(feedbackLabel, resultModulesTask.getException().getMessage()));
        new Thread(resultModulesTask).start();
    }

    private void uploadDocument() {
        int year = yearCb.getValue();
        int idModulo = topicCb.getValue().getIdTopic();
        String name = nameTxt.getText().trim();
        Task<ResultService> uploadTask = getResultServiceTask(idModulo, year, name);

        uploadTask.setOnSucceeded(_ -> {
            ResultService result = uploadTask.getValue();
            GuiHelper.showFeedback(feedbackLabel, result.getMessage());
            if (result.isSuccess()) clearForm();
        });

        uploadTask.setOnFailed(_ -> GuiHelper.showFeedback(feedbackLabel, uploadTask.getException().getMessage()));

        new Thread(uploadTask).start();
    }

    private Task<ResultService> getResultServiceTask(int idModulo, int year, String name) {
        String nombreModulo = topicCb.getValue().getNameTopic();
        boolean esOficial = documentTypeCb.getValue().equals("Oficial");

        FileStudy fileStudy = new FileStudy(0, idModulo, nombreModulo, name, "", year, esOficial);

        return new Task<>() {
            @Override
            protected ResultService call() {
                ResultService url = storageService.uploadFile(selectedFile, fileStudy, admin.getJwt());
                FileStudy fileStudyWithUrl = new FileStudy(0, idModulo, nombreModulo, name, url.getMessage(), year, esOficial);
                return AppContext.getInstance().getServerManager().moduleService().uploadTemario(fileStudyWithUrl, admin.getJwt());
            }
        };
    }
}

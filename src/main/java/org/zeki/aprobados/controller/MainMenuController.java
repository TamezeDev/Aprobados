package org.zeki.aprobados.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.zeki.aprobados.app.AppContext;
import org.zeki.aprobados.app.SessionManager;
import org.zeki.aprobados.helper.GuiHelper;
import org.zeki.aprobados.helper.SceneHelper;
import org.zeki.aprobados.model.test.Topic;
import org.zeki.aprobados.model.user.Student;
import org.zeki.aprobados.service.AlertService;
import org.zeki.aprobados.service.ResultService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private ImageView closeSessionBtn;

    @FXML
    private Label remaingQuestionsLabel;

    @FXML
    private Label rightQuestionsLabel;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label feedbackLabel;

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

    @FXML
    private FlowPane containerPane;

    // COMPONENTS
    private Student student;
    private List<VBox> lastCards;
    private List<Topic> topics;
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
        student = (Student) SessionManager.getInstance().getCurrentUser();
        lastCards = new ArrayList<>();
    }

    private void actions() {

        closeSessionBtn.setOnMouseClicked(event -> closeSession());

        testBtn.setOnMouseClicked(event -> setModuleCards());
    }

    private void closeSession() {
        // SHOW CLOSE SESSION ALERT
        if (alertService.showCloseSessionAlert()) {
            SessionManager.getInstance().logOut();
            SceneHelper.changeScene(closeSessionBtn, AppContext.getInstance().getSCENE_PATH().getSTART_VIEW());
        }
    }

    private void setUserData() {
        // SET LABELS DATA
        userNameLabel.setText(student.getName() + " " + student.getLastName());
        testCompletedLabel.setText(String.valueOf(student.getTestFinished()));
        rightQuestionsLabel.setText(String.valueOf(student.getRightQuestions()));
        wrongQuestionsLabel.setText(String.valueOf(student.getWrongQuestions()));
        remaingQuestionsLabel.setText(String.valueOf(student.getReviewQuestions()));
    }

    private void saveLastCards(FlowPane parent) {
        // SAVE CURRENT CARDS FOR LOAD TO BACK
        lastCards.clear();
        for (int i = 0; i < parent.getChildren().size(); i++) {
            lastCards.add((VBox) parent.getChildren().get(i));
        }
        parent.getChildren().clear();
    }

    private VBox createModuleCard(String module) {
        //NODES
        Label label = new Label(module);
        VBox card = new VBox();
        // STYLES
        label.getStyleClass().add("model-label-M");
        card.getStyleClass().add("card-A");
        // CONFIG
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setPrefHeight(150);
        card.setPadding(new Insets(10,10,10,10));
        card.getChildren().add(label);
        // LISTENER
        createModuleListener(card);
        return card;
    }

    private void createModuleListener(VBox card) {

        card.setOnMouseClicked(event -> {
            System.out.println("Pulsado card " + ((Label) card.getChildren().getFirst()).getText());
        });
    }

    private void setModuleCards() {
        // SET MODULE TASK
        Task<ResultService> resultModulesTask = new Task<>() {
            @Override
            protected ResultService call() throws Exception {
                return AppContext.getInstance().getServerManager().moduleService().getModules();
            }
        };
        // LISTENER OK
        resultModulesTask.setOnSucceeded(ev -> {
            ResultService result = resultModulesTask.getValue();

            if (result.isSuccess()) {
                saveLastCards(containerPane);
                topics = result.getTopics();
                topics.forEach(topic -> {
                    VBox vBox = createModuleCard(topic.getNameTopic());
                    containerPane.getChildren().add(vBox);
                });
            }
            GuiHelper.showFeedback(feedbackLabel, result.getMessage());
        });
        // LISTENER FAIL
        resultModulesTask.setOnFailed(ev -> {
            Throwable exception = resultModulesTask.getException();
            GuiHelper.showFeedback(feedbackLabel, exception.getMessage());
        });

        new Thread(resultModulesTask).start();
    }


}

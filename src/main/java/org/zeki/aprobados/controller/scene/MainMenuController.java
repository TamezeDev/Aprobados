package org.zeki.aprobados.controller.scene;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.zeki.aprobados.app.AppContext;
import org.zeki.aprobados.app.SessionManager;
import org.zeki.aprobados.helper.GuiHelper;
import org.zeki.aprobados.helper.SceneHelper;
import org.zeki.aprobados.model.test.Test;
import org.zeki.aprobados.model.test.Topic;
import org.zeki.aprobados.model.user.Student;
import org.zeki.aprobados.service.AlertService;
import org.zeki.aprobados.service.ResultService;
import org.zeki.aprobados.service.TopicService;

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
    private Label wrongQuestionsLabel;

    @FXML
    private FlowPane containerPane;

    // COMPONENTS
    private Student student;
    private List<VBox> initCards;
    // SERVICES
    private TopicService topicService;
    private AlertService alertService;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        instances();
        initGUI();
        actions();
    }

    private void initGUI() {
        setUserData();
        saveLastCards(containerPane);
    }

    private void instances() {
        alertService = new AlertService();
        topicService = new TopicService();
        student = SessionManager.getInstance().getStudent();
        initCards = new ArrayList<>();
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
        for (int i = 0; i < parent.getChildren().size(); i++) {
            initCards.add((VBox) parent.getChildren().get(i));
        }
    }

    private void createBackListener(VBox card) {
        // IF TOPIC IS SELECTED RETURN TO TOPICS OR MAIN MENU
        card.setOnMouseClicked(ev -> {
            if (topicService.topicIsSelected()) {
                setModuleCards();
                topicService.resetTopicSelected();
            } else loadSavedCards();
        });
    }

    private void loadSavedCards() {
        containerPane.getChildren().clear();
        initCards.forEach(card -> containerPane.getChildren().add(card));
    }

    private void createTestListener(BorderPane card) {

        card.setOnMouseClicked(event -> {
            // GET ID TEST
            String nameTest = ((Label) card.getCenter()).getText();
            ResultService resultIdTest = topicService.getIdSelectedTest(nameTest);
            if (!resultIdTest.isSuccess()) {
                GuiHelper.showFeedback(feedbackLabel, resultIdTest.getMessage());
                return;
            }
            // GET ANSWERS LIST
            ResultService resultTest = AppContext.getInstance().getServerManager().getTestService().getTestById(resultIdTest.getId());
            if (!resultTest.isSuccess()) GuiHelper.showFeedback(feedbackLabel, resultTest.getMessage());
            else
                SceneHelper.changeScene(card, AppContext.getInstance().getSCENE_PATH().getTEST_VIEW(), (TestController controller) -> controller.setCurrentTest(resultTest.getTest()));
        });
    }

    private void createModuleListener(VBox card) {

        card.setOnMouseClicked(event -> {

            ResultService resultService = topicService.getIdModule(((Label) card.getChildren().getFirst()).getText());

            if (!resultService.isSuccess()) GuiHelper.showFeedback(feedbackLabel, resultService.getMessage());
            else {
                int idModule = resultService.getId();
                String jwt = student.getJwt();
                setTestCards(idModule, jwt);
            }
        });
    }

    // -----------NEW THREADS --------------
    private void setTestCards(int idModule, String jwt) {
        // SET TEST CARDS
        Task<ResultService> resultTastTask = new Task<ResultService>() {
            @Override
            protected ResultService call() throws Exception {
                return AppContext.getInstance().getServerManager().moduleService().getTestByModule(idModule, jwt);
            }
        };
        // LISTENER OK
        resultTastTask.setOnSucceeded(ev -> {
            ResultService result = resultTastTask.getValue();

            if (result.isSuccess()) {
                containerPane.getChildren().clear();
                List<Test> tests = result.getTests();
                topicService.setListTest(tests, idModule);
                tests.forEach(test -> {
                    BorderPane card = GuiHelper.createTestCard(test, student.getStudentTest(test.getIdTest()), this::createTestListener);
                    containerPane.getChildren().add(card);
                });
                containerPane.getChildren().add(GuiHelper.createBackCard(this::createBackListener));
                topicService.setTopicSelected(idModule);
            }
            GuiHelper.showFeedback(feedbackLabel, result.getMessage());
        });
        // LISTENER FAIL
        resultTastTask.setOnFailed(ev -> {
            Throwable exception = resultTastTask.getException();
            GuiHelper.showFeedback(feedbackLabel, exception.getMessage());
        });
        new Thread(resultTastTask).start();
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
                containerPane.getChildren().clear();
                List<Topic> topics = result.getTopics();
                topicService.getTopicController().setTopics(topics);
                topics.forEach(topic -> {
                    VBox vBox = GuiHelper.createModuleCard(topic.getNameTopic(), this::createModuleListener);
                    containerPane.getChildren().add(vBox);
                });
                containerPane.getChildren().add(GuiHelper.createBackCard(this::createBackListener));
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

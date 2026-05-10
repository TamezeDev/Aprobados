package org.zeki.aprobados.controller.scene;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.zeki.aprobados.app.AppContext;
import org.zeki.aprobados.app.SessionManager;
import org.zeki.aprobados.dto.ModuleStudyDto;
import org.zeki.aprobados.helper.GuiHelper;
import org.zeki.aprobados.helper.SceneHelper;
import org.zeki.aprobados.model.syllabus.FileStudy;
import org.zeki.aprobados.model.test.Test;
import org.zeki.aprobados.model.test.Topic;
import org.zeki.aprobados.model.user.Student;
import org.zeki.aprobados.helper.PathHelper;
import org.zeki.aprobados.service.AlertService;
import org.zeki.aprobados.service.ResultService;
import org.zeki.aprobados.service.FileStudyService;
import org.zeki.aprobados.service.TopicService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

    @FXML
    private Button reviewWrongBtn;

    // COMPONENTS
    private Student student;
    private List<VBox> initCards;
    // SERVICES
    private TopicService topicService;
    private AlertService alertService;
    private FileStudyService fileStudyService;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        instances();
        initGUI();
        actions();
    }

    private void initGUI() {
        setUserData();
        checkIfHasWrongQuestions();
        saveLastCards(containerPane);
    }

    private void instances() {
        alertService = new AlertService();
        topicService = new TopicService();
        fileStudyService = new FileStudyService();
        student = SessionManager.getInstance().getStudent();
        initCards = new ArrayList<>();
    }

    private void actions() {

        reviewWrongBtn.setOnAction(_ -> setTestQuestions(() -> AppContext.getInstance().getServerManager().getTestService().getFailQuestions(student.getJwt()), reviewWrongBtn));

        closeSessionBtn.setOnMouseClicked(_ -> closeSession());

        testBtn.setOnMouseClicked(_ -> setModuleCards(this::createTestModuleListener, this::createBackTestListener, 1)); // NEXT YEAR CHANGUE TO 2

        studyBtn.setOnMouseClicked(_ -> createYearCard());
    }

    private void checkIfHasWrongQuestions() {
        if (student.hasWrongQuestions()) reviewWrongBtn.setVisible(true);
    }

    private void closeSession() {
        // SHOW CLOSE SESSION ALERT
        if (alertService.showCloseSessionAlert()) {
            SessionManager.getInstance().logOut();
            SceneHelper.changeScene(closeSessionBtn, PathHelper.START_VIEW);
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

    private void loadSavedCards() {
        containerPane.getChildren().clear();
        initCards.forEach(card -> containerPane.getChildren().add(card));
    }

    private void createYearCard() {
        // CREATE STUDY YEAR CARDS
        containerPane.getChildren().clear();
        VBox year1Card = GuiHelper.createStandardCard("1º DAM", this::createFirstYearListener);
        VBox year2Card = GuiHelper.createStandardCard("2º DAM", this::createSecondYearListener);
        VBox backCard = GuiHelper.createBackCard(this::createBackStudyListener);
        containerPane.getChildren().addAll(year1Card, year2Card, backCard);
    }

    private void createStudyTypesCard() {
        // CREATE CATEGORIES
        containerPane.getChildren().clear();
        VBox official = GuiHelper.createStandardCard("Temario profesores", this::createOfficialSyllabusListener);
        VBox resume = GuiHelper.createStandardCard("Resumen exámenes", this::createResumeSyllabusListener);
        VBox backCard = GuiHelper.createBackCard(this::createBackStudyListener);
        containerPane.getChildren().addAll(official, resume, backCard);

    }

    private void createFirstYearListener(VBox vBox) {
        int year = 1;
        topicService.setYearSelected(year);
        setModuleCards(this::createStudyModuleListener, this::createBackStudyListener, year);
    }

    private void createSecondYearListener(VBox vBox) {
        alertService.showNoAvailableAlert();
    }

    private void createOfficialSyllabusListener(VBox vBox) {
        getSelectedSyllabus(topicService.getTopicSelected(), 1, true);
    }

    private void createResumeSyllabusListener(VBox vBox) {
        getSelectedSyllabus(topicService.getTopicSelected(), 1, false);
    }

    private void createBackTestListener(VBox card) {
        // IF TOPIC IS SELECTED RETURN TO TOPICS OR MAIN MENU
        card.setOnMouseClicked(_ -> {
            if (topicService.topicIsSelected()) {
                setModuleCards(this::createTestModuleListener, this::createBackTestListener, topicService.getYearSelected());
                topicService.resetTopicSelected();
            } else loadSavedCards();
        });
    }

    private void createBackStudyListener(VBox card) {
        // IF TOPIC IS SELECTED RETURN TO TOPICS OR MAIN MENU
        card.setOnMouseClicked(_ -> {
            if (topicService.topicIsSelected()) {
                setModuleCards(this::createStudyModuleListener, this::createBackTestListener, topicService.getYearSelected());
                topicService.resetTopicSelected();
            } else loadSavedCards();
        });
    }

    private void createTestListener(BorderPane card) {

        card.setOnMouseClicked(_ -> {
            // GET ID TEST
            String nameTest = ((Label) card.getCenter()).getText();
            ResultService resultIdTest = topicService.getIdSelectedTest(nameTest);
            if (!resultIdTest.isSuccess()) {
                GuiHelper.showFeedback(feedbackLabel, resultIdTest.getMessage());
                return;
            }
            // GET ANSWERS LIST
            setTestQuestions(() -> AppContext.getInstance().getServerManager().getTestService().getTestById(resultIdTest.getId()), card);
        });
    }

    private void createStudyModuleListener(VBox card) {

        ResultService resultService = topicService.getIdModule(((Label) card.getChildren().getFirst()).getText());
        if (!resultService.isSuccess()) GuiHelper.showFeedback(feedbackLabel, resultService.getMessage());
        else {
            createStudyTypesCard();
        }
    }

    private void getSelectedSyllabus(int idModule, int year, boolean official) {
        ModuleStudyDto studyDto = new ModuleStudyDto(idModule, year, official);
        setFileStudiesCards(studyDto, student.getJwt(), idModule);
    }

    private void createOpenFileListener(VBox card) {

        String selectedName = ((Label) card.getChildren().getFirst()).getText();

        fileStudyService.openDocument(selectedName, result -> Platform.runLater(() -> GuiHelper.showFeedback(feedbackLabel, result.getMessage())));
    }

    private void createTestModuleListener(VBox card) {

        ResultService resultService = topicService.getIdModule(((Label) card.getChildren().getFirst()).getText());

        if (!resultService.isSuccess()) GuiHelper.showFeedback(feedbackLabel, resultService.getMessage());
        else {
            int idModule = resultService.getId();
            String jwt = student.getJwt();
            setTestCards(idModule, jwt);
        }
    }

    // -----------NEW THREADS --------------
    private void setTestQuestions(Supplier<ResultService> testQuestions, Node node) {
        // GET TEST WITH FAILED QUESTIONS
        Task<ResultService> resulTestTask = new Task<>() {
            @Override
            protected ResultService call() {
                return testQuestions.get();
            }
        };
        // LISTENER OK
        resulTestTask.setOnSucceeded(_ -> {
            ResultService result = resulTestTask.getValue();
            if (result.isSuccess()) {
                SceneHelper.changeScene(node, PathHelper.TEST_VIEW, (TestController controller) -> controller.setCurrentTest(result.getTest()));
            }
        });
        // LISTENER FAIL
        resulTestTask.setOnFailed(_ -> {
            Throwable exception = resulTestTask.getException();
            GuiHelper.showFeedback(feedbackLabel, exception.getMessage());
        });
        new Thread(resulTestTask).start();
    }

    private void setFileStudiesCards(ModuleStudyDto studyDto, String jwt, int idModule) {
        // SET SYLLABUS CARDS
        Task<ResultService> resultSyllabusTask = new Task<>() {
            @Override
            protected ResultService call() {
                return AppContext.getInstance().getServerManager().moduleService().getContentByModule(studyDto, jwt);
            }
        };
        //LISTENER OK
        resultSyllabusTask.setOnSucceeded(_ -> {
            ResultService result = resultSyllabusTask.getValue();

            if (result.isSuccess()) {
                containerPane.getChildren().clear();
                List<FileStudy> fileStudyList = result.getFileStudyList();
                fileStudyList.forEach(fileStudy -> {
                    VBox card = GuiHelper.createStandardCard(fileStudy.getUnity(), this::createOpenFileListener);
                    containerPane.getChildren().add(card);

                });
                containerPane.getChildren().add(GuiHelper.createBackCard(this::createBackStudyListener));
                topicService.setTopicSelected(idModule);
            }
            fileStudyService.setStudyFiles(result.getFileStudyList());
            GuiHelper.showFeedback(feedbackLabel, result.getMessage());
        });
        // LISTENER FAIL
        resultSyllabusTask.setOnFailed(_ -> {
            Throwable exception = resultSyllabusTask.getException();
            GuiHelper.showFeedback(feedbackLabel, exception.getMessage());
        });
        new Thread(resultSyllabusTask).start();
    }

    private void setTestCards(int idModule, String jwt) {
        // SET TEST CARDS
        Task<ResultService> resultTastTask = new Task<>() {
            @Override
            protected ResultService call() {
                return AppContext.getInstance().getServerManager().moduleService().getTestByModule(idModule, jwt);
            }
        };
        // LISTENER OK
        resultTastTask.setOnSucceeded(_ -> {
            ResultService result = resultTastTask.getValue();

            if (result.isSuccess()) {
                containerPane.getChildren().clear();
                List<Test> tests = result.getTests();
                topicService.setListTest(tests, idModule);
                tests.forEach(test -> {
                    BorderPane card = GuiHelper.createTestCard(test, student.getStudentTest(test.getIdTest()), this::createTestListener);
                    containerPane.getChildren().add(card);
                });
                containerPane.getChildren().add(GuiHelper.createBackCard(this::createBackTestListener));
                topicService.setTopicSelected(idModule);
            }
            GuiHelper.showFeedback(feedbackLabel, result.getMessage());
        });
        // LISTENER FAIL
        resultTastTask.setOnFailed(_ -> {
            Throwable exception = resultTastTask.getException();
            GuiHelper.showFeedback(feedbackLabel, exception.getMessage());
        });
        new Thread(resultTastTask).start();
    }

    private void setModuleCards(Consumer<VBox> cardListener, Consumer<VBox> backListener, int year) {
        // SET MODULE TASK
        Task<ResultService> resultModulesTask = new Task<>() {
            @Override
            protected ResultService call() {
                return AppContext.getInstance().getServerManager().moduleService().getModules(year, student.getJwt());
            }
        };
        // LISTENER OK
        resultModulesTask.setOnSucceeded(_ -> {
            ResultService result = resultModulesTask.getValue();

            if (result.isSuccess()) {
                containerPane.getChildren().clear();
                List<Topic> topics = result.getTopics();
                topicService.getTopicController().setTopics(topics);
                topics.forEach(topic -> {
                    VBox vBox = GuiHelper.createStandardCard(topic.getNameTopic(), cardListener);
                    containerPane.getChildren().add(vBox);
                });
                containerPane.getChildren().add(GuiHelper.createBackCard(backListener));
            }
            GuiHelper.showFeedback(feedbackLabel, result.getMessage());
        });
        // LISTENER FAIL
        resultModulesTask.setOnFailed(_ -> {
            Throwable exception = resultModulesTask.getException();
            GuiHelper.showFeedback(feedbackLabel, exception.getMessage());
        });

        new Thread(resultModulesTask).start();
    }


}

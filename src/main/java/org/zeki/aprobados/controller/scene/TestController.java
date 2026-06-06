package org.zeki.aprobados.controller.scene;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.zeki.aprobados.app.SessionManager;
import org.zeki.aprobados.helper.GuiHelper;
import org.zeki.aprobados.helper.SceneHelper;
import org.zeki.aprobados.model.test.Question;
import org.zeki.aprobados.model.test.Test;
import org.zeki.aprobados.model.user.Student;
import org.zeki.aprobados.helper.PathHelper;
import org.zeki.aprobados.service.AlertService;
import org.zeki.aprobados.service.CurrentTestService;
import org.zeki.aprobados.service.ResultService;

import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable {

    @FXML
    private VBox answer1Btn;

    @FXML
    private VBox answer2Btn;

    @FXML
    private VBox answer3Btn;

    @FXML
    private VBox answer4Btn;

    @FXML
    private Button backBtn;

    @FXML
    private Button lastBtn;

    @FXML
    private Button nextBtn;

    @FXML
    private Label questionLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private FlowPane questionsPane;

    @FXML
    private VBox answersContainer;

    // COMPONENTS
    private Student student;
    private String enviar;
    private String siguiente;
    private String selectedAnswer;
    private String cardA;
    private String cardB;

    // SERVICE
    private CurrentTestService testService;
    private AlertService alertService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGui();
        actions();
    }

    private void initGui() {
        setUserData();
    }

    private void instances() {
        student = SessionManager.getInstance().getStudent();
        questionLabel.setTextAlignment(TextAlignment.CENTER);
        alertService = new AlertService();
        enviar = "Enviar";
        siguiente = "Siguiente";
        selectedAnswer = "selected-answer";
        cardB = "card-B";
        cardA = "card-A";
    }

    private void actions() {
        backBtn.setOnAction(_ -> {
            if (alertService.showCloseTestAlert())
                SceneHelper.changeScene(backBtn, PathHelper.MAIN_MENU_VIEW);
        });

        nextBtn.setOnAction(_ -> checkNextQuestion());

        lastBtn.setOnAction(_ -> checkLastQuestion());
    }

    private void checkSingleQuestion() {
        if (testService.checkSingleQuestion()) nextBtn.setText(enviar);
    }

    private void checkLastQuestion() {
        // SET BUTTON CONTROL AND GET LAST QUESTION
        if (testService.nextFirstQuestion()) {
            lastBtn.setDisable(true);
        } else if (testService.isLastQuestion()) {
            nextBtn.setText(siguiente);
        }

        Question question = testService.getLastQuestion();
        renderQuestion(question);
    }

    private void checkNextQuestion() {
        // SET BUTTON CONTROL AND GET NEXT QUESTION
        if (testService.isFirstQuestion() && !testService.checkSingleQuestion()) {
            lastBtn.setDisable(false);
            if (testService.isDualQuestion()) nextBtn.setText(enviar);
        } else if (testService.nextLastQuestion()) nextBtn.setText(enviar);
        else if (nextBtn.getText().equals(enviar)) {

            if (testService.anyAnswerEmpty()) {
                if (!alertService.showSendFaultTestAlert()) return;

            } else if (!alertService.showSendTestAlert()) return;

            testService.reviewTest();
            SceneHelper.changeScene(nextBtn, PathHelper.REVIEW_TEST_VIEW, (ReviewTestController controller) -> controller.setTestService(testService));
            return;
        }

        Question question = testService.getNextQuestion();
        renderQuestion(question);
    }

    private void checkButtonsForSelectedQuestion(int index) {
        // SET BUTTON CONTROL OVER SELECTED QUESTION
        if (testService.selectedFirstQuestion(index)) {
            if (testService.checkSingleQuestion()) nextBtn.setText(enviar);
            else nextBtn.setText(siguiente);
            lastBtn.setDisable(true);

        } else if (testService.selectedLastQuestion(index)) {
            lastBtn.setDisable(false);
            nextBtn.setText(enviar);
        } else {
            lastBtn.setDisable(false);
            nextBtn.setText(siguiente);
        }
    }

    private void setQuestionCards() {
        int sizeQuestion = testService.getQuestionsLength();
        for (int i = 1; i <= sizeQuestion; i++) {
            questionsPane.getChildren().add(GuiHelper.createTestCard(i, this::createTestListener));
        }
    }

    private void createAnswerListener() {
        // SET SELECTED ANSWER
        answersContainer.getChildren().forEach(item -> item.setOnMouseClicked(_ -> {
            // EVENT FOR SELECTED ANSWER
            resetDefaultStyles();
            item.getStyleClass().remove(cardA);
            item.getStyleClass().addAll(cardB, selectedAnswer);
            questionsPane.getChildren().get(testService.getSelectedIndexQuestion()).getStyleClass().add(selectedAnswer);

            int answerIndex = answersContainer.getChildren().indexOf(item);
            testService.setAsSelectedAnswer(answerIndex);
        }));
    }

    private void createTestListener(VBox card) {
        card.setOnMouseClicked(_ -> {

            int index = Integer.parseInt(((Label) card.getChildren().getFirst()).getText()) - 1;
            Question question = testService.getQuestionByIndex(index);
            if (question != null) {
                checkButtonsForSelectedQuestion(index);
                renderQuestion(question);
                testService.setSelectedQuestion(question.getIdQuestion(), index);
            }
        });
    }

    public void setCurrentTest(Test test) {
        // INIT TEST SERVICE AND SET CURRENT TEST ON VIEW
        testService = new CurrentTestService();
        testService.setSelectedTest(test);
        setQuestionCards();
        createAnswerListener();
        checkSingleQuestion();
        Question question1 = test.getQuestions().getFirst();
        renderQuestion(question1);
    }

    private void resetDefaultStyles() {
        answersContainer.getChildren().forEach(node -> {
            node.getStyleClass().removeAll(selectedAnswer, cardB, cardA);
            node.getStyleClass().add(cardA);
        });
    }

    private void renderQuestion(Question question) {

        resetDefaultStyles();
        questionLabel.setText(question.getText());
        ((Label) answer1Btn.getChildren().getFirst()).setText(question.getAnswers().get(0).getText());
        ((Label) answer2Btn.getChildren().getFirst()).setText(question.getAnswers().get(1).getText());
        ((Label) answer3Btn.getChildren().getFirst()).setText(question.getAnswers().get(2).getText());
        ((Label) answer4Btn.getChildren().getFirst()).setText(question.getAnswers().get(3).getText());

        ResultService result = testService.answerSelected(question.getIdQuestion());
        if (result.isSuccess()) {
            answersContainer.getChildren().get(result.getId()).getStyleClass().remove(cardA);
            answersContainer.getChildren().get(result.getId()).getStyleClass().addAll(cardB, selectedAnswer);
        }
    }

    private void setUserData() {
        // SET LABELS DATA
        nameLabel.setText(student.getName() + " " + student.getLastName());
    }

}


package org.zeki.aprobados.controller.scene;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.zeki.aprobados.model.test.Question;
import org.zeki.aprobados.model.test.Test;
import org.zeki.aprobados.model.user.StudentAnswerTest;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
    private ImageView closeSessionBtn;

    @FXML
    private Button lastBtn;

    @FXML
    private Button nextBtn;

    @FXML
    private Label quertionLabel;

    @FXML
    private FlowPane questionsPane;

    @FXML
    private ImageView userMenuBtn;

    @FXML
    private VBox answersContainer;

    // COMPONENTS
    private Test currentTest;
    private List<StudentAnswerTest> answerTests;
    private int currentQuestion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instances();
        initGui();
        actions();
    }

    private void initGui() {
        setQuestionCards();
    }

    private void instances() {

    }

    private void actions() {

    }

    private void setQuestionCards() {
        currentTest.getQuestions().forEach(question -> createTestCard(question.getIdQuestion(), this::createTestListener));
    }

    private VBox createTestCard(int question, Consumer<VBox> createListener) {
        // NODES
        Label label = new Label(String.valueOf(question));
        VBox card = new VBox(label);
        // STYLES
        label.getStyleClass().add("model-label-M");
        card.getStyleClass().add("card-A");
        // CONFIG
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(60);
        card.setPrefHeight(60);
        card.setPadding(new Insets(10, 10, 10, 10));
        // LISTENER
        createListener.accept(card);
        return card;

    }

    private void createTestListener(VBox card) {
        card.setOnMouseClicked(ev -> {

            int id = Integer.parseInt(((Label) card.getChildren().getFirst()).getText());
            Question question = currentTest.getQuestionByID(id);
            if (question != null) renderQuestion(question);
        });
    }

    private void initAnswersTest() {
        // INIT LIST STUDENT ANSWERS
        answerTests = new ArrayList<>();

        currentTest.getQuestions().forEach(question -> {

            StudentAnswerTest studentAnswerTest = new StudentAnswerTest(question.getIdQuestion());
            studentAnswerTest.setSelectedAnswer(-1);
            answerTests.add(studentAnswerTest);
        });
    }

    public void setCurrentTest(Test test) {
        // SET CURRENT TEST ON VIEW
        currentTest = test;
        initAnswersTest();
        Question question1 = test.getQuestions().getFirst();
        renderQuestion(question1);
    }

    private void renderQuestion(Question question) {

        quertionLabel.setText(question.getText());
        ((Label) answer1Btn.getChildren().getFirst()).setText(question.getAnswers().get(0).getText());
        ((Label) answer2Btn.getChildren().getFirst()).setText(question.getAnswers().get(1).getText());
        ((Label) answer3Btn.getChildren().getFirst()).setText(question.getAnswers().get(2).getText());
        ((Label) answer4Btn.getChildren().getFirst()).setText(question.getAnswers().get(3).getText());
        answerTests.forEach(answer -> {

            answersContainer.getChildren().get(answer.getSelectedAnswer()).getStyleClass().remove("selected-answer");
            if (answer.getSelectedAnswer() != -1) {
                answersContainer.getChildren().get(answer.getSelectedAnswer()).getStyleClass().add("selected-answer");
            }
        });
    }
}

package org.zeki.aprobados.controller.scene;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.zeki.aprobados.app.AppContext;
import org.zeki.aprobados.app.SessionManager;
import org.zeki.aprobados.helper.GuiHelper;
import org.zeki.aprobados.helper.SceneHelper;
import org.zeki.aprobados.model.test.Answer;
import org.zeki.aprobados.model.test.Question;
import org.zeki.aprobados.model.user.AnswerTest;
import org.zeki.aprobados.model.user.Student;
import org.zeki.aprobados.service.CurrentTestService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReviewTestController implements Initializable {

    @FXML
    private Button backBtn;

    @FXML
    private FlowPane containerPane;

    @FXML
    private Label nameLabel;

    @FXML
    private FlowPane questionsPane;

    @FXML
    private ScrollPane scrollPane;

    // COMPONENTS
    private Student student;
    private String rightAnswer;
    private String wrongAnswer;
    private String selectedReviewed;
    // SERVICE
    private CurrentTestService testService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        instances();
        onActions();
    }

    private void instances() {
        student = SessionManager.getInstance().getStudent();
        rightAnswer = "right-answer";
        wrongAnswer = "wrong-answer";
        selectedReviewed = "selected-reviewed";
    }

    private void initGUI() {
        setUserData();
        setQuestionsCardsReviewed();
        setResultCards();
    }

    private void onActions() {
        backBtn.setOnAction(event -> SceneHelper.changeScene(backBtn, AppContext.getInstance().getSCENE_PATH().getMAIN_MENU_VIEW()));
    }

    private void setQuestionsCardsReviewed() {
        questionsPane.getChildren().clear();
        List<AnswerTest> answersReviewed = testService.getAnswerTest();
        for (int i = 1; i <= answersReviewed.size(); i++) {

            VBox card = GuiHelper.createTestCard(i, this::createReviewedCardListener);
            if (answersReviewed.get(i - 1).isWrong()) card.getStyleClass().add(wrongAnswer);
            else card.getStyleClass().add(rightAnswer);
            questionsPane.getChildren().add(card);
        }
    }

    private void setResultCards() {
        List<AnswerTest> answersReviewed = testService.getAnswerTest();
        List<Question> testQuestions = testService.getAllQuestions();
        for (int i = 0; i < testQuestions.size(); i++) {

            Question question = testQuestions.get(i);
            AnswerTest answerReviewed = answersReviewed.get(i);

            Answer right = question.getAnswers().stream().filter(Answer::isRight).findFirst().orElse(null);
            Answer selected = answerReviewed.getSelectedAnswer() != -1 ? question.getAnswers().get(answerReviewed.getSelectedAnswer()) : null;

            containerPane.getChildren().add(GuiHelper.createResultCard(question, selected, right, answerReviewed.isWrong()));

        }

    }

    private void createReviewedCardListener(VBox card) {
        card.setOnMouseClicked(ev -> {
            // GET INDEX ADD SET SCROLL TO ITEM SELECTED
            int index = Integer.parseInt(((Label) card.getChildren().getFirst()).getText()) - 1;
            Node resultCard = containerPane.getChildren().get(index);
            double height = containerPane.getBoundsInLocal().getHeight();
            double y = resultCard.getBoundsInParent().getMinY();

            resetReviewedStyles();
            resultCard.getStyleClass().add(selectedReviewed);

            Timeline timeline = new Timeline();

            KeyValue kv = new KeyValue(
                    scrollPane.vvalueProperty(),
                    y / height,
                    Interpolator.EASE_BOTH
            );

            KeyFrame kf = new KeyFrame(Duration.millis(400), kv);

            timeline.getKeyFrames().add(kf);
            timeline.play();
        });
    }

    private void resetReviewedStyles() {
        containerPane.getChildren().forEach(node -> {
            node.getStyleClass().remove(selectedReviewed);
        });
    }

    public void setTestService(CurrentTestService testService) {
        this.testService = testService;
        initGUI();
    }

    private void setUserData() {
        // SET LABELS DATA
        nameLabel.setText(student.getName() + " " + student.getLastName());
    }

}

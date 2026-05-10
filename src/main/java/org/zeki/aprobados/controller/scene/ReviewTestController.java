package org.zeki.aprobados.controller.scene;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.zeki.aprobados.app.AppContext;
import org.zeki.aprobados.app.SessionManager;
import org.zeki.aprobados.helper.GuiHelper;
import org.zeki.aprobados.helper.SceneHelper;
import org.zeki.aprobados.model.test.Answer;
import org.zeki.aprobados.model.test.Question;
import org.zeki.aprobados.model.user.AnswerTest;
import org.zeki.aprobados.model.user.Student;
import org.zeki.aprobados.helper.PathHelper;
import org.zeki.aprobados.service.CurrentTestService;
import org.zeki.aprobados.service.ResultService;

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

    @FXML
    private Label feedbackLabel;

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
        showFeedBackResult();
    }

    private void onActions() {
        backBtn.setOnAction(_ -> SceneHelper.changeScene(backBtn, PathHelper.MAIN_MENU_VIEW));
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
        card.setOnMouseClicked(_ -> {
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
        containerPane.getChildren().forEach(node -> node.getStyleClass().remove(selectedReviewed));
    }

    public void setTestService(CurrentTestService testService) {
        this.testService = testService;
        initGUI();
        sendTestToDB();
    }

    private void setUserData() {
        // SET LABELS DATA
        nameLabel.setText(student.getName() + ": ");
    }

    private void showFeedBackResult() {

        int size = testService.getQuestionsLength();
        int wrongs = testService.getTotalWrongAnswers();

        feedbackLabel.setText("Tienes " + (size - wrongs) + " aciertos de " + size + " preguntas");
    }

    // -----------NEW THREADS --------------
    private void sendTestToDB() {

        Task<ResultService> resultSendTestDB = new Task<>() {
            @Override
            protected ResultService call() {
                return testService.sendTestDataToDB();
            }
        };
        // LISTENER OK
        resultSendTestDB.setOnSucceeded(_ -> updateStudentStatist());

        new Thread(resultSendTestDB).start();
    }

    private void updateStudentStatist() {
        Task<ResultService> resultStatistTask = new Task<>() {
            @Override
            protected ResultService call() {
                return AppContext.getInstance().getServerManager().getUserService().uploadStudentStatist(student.getJwt());
            }
        };
        new Thread(resultStatistTask).start();
    }


}

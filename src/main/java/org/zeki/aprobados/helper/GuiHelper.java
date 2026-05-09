package org.zeki.aprobados.helper;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.zeki.aprobados.Main;
import org.zeki.aprobados.model.test.Answer;
import org.zeki.aprobados.model.test.Question;
import org.zeki.aprobados.model.test.Test;
import org.zeki.aprobados.model.user.StudentTest;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class GuiHelper {

    private static PauseTransition feedBackTransition = null;
    public static final String MODEL_LABEL_S = "model-label-S";
    public static final String MODEL_LABEL_M = "model-label-M";
    public static final String CARD_A = "card-A";
    public static final String CARD_B = "card-B";
    public static final String WRONG_ANSWER = "wrong-answer";
    public static final String RIGHT_ANSWER = "right-answer";
    public static final String CARD_BACK = "card-back";

    public static void clearFields(List<TextField> textFields) {
        textFields.forEach(TextInputControl::clear);
    }

    public static void showFeedback(Label label, String text) {
        label.setText(text);
        if (feedBackTransition != null) {
            feedBackTransition.stop();
        }
        label.setVisible(true);
        feedBackTransition = new PauseTransition(Duration.seconds(3));
        feedBackTransition.setOnFinished((event) -> {
            label.setVisible(false);
            feedBackTransition = null;
        });
        feedBackTransition.play();
    }

    public static BorderPane createResultCard(Question question, Answer selected, Answer right, boolean wrong) {
        // NODES
        Label questionName = new Label(question.getText());
        Label selectedQuestion = new Label();
        if (selected != null) selectedQuestion.setText("Seleccionada: " + selected.getText());
        else selectedQuestion.setText("Seleccionada: - ");
        Label rightQuestion = new Label("Correcta: " + right.getText());
        Label explain = new Label(question.getExplainText());
        VBox centerBox = new VBox(selectedQuestion, rightQuestion);
        BorderPane card = new BorderPane();
        card.setTop(questionName);
        card.setCenter(centerBox);
        card.setBottom(explain);
        // STYLES
        card.getStyleClass().add(CARD_B);
        questionName.getStyleClass().add(MODEL_LABEL_M);
        selectedQuestion.getStyleClass().add(MODEL_LABEL_S);
        rightQuestion.getStyleClass().add(MODEL_LABEL_S);
        explain.getStyleClass().add(MODEL_LABEL_S);
        if (wrong) card.getStyleClass().add(WRONG_ANSWER);
        else card.getStyleClass().add(RIGHT_ANSWER);
        // CONFIG
        questionName.setTextAlignment(TextAlignment.CENTER);
        explain.setTextAlignment(TextAlignment.CENTER);
        centerBox.setAlignment(Pos.CENTER);
        questionName.setTextAlignment(TextAlignment.CENTER);
        selectedQuestion.setTextAlignment(TextAlignment.CENTER);
        rightQuestion.setTextAlignment(TextAlignment.CENTER);
        explain.setTextAlignment(TextAlignment.CENTER);

        questionName.setWrapText(true);
        selectedQuestion.setWrapText(true);
        rightQuestion.setWrapText(true);
        explain.setWrapText(true);

        centerBox.setSpacing(10);
        card.setPadding(new Insets(10, 10, 10, 10));
        card.setPrefWidth(300);
        card.setPrefHeight(350);

        return card;
    }

    public static VBox createTestCard(int question, Consumer<VBox> createListener) {
        // NODES
        Label label = new Label(String.valueOf(question));
        VBox card = new VBox(label);
        // STYLES
        label.getStyleClass().add(MODEL_LABEL_M);
        card.getStyleClass().add(CARD_A);
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

    public static VBox createStandardCard(String module, Consumer<VBox> createListener) {
        // NODES
        Label label = new Label(module);
        VBox card = new VBox(label);
        // STYLES
        label.getStyleClass().add(MODEL_LABEL_M);
        card.getStyleClass().add(CARD_A);
        // CONFIG
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setPrefHeight(150);
        card.setPadding(new Insets(10, 10, 10, 10));
        // LISTENER
        card.setOnMouseClicked(event -> createListener.accept(card));

        return card;
    }

    public static VBox createBackCard(Consumer<VBox> createListener) {
        // NODES
        Label label = new Label("Volver");
        ImageView img = new ImageView(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("img/goback.png"))));
        VBox card = new VBox(img, label);
        // STYLES
        label.getStyleClass().add(MODEL_LABEL_M);
        card.getStyleClass().addAll(CARD_A, CARD_BACK);
        // CONFIG
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setPrefHeight(150);
        card.setPadding(new Insets(10, 10, 10, 10));
        card.setSpacing(20);
        img.setFitWidth(80);
        img.setFitHeight(80);

        createListener.accept(card);

        return card;
    }

    public static BorderPane createTestCard(Test test, StudentTest studentTest, Consumer<BorderPane> createListener) {
        // NODES
        Label testName = new Label(test.getNameTest());
        Label lastTime = new Label();
        VBox bottomBox = new VBox(lastTime);
        BorderPane card = new BorderPane();
        card.setCenter(testName);
        card.setBottom(bottomBox);
        // STYLES
        card.getStyleClass().add(CARD_A);
        testName.getStyleClass().add(MODEL_LABEL_M);
        lastTime.getStyleClass().add(MODEL_LABEL_S);
        // CONFIG
        testName.setTextAlignment(TextAlignment.CENTER);
        card.setPadding(new Insets(10, 10, 10, 10));
        card.setPrefWidth(200);
        card.setPrefHeight(150);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setSpacing(10);
        // SET USER TEST DATA
        if (studentTest != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
            lastTime.setText("Última vez: " + studentTest.getDate().format(formatter));
            Label wrong = new Label("Errores: " + studentTest.getErrors());
            Label note = new Label(String.format("Nota: %.1f", studentTest.getNote()));
            // STYLES
            note.getStyleClass().add(MODEL_LABEL_S);
            wrong.getStyleClass().add(MODEL_LABEL_S);
            //CONFIG
            wrong.setTextAlignment(TextAlignment.CENTER);
            note.setTextAlignment(TextAlignment.CENTER);
            // ADD NODES
            bottomBox.getChildren().add(wrong);
            bottomBox.getChildren().add(note);
        } else {
            lastTime.setText("Test no realizado");
        }
        createListener.accept(card);
        return card;
    }
}

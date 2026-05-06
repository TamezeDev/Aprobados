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
import org.zeki.aprobados.model.test.Test;
import org.zeki.aprobados.model.user.StudentTest;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class GuiHelper {

    private static PauseTransition feedBackTransition = null;

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

    public static VBox createModuleCard(String module, Consumer<VBox> createListener) {
        // NODES
        Label label = new Label(module);
        VBox card = new VBox(label);
        // STYLES
        label.getStyleClass().add("model-label-M");
        card.getStyleClass().add("card-A");
        // CONFIG
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setPrefHeight(150);
        card.setPadding(new Insets(10, 10, 10, 10));
        // LISTENER
        createListener.accept(card);
        return card;
    }

    public static VBox createBackCard(Consumer<VBox> createListener) {
        // NODES
        Label label = new Label("Volver");
        ImageView img = new ImageView(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("img/goback.png"))));
        VBox card = new VBox(img, label);
        // STYLES
        label.getStyleClass().add("model-label-M");
        card.getStyleClass().addAll("card-A", "card-back");
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
        card.getStyleClass().add("card-A");
        testName.getStyleClass().add("model-label-M");
        lastTime.getStyleClass().add("model-label-S");
        // CONFIG
        testName.setTextAlignment(TextAlignment.CENTER);
        card.setPadding(new Insets(10, 10, 10, 10));
        card.setPrefWidth(200);
        card.setPrefHeight(150);
        bottomBox.setAlignment(Pos.CENTER);
        // SET USER TEST DATA
        if (studentTest != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
            lastTime.setText("Última vez: " + studentTest.getLastDate().format(formatter));
            Label wrong = new Label("Errores: " + studentTest.getLastErrors());
            Label note = new Label(String.format("Nota: %.1f", studentTest.getLastNote()));
            // STYLES
            note.getStyleClass().add("model-label-S");
            wrong.getStyleClass().add("model-label-S");
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

package org.zeki.aprobados.helper;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.util.Duration;

import java.util.List;

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

}

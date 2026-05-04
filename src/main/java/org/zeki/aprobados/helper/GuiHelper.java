package org.zeki.aprobados.helper;

import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.util.List;

public class GuiHelper {

    public static void clearFields(List<TextField> textFields){
        textFields.forEach(TextInputControl::clear);
    }
}

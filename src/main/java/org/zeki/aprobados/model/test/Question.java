package org.zeki.aprobados.model.test;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public class Question {

    private int idQuestion;
    private String text;
    private String explainText;
    private List<Answer> answers;

    public void makeRandomAnswersOrder() {
        Collections.shuffle(answers);
    }

}

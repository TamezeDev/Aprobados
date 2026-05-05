package org.zeki.aprobados.model.test;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Question {

    private int idQuestion;
    private String text;
    private String explainText;
    private List<Answer> answers;

    public Question() {
        answers = new ArrayList<>();
    }
}

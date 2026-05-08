package org.zeki.aprobados.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerTest {

    private int idQuestion;
    private int selectedAnswer;
    private boolean wrong;

    public AnswerTest(int idQuestion) {
        this.idQuestion = idQuestion;
        selectedAnswer = -1;
    }
}

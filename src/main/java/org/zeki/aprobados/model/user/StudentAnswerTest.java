package org.zeki.aprobados.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentAnswerTest {

    private int idAnswer;
    private int selectedAnswer;
    private boolean wrong;

    public StudentAnswerTest(int idAnswer) {
        this.idAnswer = idAnswer;
    }
}

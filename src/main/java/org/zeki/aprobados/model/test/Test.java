package org.zeki.aprobados.model.test;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Test {

    private int idTest;
    private String nameTest;
    private List<Question> questions;
    private boolean isReviewed;

    public Test() {
        questions = new ArrayList<>();
    }

    public Test(int idTest, String nameTest) {
        this.idTest = idTest;
        this.nameTest = nameTest;
        questions = new ArrayList<>();
    }

    public Question getQuestionByID(int id) {
        return questions.stream().filter(question -> question.getIdQuestion() == id).findFirst().orElse(null);
    }
}

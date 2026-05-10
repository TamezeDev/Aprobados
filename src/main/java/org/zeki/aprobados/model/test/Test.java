package org.zeki.aprobados.model.test;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

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

    public void makeRandomQuestionsOrder(){
        Collections.shuffle(questions);
    }
}

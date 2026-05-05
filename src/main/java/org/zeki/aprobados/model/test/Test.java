package org.zeki.aprobados.model.test;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Test {

    private int idTest;
    private int nameTest;
    private List<Question> questions;

    public Test() {
        questions = new ArrayList<>();
    }
}

package org.zeki.aprobados.model.test;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Topic {

    private final int idTopic;
    private final String nameTopic;
    private List<Test> tests;

    public Topic(int idTopic, String nameTopic) {
        this.nameTopic = nameTopic;
        this.idTopic = idTopic;
        tests = new ArrayList<>();
    }

    public void setTestList(int idTopic, List<Test> tests) {
        if (this.idTopic == idTopic) this.tests = tests;
    }

    public int getIdTestByName(String name){
        return tests.stream().filter(test -> test.getNameTest().equals(name)).findFirst().map(Test::getIdTest).orElse(-1);
    }


}

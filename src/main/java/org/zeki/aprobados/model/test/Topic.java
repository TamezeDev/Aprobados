package org.zeki.aprobados.model.test;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Topic {

    private int idTopic;
    private String nameTopic;
    private List<Test> tests;

    public Topic(int idTopic, String nameTopic) {
        this.nameTopic = nameTopic;
        this.idTopic = idTopic;
        tests = new ArrayList<>();
    }

    public Topic() {
        tests = new ArrayList<>();
    }
}

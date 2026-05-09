package org.zeki.aprobados.service;

import lombok.Getter;
import org.zeki.aprobados.model.syllabus.FileStudy;
import org.zeki.aprobados.model.test.Test;
import org.zeki.aprobados.model.test.Topic;

import java.util.List;

@Getter
public class ResultService {

    private String message;
    private boolean success;
    private int id;
    private List<Topic> topics;
    private List<Test> tests;
    private List<FileStudy> fileStudyList;
    private Test test;

    public ResultService(String message, boolean success, int id) {
        this.message = message;
        this.success = success;
        this.id = id;
    }

    public ResultService(String message, List<Test> tests, boolean success) {
        this.message = message;
        this.success = success;
        this.tests = tests;
    }

    public ResultService(List<FileStudy> fileStudyList, String message, boolean success) {
        this.fileStudyList = fileStudyList;
        this.message = message;
        this.success = success;
    }

    public ResultService(boolean success, int id) {
        this.success = success;
        this.id = id;
    }

    public ResultService(String message, boolean success, Test test) {
        this.message = message;
        this.success = success;
        this.test = test;
    }

    public ResultService(String message, boolean success, List<Topic> topics) {
        this.message = message;
        this.success = success;
        this.topics = topics;
    }

    public ResultService(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}

package org.zeki.aprobados.service;

import lombok.Getter;
import org.zeki.aprobados.model.app.Version;
import org.zeki.aprobados.model.syllabus.FileStudy;
import org.zeki.aprobados.model.test.Test;
import org.zeki.aprobados.model.test.Topic;

import java.nio.file.Path;
import java.util.List;

@Getter
public class ResultService {

    private String message;
    private final boolean success;
    private int id;
    private List<Topic> topics;
    private List<Test> tests;
    private List<FileStudy> fileStudyList;
    private Test test;
    private Version version;
    private Path downloadedVersion;

    public ResultService(String message, boolean success, int id) {
        this.message = message;
        this.success = success;
        this.id = id;
    }

    public ResultService(String message, boolean success, Version version, Path downloadedVersion) {
        this.message = message;
        this.success = success;
        this.version = version;
        this.downloadedVersion = downloadedVersion;
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

package org.zeki.aprobados.service;

import org.zeki.aprobados.controller.FileStudyController;
import org.zeki.aprobados.model.syllabus.FileStudy;

import java.util.List;

public class FileStudyService {

    private FileStudyController studyController;

    public FileStudyService() {
        studyController = new FileStudyController();
    }

    public void setStudyFiles(List<FileStudy> studies) {
        studyController.setFileStudies(studies);
    }
}

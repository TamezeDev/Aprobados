package org.zeki.aprobados.controller;

import lombok.Getter;
import lombok.Setter;
import org.zeki.aprobados.model.syllabus.FileStudy;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FileStudyController {
    private List<FileStudy> fileStudies;

    public FileStudyController() {
        fileStudies = new ArrayList<>();
    }

}

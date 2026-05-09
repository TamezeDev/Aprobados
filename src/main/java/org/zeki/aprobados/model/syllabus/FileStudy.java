package org.zeki.aprobados.model.syllabus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileStudy {

    private int idSyllabus;
    private int idModule;
    private String unity;
    private String url;
    private int studyYear;
    private boolean official;

}

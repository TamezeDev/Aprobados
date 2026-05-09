package org.zeki.aprobados.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentStatistDto {

    private int testFinished;
    private int rightQuestions;
    private int wrongQuestions;
    private int reviewQuestions;
}

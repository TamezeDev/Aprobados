package org.zeki.aprobados.model.user;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class StudentTest {

    private final int idTest;
    private final int errors;
    private final int right;
    private final double note;
    private LocalDate date;
    private final List<AnswerTest> answers;

    public StudentTest(int idTest, int errors, int right, double note, LocalDate date) {
        this.idTest = idTest;
        this.errors = errors;
        this.right = right;
        this.note = note;
        this.date = date;
        answers = new ArrayList<>();
    }

    public StudentTest(int idTest, int errors, int right, double note, List<AnswerTest> answers) {
        this.idTest = idTest;
        this.errors = errors;
        this.right = right;
        this.note = note;
        this.answers = answers;
    }
}

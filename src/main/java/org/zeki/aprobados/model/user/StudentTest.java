package org.zeki.aprobados.model.user;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class StudentTest {

    private String idUser;
    private int idTest;
    private int errors;
    private int right;
    private double note;
    private LocalDate date;
    private List<AnswerTest> answers;

    public StudentTest() {
    }

    public StudentTest(int idTest, int errors, int right, double note, LocalDate date) {
        this.idTest = idTest;
        this.errors = errors;
        this.right = right;
        this.note = note;
        this.date = date;
        answers = new ArrayList<>();
    }

    public StudentTest(String idUser, int idTest, int errors, int right, double note, List<AnswerTest> answers) {
        this.idUser = idUser;
        this.idTest = idTest;
        this.errors = errors;
        this.right = right;
        this.note = note;
        this.answers = answers;
    }
}

package org.zeki.aprobados.model.user;

import lombok.Getter;
import lombok.Setter;
import org.zeki.aprobados.dto.StudentStatistDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class Student extends User {

    private int testFinished;
    private int rightQuestions;
    private int wrongQuestions;
    private int reviewQuestions;
    private List<StudentTest> doneTest;

    public Student(String idUser, String jwt, String name, String lastName) {
        super(idUser, jwt, name, lastName);
        doneTest = new ArrayList<>();
    }

    public void setStudentSettings(int testFinished, int rightQuestions, int wrongQuestions, int reviewQuestions) {
        this.testFinished = testFinished;
        this.rightQuestions = rightQuestions;
        this.wrongQuestions = wrongQuestions;
        this.reviewQuestions = reviewQuestions;
        doneTest = new ArrayList<>();
    }

    public StudentTest getStudentTest(int id) {
        return doneTest.stream().filter(test -> test.getIdTest() == id).findFirst().orElse(null);
    }

    public boolean hasWrongQuestions() {
        return reviewQuestions > 0;
    }

    public void reloadStatist(StudentStatistDto statistDto) {
        testFinished = statistDto.testFinished();
        rightQuestions = statistDto.rightQuestions();
        wrongQuestions = statistDto.wrongQuestions();
        reviewQuestions = statistDto.reviewQuestions();
    }

    @Override
    protected void setRole() {
        role = Role.STUDENT;
    }
}

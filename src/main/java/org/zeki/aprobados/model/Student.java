package org.zeki.aprobados.model;

public final class Student extends User {

    private int testFinished;
    private int rightQuestions;
    private int wrongQuestions;
    private int reviewQuestions;

    public Student() {
    }

    public Student(String idUser, String jwt, String name, String lastName) {
        super(idUser, jwt, name, lastName);
    }

    public void setStudentSettings(int testFinished, int rightQuestions, int wrongQuestions, int reviewQuestions) {
        this.testFinished = testFinished;
        this.rightQuestions = rightQuestions;
        this.wrongQuestions = wrongQuestions;
        this.reviewQuestions = reviewQuestions;
    }

    @Override
    protected void setRole() {
        role = Role.STUDENT;
    }
}

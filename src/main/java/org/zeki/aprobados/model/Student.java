package org.zeki.aprobados.model;

public class Student extends User {

    private int testFinished;
    private int rightQuestions;
    private int wrongQuestions;
    private int reviewQuestions;

    public Student() {
    }

    public Student(int idUser, String name, String lastName) {
        super(idUser, name, lastName);
    }

    @Override
    protected void setRole() {
        role = Role.STUDENT;
    }
}

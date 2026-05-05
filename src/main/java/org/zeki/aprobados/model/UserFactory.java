package org.zeki.aprobados.model;

public class UserFactory {

    public Student createStudent(String idUser, String name, String lastName, String study) {

        Student student = new Student(idUser, name, lastName);
        student.setRole();
        student.setStudy(study);
        return student;
    }

    public Admin createAdmin(String idUser, String name, String lastName, String study) {

        Admin admin = new Admin(idUser, name, lastName);
        admin.setRole();
        admin.setStudy(study);
        return admin;
    }
}

package org.zeki.aprobados.model;

public class UserFactory {

    public Student createStudent(String idUser, String jwt, String name, String lastName, String study) {

        Student student = new Student(idUser, jwt, name, lastName);
        student.setRole();
        student.setStudy(study);
        return student;
    }

    public Admin createAdmin(String idUser, String jwt, String name, String lastName, String study) {

        Admin admin = new Admin(idUser, jwt, name, lastName);
        admin.setRole();
        admin.setStudy(study);
        return admin;
    }
}

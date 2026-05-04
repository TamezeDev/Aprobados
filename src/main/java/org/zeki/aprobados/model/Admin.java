package org.zeki.aprobados.model;

public class Admin extends User {

    public Admin() {
    }

    public Admin(int idUser, String name, String lastName) {
        super(idUser, name, lastName);
    }

    @Override
    protected void setRole() {
        role = Role.ADMIN;
    }
}

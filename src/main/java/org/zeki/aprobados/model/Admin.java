package org.zeki.aprobados.model;

public final class Admin extends User {

    public Admin() {
    }

    public Admin(String idUser, String name, String lastName) {
        super(idUser, name, lastName);
    }

    @Override
    protected void setRole() {
        role = Role.ADMIN;
    }
}

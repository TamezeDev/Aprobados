package org.zeki.aprobados.model.user;

public final class Admin extends User {

    public Admin() {
    }

    public Admin(String idUser, String jwt, String name, String lastName) {
        super(idUser, jwt, name, lastName);
    }

    @Override
    protected void setRole() {
        role = Role.ADMIN;
    }
}

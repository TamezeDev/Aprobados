package org.zeki.aprobados.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class User {

    protected int idUser;
    protected String name;
    protected String lastName;
    protected Study study;
    protected Role role;

    public User() {
    }

    public User(int idUser, String name, String lastName) {
        this.idUser = idUser;
        this.name = name;
        this.lastName = lastName;
    }

    protected abstract void setRole();

    public void setStudy(String study) {
        switch (study) {
            case "DAM" -> this.study = Study.DAM;
            case "DAW" -> this.study = Study.DAW;
            case "ASIR" -> this.study = Study.ASIR;
        }
    }
}

package org.zeki.aprobados.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class User {

    protected String idUser;
    protected String jwt;
    protected String name;
    protected String lastName;
    protected Study study;
    protected Role role;

    protected User() {
    }

    protected User(String idUser, String jwt, String name, String lastName) {
        this.idUser = idUser;
        this.jwt = jwt;
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

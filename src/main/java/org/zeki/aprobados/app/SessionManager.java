package org.zeki.aprobados.app;

import lombok.Getter;
import org.zeki.aprobados.model.user.Admin;
import org.zeki.aprobados.model.user.Student;
import org.zeki.aprobados.model.user.User;

@Getter
public class SessionManager {

    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    public void logOut() {
        currentUser = null;
    }

    public void logIn(User user) {
        this.currentUser = user;
    }

    public Student getStudent() {
        return (Student) currentUser;
    }

    public Admin getAdmin() {
        return (Admin) currentUser;
    }
}

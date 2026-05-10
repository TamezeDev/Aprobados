package org.zeki.aprobados.app;

import lombok.Getter;
import org.zeki.aprobados.model.user.Admin;
import org.zeki.aprobados.model.user.Student;
import org.zeki.aprobados.model.user.User;

@Getter
public class SessionManager {

    private User currentUser;

    private SessionManager() {
    }

    private static class Holder {
        private static final SessionManager INSTANCE = new SessionManager();
    }

    public static SessionManager getInstance() {
        return Holder.INSTANCE;
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

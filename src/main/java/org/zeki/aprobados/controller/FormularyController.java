package org.zeki.aprobados.controller;

import java.util.regex.Pattern;

public class FormularyController {

    public boolean validatePass(String password) {

        // At least length = 8, upper case, lowercase, digit and simbol
        return !Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._#()\\-])[A-Za-z\\d@$!%*?&._#()\\-]{8,}$", password);
    }

    public boolean validateEmail(String email) {
        return Pattern.matches("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,15}$", email.trim());
    }
}

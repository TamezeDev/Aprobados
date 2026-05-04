package org.zeki.aprobados.service;

import org.zeki.aprobados.controller.FormularyController;

public class FormularyService {

    private FormularyController formularyController;

    public FormularyService() {
        formularyController = new FormularyController();
    }

    public ResultService getValidationEmail(String email) {

        if (!formularyController.validateEmail(email))
            return new ResultService("El email no contiene un formato adecuado", false);
        return new ResultService("Email OK", true);
    }

    public ResultService getValidationPassword(String pass) {

        if (!formularyController.validatePass(pass))
            return new ResultService("El password debe contener mayúscula, minúscula, número y carácter especial", false);
        return new ResultService("Pass OK", true);
    }

    public ResultService getMatchesPasswords(String pass1, String pass2) {

        if (!pass1.equals(pass2))
            return new ResultService("Las contraseñas no coinciden", false);
        return new ResultService("Pass OK", true);
    }
}

package org.zeki.aprobados.service;

import org.zeki.aprobados.controller.FormularyController;

public class FormularyService {

    private FormularyController formularyController;

    public FormularyService() {
        formularyController = new FormularyController();
    }

    public ResultService emptyData(String data) {
        if (data.isBlank()) return new ResultService("Debe completar todos los campos", false);
        return new ResultService("Campos rellenos ok", true);
    }

    public ResultService getLoginValidation(String email, String pass) {

        // VALIDATE EMAIL FIELD
        ResultService resultEmail = getValidationEmail(email);
        if (!resultEmail.isSuccess()) return resultEmail;
        // VALIDATE PASS FIELD
        ResultService resultPass = getValidationPassword(pass);
        if (!resultPass.isSuccess()) return resultPass;

        return new ResultService("All ok", true);
    }

    public ResultService getSignUpValidation(String email, String pass1, String pass2, int indexCB) {

        // VALIDATE COMBO
        ResultService resultCB = checkComboValue(indexCB);
        if (!resultCB.isSuccess()) return resultCB;
        // VALIDATE EMAIL FIELD
        ResultService resultEmail = getValidationEmail(email);
        if (!resultEmail.isSuccess()) return resultEmail;
        // VALIDATE SAME PASS
        ResultService resultPassMatch = getMatchesPasswords(pass1, pass2);
        if (!resultPassMatch.isSuccess()) return resultPassMatch;
        // VALIDATE PASS FIELD
        ResultService resultPass = getValidationPassword(pass1);
        if (!resultPass.isSuccess()) return resultPass;

        return new ResultService("All ok", true);
    }

    // ------------ PRIVATE METHODS ---------------

    private ResultService checkComboValue(int indexStudy) {
        if (indexStudy == -1) return new ResultService("Debe seleccionar los estudios que cursa", false);
        return new ResultService("Campos rellenos ok", true);
    }

    private ResultService getMatchesPasswords(String pass1, String pass2) {

        if (!pass1.equals(pass2))
            return new ResultService("Las contraseñas no coinciden", false);
        return new ResultService("Pass OK", true);
    }

    private ResultService getValidationEmail(String email) {

        if (!formularyController.validateEmail(email))
            return new ResultService("El email no contiene un formato adecuado", false);
        return new ResultService("Email OK", true);
    }

    private ResultService getValidationPassword(String pass) {

        if (!formularyController.validatePass(pass))
            return new ResultService("El password debe contener mayúscula, minúscula, número y carácter especial", false);
        return new ResultService("Pass OK", true);
    }
}

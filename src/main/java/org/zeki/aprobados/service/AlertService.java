package org.zeki.aprobados.service;

import org.zeki.aprobados.controller.AlertController;

public class AlertService {

    private AlertController alertController;

    public AlertService() {
        alertController = new AlertController();
    }

    public boolean showCloseSessionAlert() {
        String title = "Cierre de sesión";
        String content = "¿Estás seguro de que quieres cerrar la sesión?";
        return alertController.choiceAlert(title, content);
    }
}

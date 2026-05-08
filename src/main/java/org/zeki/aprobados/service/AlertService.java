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

    public boolean showCloseTestAlert() {
        String title = "Fin de test";
        String content = "¿Estás seguro de que quieres salir sin acabar el test?";
        return alertController.choiceAlert(title, content);
    }

    public boolean showSendTestAlert() {
        String title = "Envío de test";
        String content = "¿Estás seguro de que quieres enviar el test";
        return alertController.choiceAlert(title, content);
    }

    public boolean showSendFaultTestAlert() {
        String title = "Envío de test";
        String content = "¿Tienes preguntas sin responder, estás seguro de que quieres enviar el test?";
        return alertController.choiceAlert(title, content);
    }

}

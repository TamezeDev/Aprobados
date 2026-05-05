package org.zeki.aprobados.controller;

import lombok.Getter;
import org.zeki.aprobados.path.ScenePath;

@Getter
public class AppController {

    private static AppController instance;
    private final ScenePath SCENE_PATH;
    private ServerController serverController;

    private AppController() {
        SCENE_PATH = new ScenePath();
    }

    public ServerController getServerController() {
        if (serverController == null) serverController = new ServerController();
        return serverController;
    }

    public static AppController getInstance() {
        if (instance == null) instance = new AppController();
        return instance;
    }
}

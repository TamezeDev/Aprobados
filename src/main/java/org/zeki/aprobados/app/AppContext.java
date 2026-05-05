package org.zeki.aprobados.app;

import lombok.Getter;
import org.zeki.aprobados.controller.ServerManager;
import org.zeki.aprobados.path.ScenePath;

@Getter
public class AppContext {

    private static AppContext instance;
    private final ScenePath SCENE_PATH;
    private ServerManager serverManager;

    private AppContext() {
        SCENE_PATH = new ScenePath();
    }

    public ServerManager getServerManager() {
        if (serverManager == null) serverManager = new ServerManager();
        return serverManager;
    }

    public static AppContext getInstance() {
        if (instance == null) instance = new AppContext();
        return instance;
    }

}

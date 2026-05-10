package org.zeki.aprobados.app;

import lombok.Getter;

@Getter
public class AppContext {

    private final ServerManager serverManager;

    private AppContext() {
        this.serverManager = new ServerManager();
    }

    private static class Holder {
        private static final AppContext INSTANCE = new AppContext();
    }

    public static AppContext getInstance() {
        return Holder.INSTANCE;
    }

}

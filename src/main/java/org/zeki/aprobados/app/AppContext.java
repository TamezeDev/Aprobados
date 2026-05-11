package org.zeki.aprobados.app;

import lombok.Getter;
import org.zeki.aprobados.model.app.Version;

@Getter
public class AppContext {

    private final ServerManager serverManager;
    private final Version appVersion;

    private AppContext() {
        String startVersion = "1.0.0";
        this.serverManager = new ServerManager();
        appVersion = new Version(startVersion);
    }

    private static class Holder {
        private static final AppContext INSTANCE = new AppContext();
    }

    public static AppContext getInstance() {
        return Holder.INSTANCE;
    }

}

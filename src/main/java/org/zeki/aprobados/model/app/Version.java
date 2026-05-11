package org.zeki.aprobados.model.app;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Version {

    private boolean update;
    private String lastVersion;
    private String urlDownload;
    private String notes;

    public Version(String lastVersion) {
        this.lastVersion = lastVersion;
    }

    public Version(boolean update, String lastVersion, String urlDownload) {
        this.update = update;
        this.lastVersion = lastVersion;
        this.urlDownload = urlDownload;
    }
}

package org.zeki.aprobados.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.zeki.aprobados.exception.SupabaseConnectionException;
import org.zeki.aprobados.model.app.Version;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class VersionService extends SupabaseClient {

    public ResultService checkVersion(String currentVersion) {
        try {
            String url = RPC_URL + "check_version";

            JsonObject body = new JsonObject();
            body.addProperty("p_version", currentVersion);

            HttpResponse<String> response = super.postJsonPublic(url, body.toString());
            JsonObject result = JsonParser.parseString(response.body()).getAsJsonObject();

            Version version = parseVersion(result);
            if (version.isUpdate()) {
                return new ResultService("Versión actualizada", true);
            }

            Path downloadedFile = downloadUpdate(version.getUrlDownload(), version.getLastVersion());
            return new ResultService("Nueva versión disponible: " + version.getLastVersion(), false, version, downloadedFile);

        } catch (Exception _) {
            throw new SupabaseConnectionException("ERROR CONECTANDO CON EL SERVIDOR");
        }
    }

    // ----------- PRIVATE METHODS -------------
    private Path downloadUpdate(String urlDownload, String newVersion) throws Exception {
        Path downloadsDir = Path.of(System.getProperty("user.home"), "Downloads");
        Files.createDirectories(downloadsDir);

        String extension = getExtension(urlDownload);
        Path destFile = downloadsDir.resolve("Aprobados-" + newVersion + extension);

        if (Files.exists(destFile)) {
            return destFile;
        }

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlDownload))
                    .GET()
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofFile(destFile));
            return destFile;
        }
    }

    private String getExtension(String url) {
        if (url.endsWith(".jar")) return ".jar";
        if (url.endsWith(".exe")) return ".exe";
        if (url.endsWith(".zip")) return ".zip";
        return ".jar";
    }

    public Version parseVersion(JsonObject result) {
        String note = "notas";
        // PARSE JSON DATA FROM CURRENT VERSION
        boolean updated = result.get("actualizado").getAsBoolean();
        String lastVersion = result.get("ultima_version").getAsString();
        String urlDownload = result.get("url_descarga").getAsString();
        String notes = result.has(note) && !result.get(note).isJsonNull()
                ? result.get(note).getAsString() : "";
        return new Version(updated, lastVersion, urlDownload, notes);
    }
}

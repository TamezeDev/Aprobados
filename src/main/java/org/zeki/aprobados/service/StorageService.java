package org.zeki.aprobados.service;

import org.zeki.aprobados.exception.SupabaseConnectionException;
import org.zeki.aprobados.model.syllabus.FileStudy;

import java.io.File;
import java.net.http.HttpResponse;
import java.nio.file.Files;

public class StorageService extends SupabaseClient {

    private static final String STORAGE_URL = SUPABASE_BASE + "/storage/v1/object/documentos/";
    private static final String PUBLIC_URL = SUPABASE_BASE + "/storage/v1/object/public/documentos/";

    public ResultService uploadFile(File file, FileStudy fileStudy, String jwt) {
        try {
            String remotePath = encodePathSegment(buildRemotePath(fileStudy, file.getName()));
            String uploadUrl = STORAGE_URL + remotePath;

            byte[] fileBytes = Files.readAllBytes(file.toPath());
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) contentType = "application/octet-stream";

            HttpResponse<String> response = postBinary(uploadUrl, fileBytes, contentType, jwt);

            if (response.statusCode() == 400)
                return new ResultService("No tiene autorización para realizar esta operación", false);

            if (response.statusCode() == 200 || response.statusCode() == 201) {

                return new ResultService(PUBLIC_URL + remotePath, true);
            }
            return new ResultService("Error enviando archivo al servidor", false);

        } catch (Exception _) {
            throw new SupabaseConnectionException("ERROR ENVIANDO DATOS AL SERVIDOR");
        }

    }

    private String encodePathSegment(String segment) {
        return segment
                .replace(" ", "%20");
    }

    private String buildRemotePath(FileStudy fileStudy, String fileName) {
        String yearFolder = fileStudy.getStudyYear() == 1 ? "primeroDAM" : "segundoDAM";
        String typeFolder = fileStudy.isOfficial() ? "temarioOficial" : "resumenes";
        return yearFolder + "/" + fileStudy.getNameModule() + "/" + typeFolder + "/" + fileName;
    }
}


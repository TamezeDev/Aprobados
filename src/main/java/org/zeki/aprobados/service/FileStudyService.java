package org.zeki.aprobados.service;

import javafx.concurrent.Task;
import org.zeki.aprobados.controller.FileStudyController;
import org.zeki.aprobados.model.syllabus.FileStudy;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class FileStudyService {

    private final FileStudyController studyController;

    public FileStudyService() {
        studyController = new FileStudyController();
    }

    public void setStudyFiles(List<FileStudy> studies) {
        studyController.setFileStudies(studies);
    }

    public void openDocument(String selectedName, Consumer<ResultService> onResult) {

        FileStudy study = studyController.getFileByName(selectedName);
        // CHECK IF DOCUMENT IS PRESENT
        if (study == null) {
            onResult.accept(new ResultService("Documento no encontrado", false));
            return;
        }
        Task<Path> downloadTask = getPathTask(study);
        // OPEN DOCUMENT
        downloadTask.setOnSucceeded(_ -> {
            Path file = downloadTask.getValue();
            try {
                Desktop.getDesktop().open(file.toFile());
                onResult.accept(new ResultService("Documento abierto correctamente", true));
            } catch (IOException _) {
                onResult.accept(new ResultService("Error al abrir el documento", false));
            }
        });
        // FAIL! SHOW ERRO FEEDBACK
        downloadTask.setOnFailed(_ -> onResult.accept(new ResultService("Error al descargar el documento", false)));

        new Thread(downloadTask).start();
    }

    private Task<Path> getPathTask(FileStudy study) {
        String fileUrl = study.getUrl();
        // TASK TO DOWNLOAD DOCUMENT
        return new Task<>() {
            @Override
            protected Path call() throws Exception {
                Path cachedFile = studyController.getCachedFile(fileUrl);
                // CHECK IF THE DOCUMENT IS THE FOLDER
                if (Files.exists(cachedFile)) {
                    return cachedFile;
                }
                // DOWNLOAD DOCUMENT
                try (HttpClient client = HttpClient.newHttpClient()) {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(fileUrl))
                            .GET()
                            .build();

                    client.send(request, HttpResponse.BodyHandlers.ofFile(cachedFile));
                    return cachedFile;
                }
            }
        };
    }
}

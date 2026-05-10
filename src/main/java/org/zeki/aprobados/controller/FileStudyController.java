package org.zeki.aprobados.controller;

import lombok.Getter;
import lombok.Setter;
import org.zeki.aprobados.model.syllabus.FileStudy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FileStudyController {
    private List<FileStudy> fileStudies;
    private Path CACHE_DIR = Path.of(
            System.getProperty("user.home"), ".aprobados", "cache"
    );


    public FileStudyController() {
        fileStudies = new ArrayList<>();
    }

    public FileStudy getFileByName(String name) {
        return fileStudies.stream().filter(item -> item.getUnity().equals(name)).findFirst().orElse(null);
    }

    public Path getCachedFile(String url) throws Exception {
        // CREATE FOLDER CACHE
        Files.createDirectories(CACHE_DIR);

        // UNIQUE NAME FOR NO DUPLICATES
        String fileName = url.hashCode() + getExtension(url);
        return CACHE_DIR.resolve(fileName);
    }

    // ----------- PRIVATE METHODS -------------
    private String getExtension(String url) {
        if (url.endsWith(".pdf"))  return ".pdf";
        if (url.endsWith(".png"))  return ".png";
        if (url.endsWith(".jpg") || url.endsWith(".jpeg")) return ".jpg";
        return ".tmp";
    }
}

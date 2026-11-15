package com.manarbedeir.qa.ui.data;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public record UploadCase(String resource, String expectedHeading) {

    public Path path() {
        URL url = getClass().getClassLoader().getResource(resource);
        if (url == null) {
            throw new IllegalStateException("Upload file not found on classpath: " + resource);
        }
        try {
            return Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Invalid upload file location: " + url, e);
        }
    }

    public String fileName() {
        return path().getFileName().toString();
    }
}

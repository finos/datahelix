package com.scottlogic.deg.generator.outputs.manifest;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class ManifestWriter {
    public void write(ManifestDTO manifest, Path filepath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String manifestAsJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(manifest);

        Files.write(
            filepath,
            manifestAsJson.getBytes(
                Charset.forName("UTF-8")));
    }
}

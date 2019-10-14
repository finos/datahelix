package com.scottlogic.deg.generator.generation.visualiser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class VisualiserWriterFactory {

    private final Path outputFolder;

    public VisualiserWriterFactory(Path outputFolder) {
        this.outputFolder = outputFolder;
    }

    public Writer create(String destination) throws IOException {
        String destinationFileName =  outputFolder + File.separator + destination + ".dot";
        System.err.println("CREATING VISUALISER WRITER with destinationFile=" + destinationFileName);
        return new OutputStreamWriter(new FileOutputStream(destinationFileName), StandardCharsets.UTF_8);
    }
}

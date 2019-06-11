package com.scottlogic.deg.output;

import java.nio.file.Path;

public class OutputPath {
    private final Path path;

    public OutputPath(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}

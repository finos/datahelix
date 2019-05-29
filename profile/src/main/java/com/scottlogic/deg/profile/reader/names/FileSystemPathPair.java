package com.scottlogic.deg.profile.reader.names;

import java.nio.file.FileSystem;
import java.nio.file.Path;

class FileSystemPathPair {

    private final Path path;
    private final FileSystem fs;

    public FileSystemPathPair(Path path, FileSystem fs) {
        this.path = path;
        this.fs = fs;
    }

    public Path getPath() {
        return path;
    }

    public FileSystem getFs() {
        return fs;
    }
}

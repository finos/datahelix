package com.scottlogic.deg.output;

import java.nio.file.Path;

public class OutputPath {
    private final Path path;
    private OutputPath(Path path) {
        this.path = path;
    }
    public static OutputPath of(Path path){
        return new OutputPath(path);
    }

    public Path get(){
        return path;
    }
}

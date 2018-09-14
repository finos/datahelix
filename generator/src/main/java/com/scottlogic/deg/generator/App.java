package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.outputs.FileSystemDataSetOutputter;

public class App {
    public static void main(String[] args) {
        final String profileFilePath = args[0];
        final String directoryFilePath = args[1];

        new GenerationEngine(
                new FileSystemDataSetOutputter(directoryFilePath))
            .generateTestCases(profileFilePath);
    }
}

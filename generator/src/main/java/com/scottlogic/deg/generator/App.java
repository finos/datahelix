package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.outputs.FileSystemDataSetOutputter;

public class App {
    public static void main(String[] args) {
        final String profileFilePath = args[0];
        final String directoryFilePath = args[1];

        long start = System.nanoTime();

        System.out.println("Starting generation...");

        new GenerationEngine(
                new FileSystemDataSetOutputter(directoryFilePath))
            .generateTestCases(profileFilePath);

        long elapsed = System.nanoTime() - start;
        double seconds = (double)elapsed / 1000000000.0;

        System.out.println("Data generated in " + seconds + " seconds.");
    }
}

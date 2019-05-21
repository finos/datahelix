package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.outputs.formats.OutputFormat;
import com.scottlogic.deg.generator.outputs.targets.SingleDatasetOutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/*
    The implementation of this class is currently super hacky. Returns values are hardcoded based on specific knowledge
    of what the validation rules expect. In future, should defer to what the CucumberTestState knows about the
    filesystem.
*/
public class CucumberFileUtils implements FileUtils {
    private final CucumberTestState testState;

    @Inject
    CucumberFileUtils(CucumberTestState testState) {
        this.testState = testState;
    }

    @Override
    public boolean containsInvalidChars(File file) {
        return false;
    }

    @Override
    public boolean isFileEmpty(File file) {
        return false;
    }

    @Override
    public boolean exists(Path path) {
        return path.toString().equals("profileFilePath");
    }

    @Override
    public boolean isFile(Path path) {
        return true;
    }

    @Override
    public boolean isDirectory(Path path) {
        return false;
    }

    @Override
    public boolean isDirectoryEmpty(Path filepath, int fileCount) {
        return true;
    }

    @Override
    public boolean createDirectories(Path dir) throws IOException {
        return true;
    }

    @Override
    public SingleDatasetOutputTarget createFileTarget(Path destinationPath, OutputFormat outputFormat) {
        return new InMemoryOutputTarget(testState);
    }
}

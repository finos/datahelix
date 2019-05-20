package com.scottlogic.deg.generator.visualisation;

import com.scottlogic.deg.generator.commandline.VisualiseCommandLine;

import java.io.File;
import java.nio.file.Path;

public class TestVisualisationConfigSource extends VisualiseCommandLine {

    @Override
    public Path getOutputPath() {
        return null;
    }

    @Override
    public File getProfileFile() {
        return null;
    }

    @Override
    public boolean isSchemaValidationEnabled() {
        return true;
    }

    @Override
    public boolean dontOptimise() {
        return false;
    }

    @Override
    public boolean overwriteOutputFiles() {
        return false;
    }
}

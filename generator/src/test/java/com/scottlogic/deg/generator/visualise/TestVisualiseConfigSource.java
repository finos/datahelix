package com.scottlogic.deg.generator.visualise;

import java.io.File;
import java.nio.file.Path;

public class TestVisualiseConfigSource implements VisualiseConfigSource {

    public TestVisualiseConfigSource() {
    }

    @Override
    public Path getOutputPath() {
        return null;
    }

    @Override
    public File getProfileFile() {
        return null;
    }

    @Override
    public String getTitleOverride() {
        return null;
    }

    @Override
    public boolean shouldHideTitle() {
        return false;
    }

    @Override
    public boolean dontOptimise() {
        return false;
    }

    @Override
    public boolean dontSimplify() {
        return false;
    }

    @Override
    public boolean overwriteOutputFiles() {
        return false;
    }
}

package com.scottlogic.deg.generator.visualise;

import java.io.File;
import java.nio.file.Path;

public interface VisualiseConfigSource {
    String getTitle();
    Path getOutputPath();
    File getProfileFile();
    boolean shouldHideTitle();
    boolean shouldSimplify();
    boolean overwriteOutputFiles();
    boolean shouldOptimise();
}

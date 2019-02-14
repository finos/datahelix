package com.scottlogic.deg.generator.visualise;

import java.io.File;
import java.nio.file.Path;

public interface VisualiseConfigSource {

    Path getOutputPath();
    File getProfileFile();
    String getTitleOverride();
    boolean shouldHideTitle();
    boolean dontOptimise();
    boolean dontSimplify();
    boolean overwriteOutputFiles();
}

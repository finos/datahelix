package com.scottlogic.deg.generator;

import java.io.File;
import java.nio.file.Path;

public interface ConfigSource {

    Path getOutputPath();
    File getProfileFile();
    boolean dontOptimise();
    boolean overwriteOutputFiles();

    boolean shouldViolate();
    boolean getValidateProfile();

}

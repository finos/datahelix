package com.scottlogic.deg.generator;

import java.io.File;
import java.nio.file.Path;

/**
 * Super class for command line configuration options.
 */
public interface ConfigSource {

    boolean dontOptimise();
    Path getOutputPath();
    File getProfileFile();
    boolean getValidateProfile();
    boolean overwriteOutputFiles();
    boolean shouldViolate();

}

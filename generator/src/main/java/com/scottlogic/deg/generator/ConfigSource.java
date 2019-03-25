package com.scottlogic.deg.generator;

import java.io.File;
import java.nio.file.Path;

/**
 * Parent for command line configuration options.
 */
public interface ConfigSource {

    boolean dontOptimise();
    Path getOutputPath();
    File getProfileFile();
    boolean disableSchemaValidation();
    boolean getValidateProfile();
    boolean overwriteOutputFiles();

}

package com.scottlogic.deg.output.guice;

import java.nio.file.Path;

public interface OutputConfigSource {
    OutputFormat getOutputFormat();
    Path getOutputPath();
    boolean overwriteOutputFiles();
    boolean useStdOut();
}

package com.scottlogic.deg.generator.CommandLine;

import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;

public class VisualiseCommandLine  {

    @CommandLine.Parameters(index = "0", description = "The path of the profile json file.")
    private File profileFile;

    @CommandLine.Parameters(index = "1", description = "The path to write the generated data file to.")
    private Path outputPath;
}

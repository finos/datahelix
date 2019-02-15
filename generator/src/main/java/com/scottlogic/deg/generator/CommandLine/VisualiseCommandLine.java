package com.scottlogic.deg.generator.CommandLine;

import com.scottlogic.deg.generator.VisualiseExecute;
import java.io.File;
import java.nio.file.Path;

public class VisualiseCommandLine extends CommandLineBase {

    @picocli.CommandLine.Parameters(index = "0", description = "The path of the profile json file.")
    private File sourceFile;

    @picocli.CommandLine.Parameters(index = "1", description = "The directory into which generated data should be saved.")
    private Path outputDir;

    @picocli.CommandLine.Option(
        names = {"-t", "--title"},
        description = "The title to place at the top of the file")
    private String titleOverride;

    @picocli.CommandLine.Option(
        names = {"--no-title"},
        description = "Hides the title from the output")
    private boolean shouldHideTitle;

    @picocli.CommandLine.Option(
        names = {"--no-optimise"},
        description = "Prevents tree optimisation",
        hidden = true)
    private boolean dontOptimise;

    @picocli.CommandLine.Option(
        names = {"--no-simplify"},
        description = "Prevents tree simplification",
        hidden = true)
    private boolean dontSimplify;

    @picocli.CommandLine.Option(
        names = {"--overwrite"},
        description = "Defines whether to overwrite existing output files")
    private boolean overwriteOutputFiles = false;


    @Override
    public Path getOutputPath() {
        return outputDir;
    }

    @Override
    public File getProfileFile() {
        return sourceFile;
    }

    public String getTitleOverride() {
        return titleOverride;
    }

    public boolean shouldHideTitle() {
        return shouldHideTitle;
    }

    @Override
    public boolean dontOptimise() {
        return dontOptimise;
    }

    public boolean dontSimplify() {
        return dontSimplify;
    }

    @Override
    public boolean overwriteOutputFiles() {
        return overwriteOutputFiles;
    }

    @Override
    protected Class<? extends Runnable> getExecutorType() {
        return VisualiseExecute.class;
    }
}

package com.scottlogic.deg.generator.CommandLine;

import com.scottlogic.deg.generator.VisualiseExecute;
import com.scottlogic.deg.generator.visualisation.VisualisationConfigSource;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;

@picocli.CommandLine.Command(
    name = "visualise",
    description = "Produces a decision tree in DOT format for the specified profile.",
    descriptionHeading = "%nDescription:%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
    abbreviateSynopsis = true)
public class VisualiseCommandLine extends CommandLineBase implements VisualisationConfigSource {

    @CommandLine.Parameters(index = "0", description = "The path of the profile json file.")
    private File profileFile;

    @CommandLine.Parameters(index = "1", description = "The path of the output visualise file.")
    private Path outputPath;

    @CommandLine.Option(
        names = {"-t", "--title"},
        description = "The title to place at the top of the file")
    private String titleOverride;

    @CommandLine.Option(
        names = {"--no-title"},
        description = "Hides the title from the output")
    private boolean shouldHideTitle;

    @CommandLine.Option(
            names = {"--no-optimise"},
            description = "Prevents tree optimisation",
            hidden = true)
    private boolean dontOptimise;

    @CommandLine.Option(
        names = {"--no-simplify"},
        description = "Prevents tree simplification",
        hidden = true)
    private boolean dontSimplify;

    @CommandLine.Option(
        names = "--help",
        usageHelp = true,
        description = "Display these available command line options")
    boolean help;

    @picocli.CommandLine.Option(
        names = {"--overwrite"},
        description = "Defines whether to overwrite existing output files")
    private boolean overwriteOutputFiles = false;

    @Override
    public Path getOutputPath() {
        return outputPath;
    }

    @Override
    public File getProfileFile() {
        return profileFile;
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
    public boolean shouldViolate() {
        return false;
    }

    @Override
    public boolean getValidateProfile() {
        return false;
    }

    @Override
    protected Class<? extends Runnable> getExecutorType() {
        return VisualiseExecute.class;
    }
}

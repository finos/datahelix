package com.scottlogic.deg.generator.commandline;

import com.scottlogic.deg.generator.VisualiseExecute;
import com.scottlogic.deg.generator.visualisation.VisualisationConfigSource;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;

/**
 * This class holds the visualisation specific command line options.
 *
 * @see <a href="https://github.com/ScottLogic/datahelix/blob/master/docs/Options/VisualiseOptions.md">
 * Visualise options</a> for more details.
 */
@CommandLine.Command(
    name = "visualise",
    description = "Produces a decision tree in DOT format for the specified profile.",
    descriptionHeading = "%nDescription:%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
    abbreviateSynopsis = true)
public class VisualiseCommandLine extends CommandLineBase implements VisualisationConfigSource {

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
        names = {"--no-simplify"},
        description = "Prevents tree simplification",
        hidden = true)
    private boolean dontSimplify;

    @Override
    public Path getOutputPath() {
        return outputPath;
    }

    @Override
    public File getProfileFile() {
        return profileFile;
    }

    @Override
    public boolean isSchemaValidationEnabled() {
        return this.enableSchemaValidation;
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

    @Override
    public boolean overwriteOutputFiles() {
        return overwriteOutputFiles;
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

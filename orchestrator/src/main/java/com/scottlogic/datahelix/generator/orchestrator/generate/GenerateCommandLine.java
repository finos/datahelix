/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.datahelix.generator.orchestrator.generate;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.scottlogic.datahelix.generator.core.config.detail.CombinationStrategyType;
import com.scottlogic.datahelix.generator.core.config.detail.DataGenerationType;
import com.scottlogic.datahelix.generator.core.config.detail.MonitorType;
import com.scottlogic.datahelix.generator.core.config.detail.VisualiserLevel;
import com.scottlogic.datahelix.generator.orchestrator.CommonOptionInfo;
import com.scottlogic.datahelix.generator.orchestrator.guice.AllConfigSource;
import com.scottlogic.datahelix.generator.orchestrator.guice.AllModule;
import com.scottlogic.datahelix.generator.common.output.OutputFormat;
import com.scottlogic.datahelix.generator.profile.ProfileConfiguration;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import static com.scottlogic.datahelix.generator.common.util.Defaults.DEFAULT_MAX_ROWS;
import static com.scottlogic.datahelix.generator.core.config.detail.CombinationStrategyType.MINIMAL;
import static com.scottlogic.datahelix.generator.core.config.detail.DataGenerationType.RANDOM;
import static com.scottlogic.datahelix.generator.common.output.OutputFormat.CSV;

/**
 * This class holds the generate specific command line options.
 */
@picocli.CommandLine.Command(
    name = "",
    description = "Produces data using any options provided.",
    descriptionHeading = "%nDescription:%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
    version = { ProfileConfiguration.PROFILE_SCHEMA_VERSION_TEXT },
    abbreviateSynopsis = true)
public class GenerateCommandLine implements AllConfigSource, Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        Module container = new AllModule(this);
        Injector injector = Guice.createInjector(container);

        injector.getInstance(GenerateExecute.class).execute();
        return 0;
    }

    @CommandLine.Option(
        names = { "generate" },
        description = "Included for backward compatibility with command-based invocation",
        hidden = true
    )
    boolean generateCommandUsed;

    @CommandLine.Option(
        names = { CommonOptionInfo.VERSION_SHORT_OPTION, CommonOptionInfo.VERSION_LONG_OPTION },
        versionHelp = true,
        description = CommonOptionInfo.VERSION_DESCRIPTION)
    boolean versionRequested;

    @CommandLine.Option(
        names = {"-p", "--profile-file"},
        required = true,
        description = "The path of the profile json file.")
    private File profileFile;

    @CommandLine.Option(
        names = {"-o", "--output-path"}, order = 0,
        description = "The path to write the generated data file to.")
    private Path outputPath;

    @SuppressWarnings("unused")
    @CommandLine.Option(
        names = { CommonOptionInfo.HELP_SHORT_OPTION, CommonOptionInfo.HELP_LONG_OPTION },
        usageHelp = true,
        description = CommonOptionInfo.HELP_DESCRIPTION)
    private boolean help;

    @SuppressWarnings("FieldCanBeLocal")
    @CommandLine.Option(
        names = {"--replace"},
        description = "Defines whether to overwrite/replace existing output files.")
    private boolean overwriteOutputFiles = false;

    @SuppressWarnings("FieldCanBeLocal")
    @CommandLine.Option(
        names = { "--disable-schema-validation" },
        description = "Disables schema validation")
    private boolean disableSchemaValidation = false;

    @CommandLine.Option(names = {"-t", "--generation-type"},
        description = "Determines the type of data generation performed (${COMPLETION-CANDIDATES})",
        hidden = true)
    private DataGenerationType generationType = RANDOM;

    @CommandLine.Option(names = {"-c", "--combination-strategy"},
        description = "Determines the type of combination strategy used (${COMPLETION-CANDIDATES})")
    private CombinationStrategyType combinationType = MINIMAL;

    @SuppressWarnings("FieldCanBeLocal")
    @CommandLine.Option(
        names = {"-n", "--max-rows"},
        description = "Defines the maximum number of rows that should be generated")
    private long maxRows = DEFAULT_MAX_ROWS;

    @SuppressWarnings("FieldCanBeLocal")
    @CommandLine.Option(
        names = {"--quiet"},
        description = "Turns OFF default monitoring")
    private Boolean quiet = false;

    @SuppressWarnings("FieldCanBeLocal")
    @CommandLine.Option(
        names = {"--verbose"},
        description = "Turns ON system out monitoring")
    private Boolean verbose = false;

    @CommandLine.Option(
        names = {"--output-format"},
        description = "Output format (${COMPLETION-CANDIDATES})")
    private OutputFormat outputFormat = CSV;

    @SuppressWarnings("FieldCanBeLocal")
    @CommandLine.Option(
        names = {"--set-from-file-directory"},
        description = "Custom root for loading sets from file."
    )
    private String fromFilePath = "";

    @CommandLine.Option(
        names = {"--visualiser-level"},
        description = "Visualiser level (${COMPLETION-CANDIDATES})")
    private VisualiserLevel visualiserLevel = VisualiserLevel.OFF;

    @CommandLine.Option(
        names = {"--visualiser-output-folder"},
        description = "The path to the folder to write the generated visualiser files to (only used if visualiser-level != OFF).")
    private Path visualiserOutputFolder = new File(".").toPath();

    @Override
    public File getProfileFile() {
        return profileFile.getAbsoluteFile();
    }

    @Override
    public boolean overwriteOutputFiles() {
        return overwriteOutputFiles;
    }

    @Override
    public boolean useStdOut() {
        return outputPath == null;
    }

    @Override
    public Path getOutputPath() {
        return outputPath;
    }

    @Override
    public DataGenerationType getGenerationType() {
        return generationType;
    }

    @Override
    public CombinationStrategyType getCombinationStrategyType() {
        return combinationType;
    }

    @Override
    public MonitorType getMonitorType() {
        if (this.verbose) {
            return MonitorType.VERBOSE;
        }
        if (this.quiet) {
            return MonitorType.QUIET;
        }
        return MonitorType.STANDARD;
    }

    @Override
    public long getMaxRows() {
        return maxRows;
    }

    public OutputFormat getOutputFormat() {
        return outputFormat;
    }

    @Override
    public String fromFilePath() {
        return fromFilePath;
    }

    @Override
    public VisualiserLevel getVisualiserLevel() {
        return visualiserLevel;
    }

    @Override
    public Path getVisualiserOutputFolder() {
        return visualiserOutputFolder;
    }

    protected static void printAlphaFeatureWarning(String feature) {
        System.err.println(feature + " is an ALPHA FEATURE. Please do not rely on it. If you find any issues with it, please report them at https://github.com/finos/datahelix/issues.");
    }
}

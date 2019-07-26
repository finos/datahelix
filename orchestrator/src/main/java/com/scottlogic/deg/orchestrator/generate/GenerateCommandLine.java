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

package com.scottlogic.deg.orchestrator.generate;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.scottlogic.deg.generator.config.detail.*;
import com.scottlogic.deg.orchestrator.guice.AllConfigSource;
import com.scottlogic.deg.orchestrator.guice.AllModule;
import com.scottlogic.deg.output.guice.OutputFormat;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import static com.scottlogic.deg.generator.config.detail.CombinationStrategyType.MINIMAL;
import static com.scottlogic.deg.common.util.Defaults.DEFAULT_MAX_ROWS;
import static com.scottlogic.deg.generator.config.detail.DataGenerationType.RANDOM;
import static com.scottlogic.deg.generator.config.detail.TreeWalkerType.DECISION_BASED;
import static com.scottlogic.deg.output.guice.OutputFormat.CSV;

/**
 * This class holds the generate specific command line options.
 */
@picocli.CommandLine.Command(
    name = "generate",
    description = "Produces data using any options provided.",
    descriptionHeading = "%nDescription:%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
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
        names = {"-p", "--profile-file"},
        required = true,
        description = "The path of the profile json file.")
    File profileFile;

    @CommandLine.Option(
        names = {"-o", "--output-path"}, order = 0,
        description = "The path to write the generated data file to.")
    private Path outputPath;

    @CommandLine.Option(
        names = {"--no-optimise"},
        description = "Prevents tree optimisation",
        hidden = true)
    boolean dontOptimise;

    @CommandLine.Option(
        names = "--help",
        usageHelp = true,
        description = "Display these available command line options")
    boolean help;

    @CommandLine.Option(
        names = {"--replace"},
        description = "Defines whether to overwrite/replace existing output files")
    boolean overwriteOutputFiles = false;

    @CommandLine.Option(
        names = { "--disable-schema-validation" },
        description = "Disables schema validation")
    boolean disableSchemaValidation = false;

    @CommandLine.Option(names = {"-t", "--generation-type"},
        description = "Determines the type of data generation performed (${COMPLETION-CANDIDATES})",
        hidden = true)
    private DataGenerationType generationType = RANDOM;

    @CommandLine.Option(names = {"-c", "--combination-strategy"},
        description = "Determines the type of combination strategy used (${COMPLETION-CANDIDATES})",
        hidden = true)
    @SuppressWarnings("unused")
    private CombinationStrategyType combinationType = MINIMAL;

    @CommandLine.Option(
        names = {"--no-partition"},
        description = "Prevents tree partitioning",
        hidden = true)
    private boolean dontPartitionTrees;

    @CommandLine.Option(names = {"-w", "--walker-type"},
        description = "Determines the tree walker that should be used (${COMPLETION-CANDIDATES})",
        hidden = true)
    private TreeWalkerType walkerType = DECISION_BASED;

    @CommandLine.Option(
        names = {"-n", "--max-rows"},
        description = "Defines the maximum number of rows that should be generated")
    private long maxRows = DEFAULT_MAX_ROWS;

    @CommandLine.Option(
        names = {"--quiet"},
        description = "Turns OFF default monitoring")
    private Boolean quiet = false;

    @CommandLine.Option(
        names = {"--verbose"},
        description = "Turns ON system out monitoring")
    private Boolean verbose = false;

    @CommandLine.Option(
        names = {"--visualise-reductions"},
        description = "Visualise each tree reduction",
        hidden = true)
    private Boolean visualiseReductions = false;

    @CommandLine.Option(
        names = {"--output-format"},
        description = "Output format (${COMPLETION-CANDIDATES})")
    private OutputFormat outputFormat = CSV;

    @CommandLine.Option(
        names = {"--allow-untyped-fields"},
        description = "Remove the need for each field to have at least one compliant typing constraint applied")
    private boolean allowUntypedFields = false;

    @CommandLine.Option(
        names = {"--set-from-file-directory"},
        description = "Custom root for loading sets from file."
    )
    private String fromFilePath;

    public boolean shouldDoPartitioning() {
        return !this.dontPartitionTrees;
    }

    @Override
    public boolean dontOptimise() {
        return this.dontOptimise;
    }

    @Override
    public File getProfileFile() {
        return this.profileFile;
    }

    @Override
    public boolean isSchemaValidationDisabled() {
        return this.disableSchemaValidation;
    }

    @Override
    public boolean overwriteOutputFiles() {
        return this.overwriteOutputFiles;
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
        return this.generationType;
    }

    @Override
    public CombinationStrategyType getCombinationStrategyType() {
        return this.combinationType;
    }

    @Override
    public TreeWalkerType getWalkerType() {
        return this.walkerType;
    }

    @Override
    public boolean requireFieldTyping(){
        return !allowUntypedFields;
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

    @Override
    public boolean visualiseReductions() {
        return visualiseReductions;
    }

    public OutputFormat getOutputFormat() {
        return outputFormat;
    }

    @Override
    public String fromFilePath() {
        return fromFilePath;
    }
}

package com.scottlogic.deg.orchestrator.visualise;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.visualisation.DecisionTreeVisualisationWriter;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.orchestrator.guice.AllConfigSource;
import com.scottlogic.deg.output.OutputPath;
import com.scottlogic.deg.profile.reader.ProfileReader;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.generator.validators.StaticContradictionDecisionTreeValidator;
import com.scottlogic.deg.orchestrator.validator.VisualisationConfigValidator;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * This class performs the visualisation of a profile
 * and outputs the visualisation as a graphviz dot file..
 */
public class VisualiseExecute implements Runnable {

    private final DecisionTreeFactory profileAnalyser;
    private final ErrorReporter errorReporter;
    private final FieldSpecFactory fieldSpecFactory;
    private final FieldSpecMerger fieldSpecMerger;
    private final Path outputPath;
    private final ProfileReader profileReader;
    private final ProfileSchemaValidator profileSchemaValidator;
    private final AllConfigSource configSource;
    private final VisualisationConfigValidator validator;

    @Inject
    public VisualiseExecute(DecisionTreeFactory profileAnalyser,
                            ErrorReporter errorReporter,
                            FieldSpecFactory fieldSpecFactory,
                            FieldSpecMerger fieldSpecMerger,
                            OutputPath outputPath,
                            ProfileReader profileReader,
                            ProfileSchemaValidator profileSchemaValidator,
                            AllConfigSource configSource,
                            VisualisationConfigValidator validator) {
        this.profileAnalyser = profileAnalyser;
        this.errorReporter = errorReporter;
        this.fieldSpecFactory = fieldSpecFactory;
        this.fieldSpecMerger = fieldSpecMerger;
        this.configSource = configSource;
        this.outputPath = outputPath.getPath();
        this.profileReader = profileReader;
        this.profileSchemaValidator = profileSchemaValidator;
        this.validator = validator;
    }

    @Override
    public void run() {
        validator.validateCommandLine(configSource.overwriteOutputFiles(), outputPath);
        profileSchemaValidator.validateProfile(configSource.getProfileFile());

        final Profile profile;
        try {
            profile = profileReader.read(configSource.getProfileFile().toPath());
        } catch (Exception e) {
            System.err.println("Failed to read file!");
            e.printStackTrace();
            return;
        }

        final DecisionTree mergedTree = profileAnalyser.analyse(profile);

        final String profileBaseName = configSource.getProfileFile().getName()
            .replaceFirst("\\.[^.]+$", "");

        StaticContradictionDecisionTreeValidator treeValidator =
            new StaticContradictionDecisionTreeValidator(
                profile.getFields(),
                new RowSpecMerger(fieldSpecMerger),
                new ConstraintReducer(fieldSpecFactory, fieldSpecMerger));

        DecisionTree validatedTree = treeValidator.markContradictions(mergedTree);

        final String title =
            Stream.of(profile.getDescription(), profileBaseName)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);

        try {
            writeTreeTo(
                validatedTree,
                title,
                configSource.getOutputPath());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void writeTreeTo(
        DecisionTree decisionTree,
        String description,
        Path outputFilePath)
        throws IOException {

        try (OutputStreamWriter outWriter = new OutputStreamWriter(
            new FileOutputStream(outputFilePath.toString()),
            StandardCharsets.UTF_8)) {

            new DecisionTreeVisualisationWriter(outWriter).writeDot(
                decisionTree,
                "tree",
                description);
        }
    }
}

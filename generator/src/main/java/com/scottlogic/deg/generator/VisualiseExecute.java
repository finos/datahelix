package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.decisiontree.visualisation.DecisionTreeVisualisationWriter;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.inputs.JsonProfileReader;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.inputs.validation.NoopProfileValidator;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.generator.validators.StaticContradictionDecisionTreeValidator;
import com.scottlogic.deg.generator.validators.ValidationResult;
import com.scottlogic.deg.generator.validators.VisualisationConfigValidator;
import com.scottlogic.deg.generator.visualisation.VisualisationConfig;
import com.scottlogic.deg.generator.visualisation.VisualisationConfigSource;
import picocli.CommandLine;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

@CommandLine.Command(
    name = "visualise",
    description = "Produces a decision tree in DOT format for the specified profile.",
    descriptionHeading = "%nDescription:%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
    abbreviateSynopsis = true)
public class VisualiseExecute implements Runnable {

    private final VisualisationConfig config;
    private final ProfileReader profileReader;
    private final VisualisationConfigSource configSource;
    private final VisualisationConfigValidator validator;
    private final ErrorReporter errorReporter;

    @Inject
    public VisualiseExecute(VisualisationConfig config,
                            ProfileReader profileReader,
                            VisualisationConfigSource configSource,
                            VisualisationConfigValidator validator,
                            ErrorReporter errorReporter) {
        this.config = config;
        this.profileReader = profileReader;
        this.configSource = configSource;
        this.validator = validator;
        this.errorReporter = errorReporter;
    }

    @Override
    public void run() {

        ValidationResult validationResult = validator.validateCommandLine(config);

        if (!validationResult.isValid()) {
            errorReporter.display(validationResult);
            return;
        }

        final DecisionTreeFactory profileAnalyser = new ProfileDecisionTreeFactory();

        final Profile profile;
        try {
            profile = new JsonProfileReader(new NoopProfileValidator()).read(this.configSource.getProfileFile().toPath());
        } catch (Exception e) {
            System.err.println("Failed to read file!");
            e.printStackTrace();
            return;
        }

        final DecisionTreeCollection decisionTreeCollection = profileAnalyser.analyse(profile);
        final DecisionTree mergedTree = decisionTreeCollection.getMergedTree();
        final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();

        final String profileBaseName = configSource.getProfileFile().getName()
            .replaceFirst("\\.[^.]+$", "");
        final DecisionTreeOptimiser treeOptimiser = configSource.dontOptimise()
            ? new NoopDecisionTreeOptimiser()
            : new MostProlificConstraintOptimiser();

        StaticContradictionDecisionTreeValidator treeValidator = new StaticContradictionDecisionTreeValidator(
            profile.fields,
            new RowSpecMerger(fieldSpecMerger),
            new ConstraintReducer(new FieldSpecFactory(), fieldSpecMerger));

        DecisionTree validatedTree = treeValidator.markContradictions(mergedTree);

        final String title = configSource.shouldHideTitle()
            ? null
            : Stream.of(configSource.getTitleOverride(), profile.description, profileBaseName)
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

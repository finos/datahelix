package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeCollection;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeSimplifier;
import com.scottlogic.deg.generator.decisiontree.MostProlificConstraintOptimiser;
import com.scottlogic.deg.generator.decisiontree.NoopDecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.ProfileDecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.NoopTreePartitioner;
import com.scottlogic.deg.generator.decisiontree.visualisation.DecisionTreeVisualisationWriter;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.generator.validators.StaticContradictionDecisionTreeValidator;
import com.scottlogic.deg.generator.validators.ValidationResult;
import com.scottlogic.deg.generator.validators.VisualiseConfigValidator;
import com.scottlogic.deg.generator.visualise.VisualiseConfig;
import com.scottlogic.deg.generator.visualise.VisualiseConfigSource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@picocli.CommandLine.Command(
    name = "visualise",
    description = "Produces a decision tree in DOT format for the specified profile.",
    mixinStandardHelpOptions = true,
    version = "1.0")
public class VisualiseExecute implements Runnable {

    private final VisualiseConfig config;
    private final ProfileReader profileReader;
    private final VisualiseConfigSource configSource;
    private final VisualiseConfigValidator validator;
    private final ErrorReporter errorReporter;

    @Inject
    public VisualiseExecute(VisualiseConfig config,
        ProfileReader profileReader,
        VisualiseConfigSource configSource,
        VisualiseConfigValidator validator,
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
            profile = this.profileReader.read(this.configSource.getProfileFile().toPath());
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

        final List<DecisionTree> treePartitions = new NoopTreePartitioner()
            .splitTreeIntoPartitions(mergedTree)
            .map(treeOptimiser::optimiseTree)
            .map(tree -> configSource.dontSimplify() ? tree
                : new DecisionTreeSimplifier().simplify(tree))
            .map(treeValidator::markContradictions)
            .collect(Collectors.toList());

        final String title = configSource.shouldHideTitle()
            ? null
            : Stream.of(configSource.getTitleOverride(), profile.description, profileBaseName)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        try {
            if (treePartitions.size() == 1) {
                writeTreeTo(
                    treePartitions.get(0),
                    title,
                    configSource.getOutputPath().resolve(profileBaseName + ".gv"));
            } else {
                writeTreeTo(
                    mergedTree,
                    title,
                    configSource.getOutputPath().resolve(profileBaseName + ".unpartitioned.gv"));

                for (int i = 0; i < treePartitions.size(); i++) {
                    writeTreeTo(
                        treePartitions.get(i),
                        title != null
                            ? title + " (partition " + (i + 1) + ")"
                            : null,
                        configSource.getOutputPath()
                            .resolve(profileBaseName + ".partition" + (i + 1) + ".gv"));
                }
            }
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

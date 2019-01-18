package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.decisiontree.visualisation.DecisionTreeVisualisationWriter;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.inputs.validation.NoopProfileValidator;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.validators.StaticContradictionDecisionTreeValidator;

import java.io.File;
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
public class Visualise implements Runnable {
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

    @Override
    public void run() {
        final DecisionTreeFactory profileAnalyser = new ProfileDecisionTreeFactory();
        final Profile profile;

        try {
            profile = new ProfileReader(new NoopProfileValidator()).read(sourceFile.toPath());
        } catch (Exception e) {
            System.err.println("Failed to read file!");
            e.printStackTrace();
            return;
        }

        final DecisionTreeCollection decisionTreeCollection = profileAnalyser.analyse(profile);
        final DecisionTree mergedTree = decisionTreeCollection.getMergedTree();
        final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();

        final String profileBaseName = sourceFile.getName().replaceFirst("\\.[^.]+$", "");
        final DecisionTreeOptimiser treeOptimiser = dontOptimise
                ? new NoopDecisionTreeOptimiser()
                : new MostProlificConstraintOptimiser();

        StaticContradictionDecisionTreeValidator treeValidator = new StaticContradictionDecisionTreeValidator(
            profile.fields,
            new RowSpecMerger(fieldSpecMerger),
            new ConstraintReducer(new FieldSpecFactory(), fieldSpecMerger));

        final List<DecisionTree> treePartitions = Stream.of(mergedTree)
                .map(treeOptimiser::optimiseTree)
                .map(tree -> this.dontSimplify ? tree : new DecisionTreeSimplifier().simplify(tree))
                .map(treeValidator::markContradictions)
                .collect(Collectors.toList());

        final String title = shouldHideTitle
            ? null
            : Stream.of(titleOverride, profile.description, profileBaseName)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        try {
            if (treePartitions.size() == 1) {
                writeTreeTo(
                    treePartitions.get(0),
                    title,
                    outputDir.resolve(profileBaseName + ".gv"));
            } else {
                writeTreeTo(
                    mergedTree,
                    title,
                    outputDir.resolve(profileBaseName + ".unpartitioned.gv"));

                for (int i = 0; i < treePartitions.size(); i++) {
                    writeTreeTo(
                        treePartitions.get(i),
                        title != null
                            ? title + " (partition " + (i + 1) + ")"
                            : null,
                        outputDir.resolve(profileBaseName + ".partition" + (i + 1) + ".gv"));
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

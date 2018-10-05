package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.inputs.ProfileReader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

    @Override
    public void run() {
        final IDecisionTreeGenerator profileAnalyser = new DecisionTreeGenerator();
        final Profile profile;

        try {
            profile = new ProfileReader().read(sourceFile.toPath());
        } catch (Exception e) {
            System.err.println("Failed to read file!");
            System.err.println(e.toString());
            for (StackTraceElement ste : e.getStackTrace())
                System.err.println(ste.toString());
            return;
        }

        final DecisionTreeCollection decisionTreeCollection = profileAnalyser.analyse(profile);
        final DecisionTree mergedTree = decisionTreeCollection.getMergedTree();

        final String profileBaseName = sourceFile.getName().replaceFirst("\\.[^.]+$", "");

        final List<DecisionTree> treePartitions = new NoopTreePartitioner().splitTreeIntoPartitions(mergedTree).collect(Collectors.toList());

        final String title = shouldHideTitle
            ? null
            : Stream.of(titleOverride, profile.description, profileBaseName)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        try {
            if (treePartitions.size() == 1) {
                writeTreeTo(
                    mergedTree,
                    title,
                    outputDir.resolve(profileBaseName + ".gv"));
            } else {
                writeTreeTo(
                    mergedTree,
                    title,
                    outputDir.resolve(profileBaseName + ".unpartitioned.gv"));

                for (int i = 0; i < treePartitions.size(); i++) {
                    writeTreeTo(
                        mergedTree,
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

        try (PrintWriter outWriter = new PrintWriter(outputFilePath.toString())) {
            new DecisionTreeVisualisationWriter(outWriter).writeDot(
                decisionTree,
                "tree",
                description);
        }
    }
}

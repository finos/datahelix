package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.inputs.ProfileReader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;
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

        List<DecisionTree> treePartitions = new NoopTreePartitioner().splitTreeIntoPartitions(mergedTree).collect(Collectors.toList());

        try {
            writeTreeTo(
                mergedTree,
                outputDir.resolve(profileBaseName + ".unpartitioned.gv"));

            if (treePartitions.size() > 1) {
                for (int i = 0; i < treePartitions.size(); i++) {
                    writeTreeTo(
                        mergedTree,
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
        Path outputFilePath)
        throws IOException {

        try (PrintWriter outWriter = new PrintWriter(outputFilePath.toString())) {
            new DecisionTreeVisualisationWriter(outWriter).writeDot(
                decisionTree,
                "tree",
                "");
        }
    }
}

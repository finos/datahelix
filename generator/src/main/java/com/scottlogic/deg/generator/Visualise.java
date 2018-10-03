package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.decisiontree.DecisionTreeCollection;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeGenerator;
import com.scottlogic.deg.generator.inputs.ProfileReader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

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

        final DecisionTreeCollection analysedProfile = profileAnalyser.analyse(profile);

        writeTreeGraphs(analysedProfile, outputDir, sourceFile.getName());
    }

    private void writeTreeGraphs(DecisionTreeCollection analysedProfile, Path directory, String filename) {
        String dot = analysedProfile.getMergedTree().toDot("tree", "");

        try {
            try (PrintWriter out = new PrintWriter(String.format("%s/%s.gv", directory.toString(), filename ))) {
                out.println(dot);
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}

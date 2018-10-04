package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.inputs.ProfileReader;

import java.io.File;
import java.io.FileNotFoundException;
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

        try {
            writeTreeGraphs(
                analysedProfile,
                outputDir,
                sourceFile.getName().replaceFirst("\\.[^.]+$", ""));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void writeTreeGraphs(
        DecisionTreeCollection analysedProfile,
        Path directory,
        String filenameBase)
        throws IOException {

        try (PrintWriter out = new PrintWriter(directory.resolve(filenameBase + ".gv").toString())) {
            new DecisionTreeVisualisationWriter(out).writeDot(
                analysedProfile.getMergedTree(),
                "tree",
                "");
        }
    }
}

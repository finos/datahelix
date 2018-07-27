package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResultWriter;

import java.nio.file.Paths;

public class App
{
    private static final IProfileAnalyser profileAnalyser = new DummyProfileAnalyser();
    private static final IDataGenerator dataGenerator = new DummyDataGenerator();

    public static void main(String[] args) {
        final String profileFilePath = args[0];
        final String directoryFilePath = args[1];

        final Profile profile;

        try {
            profile = new ProfileReader().reader(Paths.get(profileFilePath));
        }
        catch(Exception e) {
            System.err.println("Failed to read file!");
            return;
        }

        final IAnalysedProfile analysedProfile = profileAnalyser.analyse(profile);
        final TestCaseGenerationResult generationResult = dataGenerator.generateData(profile, analysedProfile);

        try {
            new TestCaseGenerationResultWriter().writeToDirectory(
                generationResult,
                Paths.get(directoryFilePath).toAbsolutePath().normalize());
        }
        catch (Exception e) {
            System.err.println("Failed to write generation result");
        }
    }
}

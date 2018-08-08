package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeProfile;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.IDataGenerator;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResultWriter;
import com.scottlogic.deg.generator.reducer.AutomatonFactory;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;

import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        final String profileFilePath = args[0];
        final String directoryFilePath = args[1];

        new GenerationEngine().generateTestCases(profileFilePath, directoryFilePath);
    }
}

class GenerationEngine {
    private final IDecisionTreeGenerator profileAnalyser = new DecisionTreeGenerator();
    private final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();
    private final IDataGenerator dataGenerator = new DataGenerator(
            fieldSpecMerger,
            new ConstraintReducer(
                    new FieldSpecFactory(
                            new AutomatonFactory()
                    ),
                    fieldSpecMerger));

    void generateTestCases(String profileFilePath, String directoryFilePath) {
        final Profile profile;

        try {
            profile = new ProfileReader().read(Paths.get(profileFilePath));
        }
        catch(Exception e) {
            System.err.println("Failed to read file!");
            System.err.println(e.toString());
            for (StackTraceElement ste : e.getStackTrace())
                System.err.println(ste.toString());
            return;
        }

        final IDecisionTreeProfile analysedProfile = this.profileAnalyser.analyse(profile);

        final TestCaseGenerationResult generationResult = this.dataGenerator.generateData(profile, analysedProfile);

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

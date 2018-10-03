package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.decisiontree.DecisionTreeCollection;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeGenerator;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.IDataGenerator;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.IDataSetOutputter;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;

import java.nio.file.Paths;

public class GenerationEngine {
    private final IDecisionTreeGenerator profileAnalyser = new DecisionTreeGenerator();
    private final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();
    private final IDataGenerator dataGenerator = new DataGenerator(
        new RowSpecMerger(fieldSpecMerger),
        new ConstraintReducer(
            new FieldSpecFactory(),
            fieldSpecMerger));

    private final IDataSetOutputter outputter;

    public GenerationEngine(IDataSetOutputter outputter) {
        this.outputter = outputter;
    }

    public void generateTestCases(String profileFilePath, GenerationConfig config) {
        final Profile profile;

        try {
            profile = new ProfileReader().read(Paths.get(profileFilePath));
        } catch (Exception e) {
            System.err.println("Failed to read file!");
            System.err.println(e.toString());
            for (StackTraceElement ste : e.getStackTrace())
                System.err.println(ste.toString());
            return;
        }

        final DecisionTreeCollection analysedProfile = this.profileAnalyser.analyse(profile);

        final TestCaseGenerationResult generationResult = this.dataGenerator.generateData(profile, analysedProfile.getMergedTree(), config);

        try {
            this.outputter.output(generationResult);

        } catch (Exception e) {
            System.err.println("Failed to write generation result");
            System.err.println(e.toString());
            for (StackTraceElement ste : e.getStackTrace())
                System.err.println(ste.toString());
        }
    }
}


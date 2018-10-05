package com.scottlogic.deg.generator.smoke_tests;

import com.scottlogic.deg.generator.GenerationEngine;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.combination_strategies.FieldExhaustiveCombinationStrategy;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.targets.IOutputTarget;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiConsumer;

import static org.hamcrest.Matchers.notNullValue;

class ExampleProfilesTests {
    @TestFactory
    Collection<DynamicTest> shouldGenerateAsTestCasesWithoutErrors() throws IOException {
        return forEachProfileFile(((generationEngine, profileFile) ->
        {
            GenerationConfig config = new GenerationConfig(GenerationConfig.DataGenerationType.Interesting,
                new FieldExhaustiveCombinationStrategy());
            generationEngine.generateTestCases(profileFile.toPath(), config);
        }));
    }

    @TestFactory
    Collection<DynamicTest> shouldGenerateWithoutErrors() throws IOException {
        return forEachProfileFile(((generationEngine, profileFile) ->
        {
            GenerationConfig config = new GenerationConfig(GenerationConfig.DataGenerationType.Interesting,
                new FieldExhaustiveCombinationStrategy());
            generationEngine.generateDataSet(profileFile.toPath(), config);
        }));
    }

    private Collection<DynamicTest> forEachProfileFile(BiConsumer<GenerationEngine, File> consumer) throws IOException {
        Collection<DynamicTest> dynamicTests = new ArrayList<>();

        File[] directoriesArray =
            Paths.get("..", "examples")
                .toFile()
                .listFiles(File::isDirectory);

        for (File dir : directoriesArray) {
            File profileFile = Paths.get(dir.getCanonicalPath(), "profile.json").toFile();

            GenerationConfig config = new GenerationConfig(GenerationConfig.DataGenerationType.Interesting,
                new FieldExhaustiveCombinationStrategy());

            DynamicTest test = DynamicTest.dynamicTest(dir.getName(), () -> {
                new GenerationEngine(
                        new NullOutputTarget())
                    .generateTestCases(profileFile.toPath(), config);
            });

            dynamicTests.add(test);
        }

        return dynamicTests;
    }

    private class NullOutputTarget implements IOutputTarget {
        @Override
        public void outputDataset(Iterable<GeneratedObject> generatedObjects, ProfileFields profileFields) throws IOException {
            // iterate through the rows - assume lazy generation, so we haven't tested unless we've exhausted the iterable

            generatedObjects.iterator().forEachRemaining(
                row -> Assert.assertThat(row, notNullValue())); // might as well assert non-null while we're at it
        }

        @Override
        public void outputTestCases(TestCaseGenerationResult dataSets) throws IOException {
            // iterate through the rows - assume lazy generation, so we haven't tested unless we've exhausted every iterable

            dataSets.datasets.iterator().forEachRemaining(
                ds -> ds.iterator().forEachRemaining(
                    row -> Assert.assertThat(row, notNullValue()))); // might as well assert non-null while we're at it
        }
    }
}

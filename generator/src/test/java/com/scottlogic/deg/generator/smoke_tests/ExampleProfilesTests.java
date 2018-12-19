package com.scottlogic.deg.generator.smoke_tests;

import com.scottlogic.deg.generator.GenerationEngine;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.analysis.FieldDependencyAnalyser;
import com.scottlogic.deg.generator.decisiontree.MostProlificConstraintOptimiser;
import com.scottlogic.deg.generator.decisiontree.ProfileDecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.generation.DecisionTreeDataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.walker.DecisionTreeWalkerFactory;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.HierarchicalDependencyFixFieldStrategy;
import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.notNullValue;

class ExampleProfilesTests {
    private static final DecisionTreeWalkerFactory walkerFactory = new SmokeTestsDecisionTreeWalkerFactory();

    @TestFactory
    Collection<DynamicTest> shouldGenerateAsTestCasesWithoutErrors() throws IOException, InvalidProfileException {
        return forEachProfileFile(((generationEngine, profileFile) -> {
            GenerationConfig config = new GenerationConfig(
                new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.CARTESIAN_PRODUCT,
                GenerationConfig.CombinationStrategyType.PINNING));
            final Profile profile = new ProfileReader().read(profileFile.toPath());
            generationEngine.generateTestCases(profile, config);
        }));
    }

    @TestFactory
    Collection<DynamicTest> shouldGenerateWithoutErrors() throws IOException, InvalidProfileException {
        return forEachProfileFile(((generationEngine, profileFile) -> {
            GenerationConfig config = new GenerationConfig(
                new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.CARTESIAN_PRODUCT,
                GenerationConfig.CombinationStrategyType.PINNING));

            final Profile profile = new ProfileReader().read(profileFile.toPath());
            generationEngine.generateTestCases(profile, config);
        }));
    }

    private Collection<DynamicTest> forEachProfileFile(GenerateConsumer consumer) throws IOException, InvalidProfileException {
        Collection<DynamicTest> dynamicTests = new ArrayList<>();

        File[] directoriesArray =
            Paths.get("..", "examples")
                .toFile()
                .listFiles(File::isDirectory);

        for (File dir : directoriesArray) {
            File profileFile = Paths.get(dir.getCanonicalPath(), "profile.json").toFile();
            final Profile profile = new ProfileReader().read(profileFile.toPath());
            FixFieldStrategy fixFieldStrategy = new HierarchicalDependencyFixFieldStrategy(profile, new FieldDependencyAnalyser());

            DynamicTest test = DynamicTest.dynamicTest(dir.getName(), () -> {
                consumer.generate(
                    new GenerationEngine(
                        new NullOutputTarget(),
                        new DecisionTreeDataGenerator(
                            null,
                            new RelatedFieldTreePartitioner(),
                            new MostProlificConstraintOptimiser(),
                            new NoopDataGeneratorMonitor(),
                            new ProfileDecisionTreeFactory(),
                            fixFieldStrategy
                            )),
                    profileFile);
            });

            dynamicTests.add(test);
        }

        return dynamicTests;
    }

    private class NullOutputTarget implements OutputTarget {
        @Override
        public void outputDataset(Stream<GeneratedObject> generatedObjects, ProfileFields profileFields) throws IOException {
            // iterate through the rows - assume lazy generation, so we haven't tested unless we've exhausted the iterable

            generatedObjects.forEach(
                row -> Assert.assertThat(row, notNullValue())); // might as well assert non-null while we're at it
        }

        @Override
        public void outputTestCases(TestCaseGenerationResult dataSets) throws IOException {
            // iterate through the rows - assume lazy generation, so we haven't tested unless we've exhausted every iterable

            dataSets.datasets.forEach(
                ds -> ds.stream().forEach(
                    row -> Assert.assertThat(row, notNullValue()))); // might as well assert non-null while we're at it
        }
    }

    @FunctionalInterface
    private interface GenerateConsumer {
        void generate(GenerationEngine engine, File profileFile) throws IOException, InvalidProfileException;
    }
}


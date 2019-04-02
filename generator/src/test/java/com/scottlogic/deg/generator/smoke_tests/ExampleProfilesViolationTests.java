package com.scottlogic.deg.generator.smoke_tests;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.StandardGenerationEngine;
import com.scottlogic.deg.generator.analysis.FieldDependencyAnalyser;
import com.scottlogic.deg.generator.cucumber.testframework.utils.CucumberManifestWriter;
import com.scottlogic.deg.generator.decisiontree.MostProlificConstraintOptimiser;
import com.scottlogic.deg.generator.decisiontree.ProfileDecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.generation.databags.StandardRowSpecDataBagSourceFactory;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.JsonProfileReader;
import com.scottlogic.deg.generator.inputs.profileviolation.IndividualConstraintRuleViolator;
import com.scottlogic.deg.generator.inputs.profileviolation.IndividualRuleProfileViolator;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.datasetwriters.DataSetWriter;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import com.scottlogic.deg.generator.violations.ViolationGenerationEngine;
import com.scottlogic.deg.generator.walker.CartesianProductDecisionTreeWalker;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategyFactory;
import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsNot.not;

class ExampleProfilesViolationTests {

    @TestFactory
    Collection<DynamicTest> shouldReadProfileCorrectly() throws IOException {
        GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.PINNING));

        return forEachProfileFile(config, ((standard, violating, profileFile) -> {
            final Profile profile = new JsonProfileReader().read(profileFile.toPath());

            Collection<Integer> constraintsPerRule = profile.rules.stream().map(r -> r.constraints.size()).collect(Collectors.toList());
            Assert.assertThat(constraintsPerRule, not(hasItem(0))); //there should be no rules with 0 constraints
        }));
    }

    @TestFactory
    Collection<DynamicTest> shouldGenerateAsTestCasesWithoutErrors() throws IOException {
        GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.PINNING));
                
        return forEachProfileFile(config, ((standard, violating, profileFile) -> {
            final Profile profile = new JsonProfileReader().read(profileFile.toPath());
            standard.generateDataSet(profile, config, new NullOutputTarget());
        }));
    }

    @TestFactory
    Collection<DynamicTest> shouldGenerateWithoutErrors() throws IOException {
        GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.CARTESIAN_PRODUCT,
                GenerationConfig.CombinationStrategyType.PINNING));

        return forEachProfileFile(config, ((standard, violating, profileFile) -> {
            final Profile profile = new JsonProfileReader().read(profileFile.toPath());
            violating.generateDataSet(profile, config, new NullOutputTarget());
        }));
    }

    private Collection<DynamicTest> forEachProfileFile(GenerationConfig config, GenerateConsumer consumer) throws IOException {
        Collection<DynamicTest> dynamicTests = new ArrayList<>();

        File[] directoriesArray =
            Paths.get("..", "examples")
                .toFile()
                .listFiles(File::isDirectory);

        for (File dir : directoriesArray) {
            File profileFile = Paths.get(dir.getCanonicalPath(), "profile.json").toFile();

            DynamicTest test = DynamicTest.dynamicTest(dir.getName(), () -> {
                StandardGenerationEngine engine = new StandardGenerationEngine(
                    new DecisionTreeDataGenerator(
                        new CartesianProductDecisionTreeWalker(
                            new ConstraintReducer(
                                new FieldSpecFactory(new FieldSpecMerger()),
                                new FieldSpecMerger()
                            ),
                            new RowSpecMerger(new FieldSpecMerger())
                        ),
                        new RelatedFieldTreePartitioner(),
                        new MostProlificConstraintOptimiser(),
                        new NoopDataGeneratorMonitor(),
                        new StandardRowSpecDataBagSourceFactory(
                            new FieldSpecValueGenerator(
                                config,
                                new StandardFieldValueSourceEvaluator(),
                                new JavaUtilRandomNumberGenerator())),
                        new FixFieldStrategyFactory(new FieldDependencyAnalyser())),
                    new ProfileDecisionTreeFactory(),
                    new NoopDataGeneratorMonitor());
                ViolationGenerationEngine violationGenerationEngine =
                    new ViolationGenerationEngine(
                        new IndividualRuleProfileViolator(
                            new CucumberManifestWriter(),
                            null,
                            new IndividualConstraintRuleViolator(new ArrayList<>())),
                        engine);

                consumer.generate(
                    engine,
                    violationGenerationEngine,
                    profileFile);
            });

            dynamicTests.add(test);
        }

        return dynamicTests;
    }

    private class NullOutputTarget extends FileOutputTarget {
        NullOutputTarget() {
            super(null, new NullDataSetWriter());
        }

        @Override
        public void outputDataset(Stream<GeneratedObject> generatedObjects, ProfileFields profileFields) {
            // iterate through the rows - assume lazy generation, so we haven't tested unless we've exhausted the iterable

            generatedObjects.forEach(
                row -> Assert.assertThat(row, notNullValue())); // might as well assert non-null while we're at it
        }

        @Override
        public FileOutputTarget withFilename(String filename){
            return new NullOutputTarget();
        }
    }

    private class NullDataSetWriter implements DataSetWriter{
        @Override
        public Closeable openWriter(Path directory, String filenameWithoutExtension, ProfileFields profileFields) {
            return null;
        }

        @Override
        public void writeRow(Closeable closeable, GeneratedObject row) {
        }

        @Override
        public String getFileName(String fileNameWithoutExtension) {
            return null;
        }
    }

    @FunctionalInterface
    private interface GenerateConsumer {
        void generate(StandardGenerationEngine standardEngine, ViolationGenerationEngine violatingEngine, File profileFile) throws IOException, InvalidProfileException;
    }

}

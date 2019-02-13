package com.scottlogic.deg.generator.smoke_tests;

import com.scottlogic.deg.generator.StandardGenerationEngine;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.inputs.JsonProfileReader;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagSourceFactory;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.decisiontree.MostProlificConstraintOptimiser;
import com.scottlogic.deg.generator.decisiontree.ProfileDecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.validation.NoopProfileValidator;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.dataset_writers.DataSetWriter;
import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import com.scottlogic.deg.generator.violations.ViolationGenerationEngine;
import com.scottlogic.deg.generator.walker.CartesianProductDecisionTreeWalker;
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
import java.util.Collections;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.notNullValue;

class ExampleProfilesViolationTests {

    @TestFactory
    Collection<DynamicTest> shouldGenerateAsTestCasesWithoutErrors() throws IOException {
        GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.CARTESIAN_PRODUCT,
                GenerationConfig.CombinationStrategyType.PINNING));
                
        return forEachProfileFile(config, ((generationEngine, profileFile) -> {
            final Profile profile = new JsonProfileReader(new NoopProfileValidator()).read(profileFile.toPath());
            generationEngine.generateDataSet(profile, config, new NullOutputTarget());
        }));
    }

    @TestFactory
    Collection<DynamicTest> shouldGenerateWithoutErrors() throws IOException {
        GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.CARTESIAN_PRODUCT,
                GenerationConfig.CombinationStrategyType.PINNING));

        return forEachProfileFile(config, ((generationEngine, profileFile) -> {
            final Profile profile = new JsonProfileReader(new NoopProfileValidator()).read(profileFile.toPath());
            generationEngine.generateDataSet(profile, config, new NullOutputTarget());
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
                                new FieldSpecFactory(),
                                new FieldSpecMerger()
                            ),
                            new RowSpecMerger(new FieldSpecMerger())
                        ),
                        new RelatedFieldTreePartitioner(),
                        new MostProlificConstraintOptimiser(),
                        new NoopDataGeneratorMonitor(),
                        new RowSpecDataBagSourceFactory(
                            new FieldSpecValueGenerator(
                                config,
                                new StandardFieldValueSourceEvaluator(),
                                new JavaUtilRandomNumberGenerator()))),
                    new ProfileDecisionTreeFactory(),
                    new NoopDataGeneratorMonitor());
                ViolationGenerationEngine violationGenerationEngine = new ViolationGenerationEngine(null, engine, new ManifestWriter(), Collections.emptyList());

                consumer.generate(
                    violationGenerationEngine,
                    profileFile);
            });

            dynamicTests.add(test);
        }

        return dynamicTests;
    }

    private class NullOutputTarget extends FileOutputTarget {
        public NullOutputTarget() {
            super(null, new NullDataSetWriter());
        }

        @Override
        public void outputDataset(Stream<GeneratedObject> generatedObjects, ProfileFields profileFields) throws IOException {
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
        void generate(ViolationGenerationEngine engine, File profileFile) throws IOException, InvalidProfileException;
    }
}

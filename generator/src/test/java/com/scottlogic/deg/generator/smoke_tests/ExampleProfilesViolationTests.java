package com.scottlogic.deg.generator.smoke_tests;

import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.StandardGenerationEngine;
import com.scottlogic.deg.generator.config.detail.DataGenerationType;
import com.scottlogic.deg.generator.decisiontree.MaxStringLengthInjectingDecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.MostProlificConstraintOptimiser;
import com.scottlogic.deg.generator.decisiontree.ProfileDecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.generation.combinationstrategies.PinningCombinationStrategy;
import com.scottlogic.deg.generator.generation.databags.StandardRowSpecDataBagGenerator;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import com.scottlogic.deg.profile.reader.JsonProfileReader;
import com.scottlogic.deg.generator.inputs.profileviolation.IndividualConstraintRuleViolator;
import com.scottlogic.deg.generator.inputs.profileviolation.IndividualRuleProfileViolator;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.outputs.formats.DataSetWriter;
import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
import com.scottlogic.deg.generator.outputs.targets.MultiDatasetOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.SingleDatasetOutputTarget;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import com.scottlogic.deg.generator.violations.ViolationGenerationEngine;
import com.scottlogic.deg.generator.walker.CartesianProductDecisionTreeWalker;
import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.mock;

class ExampleProfilesViolationTests {

    @TestFactory
    Collection<DynamicTest> shouldReadProfileCorrectly() throws IOException {
        return forEachProfileFile(((standard, violating, profileFile) -> {
            final Profile profile = new JsonProfileReader().read(profileFile.toPath());

            Collection<Integer> constraintsPerRule = profile.rules.stream().map(r -> r.constraints.size()).collect(Collectors.toList());
            Assert.assertThat(constraintsPerRule, not(hasItem(0))); //there should be no rules with 0 constraints
        }));
    }

    @TestFactory
    Collection<DynamicTest> shouldGenerateAsTestCasesWithoutErrors() throws IOException {
        return forEachProfileFile(((standard, violating, profileFile) -> {
            final Profile profile = new JsonProfileReader().read(profileFile.toPath());
            standard.generateDataSet(profile, new NullSingleDatasetOutputTarget());
        }));
    }

    @TestFactory
    Collection<DynamicTest> shouldGenerateWithoutErrors() throws IOException {
        return forEachProfileFile(((standard, violating, profileFile) -> {
            final Profile profile = new JsonProfileReader().read(profileFile.toPath());
            violating.generateDataSet(profile, new NullMultiDatasetOutputTarget());
        }));
    }

    private Collection<DynamicTest> forEachProfileFile(GenerateConsumer consumer) throws IOException {
        Collection<DynamicTest> dynamicTests = new ArrayList<>();

        File[] directoriesArray =
            Paths.get("..", "examples")
                .toFile()
                .listFiles(File::isDirectory);

        for (File dir : directoriesArray) {
            File profileFile = Paths.get(dir.getCanonicalPath(), "profile.json").toFile();

            DynamicTest test = DynamicTest.dynamicTest(dir.getName(), () -> {
                StandardGenerationEngine standardGenerationEngine = new StandardGenerationEngine(
                    new DecisionTreeDataGenerator(
                        new CartesianProductDecisionTreeWalker(
                            new ConstraintReducer(
                                new FieldSpecFactory(new StringRestrictionsFactory()),
                                new FieldSpecMerger()
                            ),
                            new RowSpecMerger(new FieldSpecMerger())
                        ),
                        new RelatedFieldTreePartitioner(),
                        new MostProlificConstraintOptimiser(),
                        new NoopDataGeneratorMonitor(),
                        new StandardRowSpecDataBagGenerator(
                            new FieldSpecValueGenerator(
                                DataGenerationType.INTERESTING,
                                new StandardFieldValueSourceEvaluator(),
                                new JavaUtilRandomNumberGenerator()),
                            new PinningCombinationStrategy()),
                        new PinningCombinationStrategy(),
                        10),
                    new MaxStringLengthInjectingDecisionTreeFactory(new ProfileDecisionTreeFactory(), 200),
                    new NoopDataGeneratorMonitor());

                ViolationGenerationEngine violationGenerationEngine =
                    new ViolationGenerationEngine(
                        new IndividualRuleProfileViolator(
                            mock(ManifestWriter.class),
                            null,
                            new IndividualConstraintRuleViolator(new ArrayList<>())),
                        standardGenerationEngine);

                consumer.generate(
                    standardGenerationEngine,
                    violationGenerationEngine,
                    profileFile);
            });

            dynamicTests.add(test);
        }

        return dynamicTests;
    }

    private class NullSingleDatasetOutputTarget implements SingleDatasetOutputTarget {
        @Override
        public DataSetWriter openWriter(ProfileFields fields) {
            return new NullDataSetWriter();
        }

        private class NullDataSetWriter implements DataSetWriter {
            @Override
            public void writeRow(GeneratedObject row) {
                Assert.assertThat(row, notNullValue()); // non-essential, but might occasionally catch an error
            }

            @Override
            public void close() {}
        }
    }

    private class NullMultiDatasetOutputTarget implements MultiDatasetOutputTarget {
        @Override
        public SingleDatasetOutputTarget getSubTarget(String name) {
            return new NullSingleDatasetOutputTarget();
        }
    }

    @FunctionalInterface
    private interface GenerateConsumer {
        void generate(StandardGenerationEngine standardEngine, ViolationGenerationEngine violatingEngine, File profileFile) throws IOException, InvalidProfileException;
    }
}

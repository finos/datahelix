package com.scottlogic.deg.generator.decisiontree;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.test_utils.AnyOrderCollectionEqualityComparer;
import com.scottlogic.deg.generator.decisiontree.test_utils.ConstraintNodeComparer;
import com.scottlogic.deg.generator.decisiontree.test_utils.DecisionComparer;
import com.scottlogic.deg.generator.decisiontree.test_utils.DecisionTreeDto;
import com.scottlogic.deg.generator.decisiontree.test_utils.EqualityComparer;
import com.scottlogic.deg.generator.decisiontree.test_utils.OptimiseTestStrategy;
import com.scottlogic.deg.generator.decisiontree.test_utils.PartitionTestStrategy;
import com.scottlogic.deg.generator.decisiontree.test_utils.ProfileFieldComparer;
import com.scottlogic.deg.generator.decisiontree.test_utils.TreeComparer;
import com.scottlogic.deg.generator.decisiontree.test_utils.TreeComparisonContext;
import com.scottlogic.deg.generator.decisiontree.test_utils.TreeComparisonReporter;
import com.scottlogic.deg.generator.decisiontree.test_utils.TreeTransformationTestStrategy;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils.mapping.DecisionTreeMapper;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TreeTransformationIntegrationTests {
    private final IDecisionTreeGenerator decisionTreeGenerator = new DecisionTreeGenerator();
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final DecisionTreeMapper decisionTreeMapper = new DecisionTreeMapper();

    /*
     * FIXME -- wait until @steve-tennantsl renames his expected JSON files before we
     * re-enable this TestFactory
     */
    @Disabled  
    @TestFactory
    Collection<DynamicTest> decisionTreePartitioner_givenProfileInputs_resultEqualsProfileOutputs() {
    	return doTest(new PartitionTestStrategy());
    }
    
    @TestFactory
    Collection<DynamicTest> decisionTreeOptimiser_givenProfileInputs_resultEqualsProfileOutputs() {
    	return doTest(new OptimiseTestStrategy());
    }
    
    private Collection<DynamicTest> doTest(TreeTransformationTestStrategy strategy) {
        final String FS = File.separator;
        final String testsDirPathPrefix = ".." + FS + "generator" + FS + "resources" + FS;
        String testsDirPathName = testsDirPathPrefix + FS + strategy.getTestsDirName() + FS;
        TreeComparisonReporter reporter = new TreeComparisonReporter();

        return getTestFiles(testsDirPathName).map(directory -> {
            try {
                File inputFile = new File(directory.getPath() + FS + "profile.json");
                File outputFile = new File(directory.getPath() + FS + "expected.json");
                Profile inputProfile = getProfile(inputFile.toPath());

                DecisionTree beforeTree = decisionTreeGenerator.analyse(inputProfile).getMergedTree();
                
                final List<DecisionTree> actualTrees = strategy.transformTree(beforeTree);
                
                List<DecisionTreeDto> expectedTreeDto = getMappedExpectedOutput(outputFile);
                final List<DecisionTree> expectedTrees = expectedTreeDto.stream()
                    .map(decisionTreeMapper::map)
                    .collect(Collectors.toList());

                return DynamicTest.dynamicTest(directory.getName(), () -> {
                    AnyOrderCollectionEqualityComparer defaultAnyOrderCollectionEqualityComparer = new AnyOrderCollectionEqualityComparer();
                    TreeComparisonContext context = new TreeComparisonContext();
                    EqualityComparer anyOrderComparer = new AnyOrderCollectionEqualityComparer(
                        new TreeComparer(
                            new ConstraintNodeComparer(
                                context,
                                defaultAnyOrderCollectionEqualityComparer,
                                new DecisionComparer(),
                                defaultAnyOrderCollectionEqualityComparer,
                                new AnyOrderCollectionEqualityComparer(new DecisionComparer())),
                            new ProfileFieldComparer(context, defaultAnyOrderCollectionEqualityComparer, defaultAnyOrderCollectionEqualityComparer),
                            context
                        )
                    );

                    boolean match = anyOrderComparer.equals(
                        expectedTrees,
                        actualTrees);

                    if (!match) {
                        reporter.reportMessages(context);
                        Assert.fail("Trees do not match");
                    }
            });
            }
            catch (IOException | InvalidProfileException ex) {
                // Throwing RuntimeException to escape the lambda
                throw new RuntimeException(ex);
            }
        }).collect(Collectors.toList());
    }

    private Stream<File> getTestFiles(String testsDirectory) {
        return
            Stream.of(Objects.requireNonNull(Paths.get(testsDirectory)
                .toFile()
                .listFiles(File::isDirectory)));
    }

    private Profile getProfile(Path path) throws IOException, InvalidProfileException {
        return new ProfileReader().read(path);
    }

    private List<DecisionTreeDto> getMappedExpectedOutput(File file) throws IOException {
            byte[] encoded = Files.readAllBytes(file.toPath());
            String fileContents = new String(encoded, Charset.forName("UTF-8"));

            return jsonMapper.readValue(fileContents, new TypeReference<List<DecisionTreeDto>>(){});
    }
}
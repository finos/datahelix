package com.scottlogic.deg.generator.decisiontree.tree_partitioning;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils.*;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TreeTransformationIntegrationTests {
    private enum XUT { // Transformation Under Test
        TREE_PARTITION ("partitioning-tests"),
        TREE_OPTIMISE ("optimiser-tests");
        
        private String testsDirName;

        public String getTestsDirName() {
            return testsDirName;
        }

        private XUT(String testDirsName) {
            this.testsDirName = testDirsName;
        }
    }

    private final IDecisionTreeGenerator decisionTreeGenerator = new DecisionTreeGenerator();
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final DecisionTreeMapper decisionTreeMapper = new DecisionTreeMapper();

    @Disabled
    @TestFactory
    Collection<DynamicTest> decisionTreePartitioner_givenProfileInputs_resultEqualsProfileOutputs() {
    	return doTest(XUT.TREE_PARTITION);
    }
    
    @TestFactory
    Collection<DynamicTest> decisionTreeOptimiser_givenProfileInputs_resultEqualsProfileOutputs() {
    	return doTest(XUT.TREE_OPTIMISE);
    }
    
    private List<DecisionTree> transformTree(DecisionTree beforeTree, XUT xut) {
        List<DecisionTree> actualTrees = null;
        
        switch (xut) {
        case TREE_PARTITION:
            ITreePartitioner treePartitioner = new TreePartitioner();
            actualTrees = treePartitioner
                .splitTreeIntoPartitions(beforeTree)
                .collect(Collectors.toList());            
            break;
        case TREE_OPTIMISE:
            IDecisionTreeOptimiser treeOptimiser = new DecisionTreeOptimiser();            
            DecisionTree afterTree = treeOptimiser.optimiseTree(beforeTree);
            actualTrees = Collections.singletonList(afterTree);
            break;
        default:
            throw new UnsupportedOperationException("Illegal transformation under test: " 
                        + xut.name());
        }
        
        return actualTrees;
    }
    
    private Collection<DynamicTest> doTest(XUT xut) {
        final String FS = File.separator;
        final String testsDirPathPrefix = ".." + FS + "generator" + FS + "resources" + FS;
        String testsDirPathName = testsDirPathPrefix + FS + xut.getTestsDirName() + FS;
        TreeComparisonReporter reporter = new TreeComparisonReporter();

        return getTestFiles(testsDirPathName).map(directory -> {
            try {
                File inputFile = new File(directory.getPath() + FS + "profile.json");
                File outputFile = new File(directory.getPath() + FS + "expected.json");
                Profile inputProfile = getProfile(inputFile.toPath());

                DecisionTree beforeTree = decisionTreeGenerator.analyse(inputProfile).getMergedTree();
                
                final List<DecisionTree> actualTrees = transformTree(beforeTree, xut);
                
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
package com.scottlogic.deg.generator.decisiontree.tree_partitioning;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils.*;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils.mapping.DecisionTreeMapper;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import org.junit.Assert;
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


class DecisionTreePartitionerIntegrationTests {
    private static final String treePartitionerTestsDirectory = "../generator/resources/partitioning-tests/";

    private final IDecisionTreeGenerator decisionTreeGenerator = new DecisionTreeGenerator();
    private final ITreePartitioner treePartitioner = new TreePartitioner();
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final DecisionTreeMapper decisionTreeMapper = new DecisionTreeMapper();

    @TestFactory
    Collection<DynamicTest> decisionTreePartitioner_givenProfileInputs_resultEqualsProfileOutputs() {
        TreeComparisonReporter reporter = new TreeComparisonReporter();

        return getPartitioningTestsDirectory().map(directory -> {
            try {
                File inputFile = new File(directory.getPath() + "/profile.json");
                File outputFile = new File(directory.getPath() + "/expected-partitioning.json");
                Profile inputProfile = getProfile(inputFile.toPath());

                DecisionTree decisionTree = decisionTreeGenerator.analyse(inputProfile).getMergedTree();
                final List<DecisionTree> actualPartitionedTrees = treePartitioner
                    .splitTreeIntoPartitions(decisionTree)
                    .collect(Collectors.toList());

                List<DecisionTreeDto> expectedTreeDto = getMappedExpectedOutput(outputFile);
                final List<DecisionTree> expectedPartitionedTrees = expectedTreeDto.stream()
                    .map(decisionTreeMapper::map)
                    .collect(Collectors.toList());

                return DynamicTest.dynamicTest(directory.getName(), () -> {
                    TreeComparisonContext context = new TreeComparisonContext();
                    EqualityComparer anyOrderComparer = new AnyOrderCollectionEqualityComparer(
                        new TreeComparer(
                            new ConstraintNodeComparer(context),
                            new ProfileFieldComparer(context),
                            context
                        )
                    );

                    boolean match = anyOrderComparer.equals(
                        expectedPartitionedTrees,
                        actualPartitionedTrees);

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

    private Stream<File> getPartitioningTestsDirectory() {
        return
            Stream.of(Objects.requireNonNull(Paths.get(treePartitionerTestsDirectory)
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
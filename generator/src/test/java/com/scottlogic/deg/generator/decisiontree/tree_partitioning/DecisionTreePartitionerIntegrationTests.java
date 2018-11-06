package com.scottlogic.deg.generator.decisiontree.tree_partitioning;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeMatchers;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeGenerator;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils.*;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils.mapping.DecisionTreeMapper;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;

import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class DecisionTreePartitionerIntegrationTests {
    private static final String inputDirectory = "./src/test/profiles/input/";
    private static final String outputDirectory = "./src/test/profiles/output/";

    private final IDecisionTreeGenerator decisionTreeGenerator = new DecisionTreeGenerator();
    private final ITreePartitioner treePartitioner = new TreePartitioner();
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final DecisionTreeMapper decisionTreeMapper = new DecisionTreeMapper();

    @TestFactory
    Collection<DynamicTest> decisionTreePartitioner_givenProfileInputs_resultEqualsProfileOutputs() throws IOException {
        ArrayList<DynamicTest> tests = new ArrayList<>();

        TreeComparisonContext comparisonContext = new TreeComparisonContext();
        ConstraintNodeComparer constraintNodeComparer = new ConstraintNodeComparer(comparisonContext);
        IEqualityComparer treeComparer = new TreeComparer(constraintNodeComparer);
        IEqualityComparer anyOrderComparer = new AnyOrderCollectionEqualityComparer(treeComparer);

            getInputProfileFilePaths().forEach(path -> {
            String inputProfileFileName = path.getFileName().toString();
            File expectedTreeOutputFile = new File(outputDirectory + inputProfileFileName);
            Profile inputProfile = getProfile(path);

            if (!expectedTreeOutputFile.exists() || inputProfile == null) {
                Assert.fail("Could not locate file " + inputProfileFileName);
            }

            DecisionTree decisionTree = decisionTreeGenerator.analyse(inputProfile).getMergedTree();
            final List<DecisionTree> actualPartitionedTrees = treePartitioner
                    .splitTreeIntoPartitions(decisionTree)
                    .collect(Collectors.toList());

            List<DecisionTreeDto> expectedTreeDto = getMappedExpectedOutput(expectedTreeOutputFile);
            final List<DecisionTree> expectedPartitionedTrees = expectedTreeDto.stream()
                    .map(decisionTreeMapper::map)
                    .collect(Collectors.toList());

            tests.add(DynamicTest.dynamicTest(inputProfileFileName, () -> {
                String message = "";
                constraintNodeComparer.setReportErrors((missing1, missing2) -> {
                    System.out.println(String.format("-- %s --", inputProfileFileName));
                    if (!missing1.isEmpty()) {
                        System.out.println(String.format("%s: Got %s", inputProfileFileName, missing1));
                    }
                    if (!missing2.isEmpty()) {
                        System.out.println(String.format("%s: Expected %s", inputProfileFileName, missing2));
                    }

                    System.out.println("");
                });
                comparisonContext.reset();

                boolean match = anyOrderComparer.equals(
                    expectedPartitionedTrees,
                    actualPartitionedTrees);
                Assert.assertTrue(message, match);
            }));
        });

        return tests;
    }

    private Stream<Path> getInputProfileFilePaths() throws IOException {
        return Files.walk(Paths.get(inputDirectory)).filter(path -> getFilenameExtension(path.toFile().toString())
                .toLowerCase().equals("json"));
    }

    private String getFilenameExtension(String filename) {
        final int extensionIndex = filename.lastIndexOf(".");
        return filename.substring(extensionIndex + 1);
    }

    private Profile getProfile(Path path) {
        ProfileReader reader = new ProfileReader();

        try {
            return reader.read(path);
        } catch (IOException | InvalidProfileException e) {
            return null;
        }
    }

    private List<DecisionTreeDto> getMappedExpectedOutput(File file) {
        try {
            byte[] encoded = Files.readAllBytes(file.toPath());
            String fileContents = new String(encoded, Charset.forName("UTF-8"));

            return jsonMapper.readValue(fileContents, new TypeReference<List<DecisionTreeDto>>(){});
        } catch (IOException e) {
            return null;
        }
    }
}
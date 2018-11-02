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
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils.DecisionTreeDto;
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

        getFilePaths().forEach(path -> {
            String name = path.getFileName().toString();
            File expectedOutput = new File(outputDirectory + name);
            Profile profile = getProfile(path);

            if (!expectedOutput.exists() || profile == null) {
                return;
            }

            DecisionTree decisionTree = decisionTreeGenerator.analyse(profile).getMergedTree();
            final List<DecisionTree> partitionedTrees = treePartitioner
                    .splitTreeIntoPartitions(decisionTree)
                    .collect(Collectors.toList());

            List<DecisionTreeDto> expectedTreeDto = getMappedExpectedOutput(expectedOutput);
            assert expectedTreeDto != null;
            final List<DecisionTree> mappedTrees = expectedTreeDto.stream()
                    .map(decisionTreeMapper::map)
                    .collect(Collectors.toList());

//            tests.add(DynamicTest.dynamicTest(name, () -> Assert.assertThat(
//                    partitionedTrees,
//                    DecisionTreeMatchers.isEquivalentTo(mappedTrees)
//            )));

            tests.add(DynamicTest.dynamicTest(name, () -> Assert.assertThat(
                partitionedTrees, /* actual */
                DecisionTreeMatchers.isEqualTo(mappedTrees) /* expected */
            )));
        });

        return tests;
    }

    private Stream<Path> getFilePaths() throws IOException {
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
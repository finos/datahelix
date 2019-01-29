package com.scottlogic.deg.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.decisiontree.serialisation.DecisionTreeDto;
import com.scottlogic.deg.generator.decisiontree.serialisation.DecisionTreeMapper;
import com.scottlogic.deg.generator.inputs.JsonProfileReader;

import com.scottlogic.deg.generator.inputs.validation.NoopProfileValidator;
import picocli.CommandLine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@picocli.CommandLine.Command(
    name = "genTreeJson",
    description = "Produces a JSON file for the decision trees given an input DEG profile.",
    mixinStandardHelpOptions = true,
    version = "1.0")
public class GenerateTreeCollectionJson implements Runnable {
    @picocli.CommandLine.Parameters(index = "0", description = "The path of the input profile json file.")
    private File inputPath;

    @picocli.CommandLine.Parameters(index = "1", 
            description = "The path of output file into which the generated JSON file will be saved.")
    private Path outputPath;
    
    @CommandLine.Option(
            names = {"--optimise"},
            description = "Applies tree optimisation",
            hidden = true)
    private boolean doOptimise = false;

    @CommandLine.Option(
            names = {"--partition"},
            description = "Applies tree partitioning",
            hidden = true)
    private boolean doPartition = false;

    @Override
    public void run() {
        final DecisionTreeFactory profileAnalyser = new ProfileDecisionTreeFactory();
        final Profile profile;

        try {
            profile = new JsonProfileReader(new NoopProfileValidator()).read(inputPath.toPath());
        } catch (Exception e) {
            System.err.println("Failed to read file!");
            e.printStackTrace();
            return;
        }

        final DecisionTreeCollection decisionTreeCollection = profileAnalyser.analyse(profile);
        final DecisionTree mergedTree = decisionTreeCollection.getMergedTree();

                
        DecisionTreeOptimiser treeOptimiser = doOptimise
                ? new MostProlificConstraintOptimiser()
                : new NoopDecisionTreeOptimiser();
                
        final List<DecisionTree> listOfTree = Stream.of(mergedTree)
                        .map(treeOptimiser::optimiseTree)
                    .collect(Collectors.toList());
        
        final DecisionTreeMapper decisionTreeMapper = new DecisionTreeMapper();
        
        List<DecisionTreeDto> listOfDto = listOfTree.stream()
                .map(decisionTreeMapper::toDto)
                .collect(Collectors.toList());
            
        ObjectMapper om = new ObjectMapper();
        
        om.enable(SerializationFeature.INDENT_OUTPUT);
        try (BufferedWriter bw = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            String s = om.writeValueAsString(listOfDto);
            bw.write(s);
        } catch (JsonProcessingException e2) {
            System.err.format("JSON error%n");
            e2.printStackTrace();
        } catch (IOException e1) {
            System.err.format("IOException when writing file %s%n", outputPath);
            e1.printStackTrace();
        }         
    }
}

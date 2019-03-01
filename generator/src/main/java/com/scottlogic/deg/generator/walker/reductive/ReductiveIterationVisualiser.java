package com.scottlogic.deg.generator.walker.reductive;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.visualisation.DecisionTreeVisualisationWriter;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class ReductiveIterationVisualiser implements IterationVisualiser {
    private final AtomicInteger currentIteration = new AtomicInteger();
    private final FileUtils fileUtils;
    private final Path visualiseDirectoryPath;

    @Inject
    public ReductiveIterationVisualiser(
        @Named("outputPath") Path outputPath,
        FileUtils fileUtils) {
        this.fileUtils = fileUtils;
        boolean outputPathIsADirectory = outputPath != null && outputPath.toFile().isDirectory();

        Path directoryPath;
        if (outputPathIsADirectory) {
            directoryPath = outputPath;
        } else {
            directoryPath = outputPath == null || outputPath.getParent() == null
                ? Paths.get(System.getProperty("user.dir"))
                : outputPath.getParent();
        }

        this.visualiseDirectoryPath = directoryPath.resolve("reductive-walker");
    }

    @Override
    public void visualise(ConstraintNode rootNode, ReductiveState reductiveState) throws IOException {
        createVisualiseDirectoryIfAbsent();

        int iteration = currentIteration.getAndIncrement();

        ProfileFields profileFields = reductiveState.getFields();
        String description = String.format("Iteration %d\n%s", iteration, reductiveState.toString(true));
        Path outputPath = visualiseDirectoryPath.resolve(String.format("Reduced-tree-%03d.gv", iteration));

        //copy of Visualise.writeTreeTo()
        try (OutputStreamWriter outWriter = new OutputStreamWriter(
            new FileOutputStream(outputPath.toString()),
            StandardCharsets.UTF_8)) {

            new DecisionTreeVisualisationWriter(outWriter).writeDot(
                new DecisionTree(rootNode, profileFields, description),
                "tree",
                description);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createVisualiseDirectoryIfAbsent() throws IOException {
        File directory = this.visualiseDirectoryPath.toFile();
        if (directory.exists()){
            if (directory.isDirectory()){
                return; //exists
            }

            throw new IOException("Cannot visualise iterations of the tree, a file exists at the path where the" +
                " directory needs to exist.\n" + this.visualiseDirectoryPath.toString());
        }

        fileUtils.createDirectories(this.visualiseDirectoryPath);
    }
}

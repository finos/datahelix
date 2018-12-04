package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.visualisation.DecisionTreeVisualisationWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class ReductiveIterationVisualiser implements IterationVisualiser {
    private final AtomicInteger currentIteration = new AtomicInteger();

    @Override
    public void visualise(ConstraintNode rootNode, FieldCollection fieldCollection) {
        int iteration = currentIteration.getAndIncrement();

        ProfileFields profileFields = fieldCollection.getFields();
        String description = String.format("Iteration %d\n%s", iteration, fieldCollection.toString(true));
        Path outputPath = FileSystems.getDefault().getPath("reductive-walker", String.format("Reduced-tree-%03d.gv", iteration));

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
}

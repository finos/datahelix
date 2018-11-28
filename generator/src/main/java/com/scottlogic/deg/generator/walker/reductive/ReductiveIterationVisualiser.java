package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Visualise;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class ReductiveIterationVisualiser implements IterationVisualiser {
    private final AtomicInteger currentIteration = new AtomicInteger();

    @Override
    public void visualise(ConstraintNode rootNode, FieldCollection fieldCollection) {
        int iteration = currentIteration.getAndIncrement();

        Visualise vis = new Visualise();
        ProfileFields profileFields = fieldCollection.getFields();
        String description = String.format("Iteration %d\n%s", iteration, fieldCollection.toString(true));
        Path outputPath = FileSystems.getDefault().getPath("reductive-walker", String.format("Reduced-tree-%03d.gv", iteration));

        try {
            vis.writeTreeTo(new DecisionTree(rootNode, profileFields, description), description, outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

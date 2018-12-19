package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;

import java.io.IOException;

public interface IterationVisualiser {
    void visualise(ConstraintNode rootNode, ReductiveState reductiveState) throws IOException;
}

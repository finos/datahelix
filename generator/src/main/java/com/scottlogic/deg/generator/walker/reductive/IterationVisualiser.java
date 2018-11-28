package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;

public interface IterationVisualiser {
    void visualise(ConstraintNode rootNode, FieldCollection fieldCollection);
}

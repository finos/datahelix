package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;

public interface FixFieldStrategy {
    Field getNextFieldToFix(FieldCollection fieldCollection, ReductiveConstraintNode rootNode);
}


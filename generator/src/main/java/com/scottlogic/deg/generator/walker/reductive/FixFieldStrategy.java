package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;

public interface FixFieldStrategy {
    FieldAndConstraintMapping getFieldAndConstraintMapToFixNext(ReductiveConstraintNode rootNode);
}


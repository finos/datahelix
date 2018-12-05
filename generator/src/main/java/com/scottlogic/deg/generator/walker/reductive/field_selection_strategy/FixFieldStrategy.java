package com.scottlogic.deg.generator.walker.reductive.field_selection_strategy;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.walker.reductive.FieldCollection;

public interface FixFieldStrategy {
    Field getNextFieldToFix(FieldCollection fieldCollection, ReductiveConstraintNode rootNode);
}


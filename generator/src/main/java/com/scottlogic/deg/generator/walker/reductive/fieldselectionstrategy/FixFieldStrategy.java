package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;

public interface FixFieldStrategy {
    Field getNextFieldToFix(ReductiveState reductiveState, ConstraintNode rootNode);
}

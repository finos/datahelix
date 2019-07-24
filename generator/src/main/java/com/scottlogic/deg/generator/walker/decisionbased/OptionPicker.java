package com.scottlogic.deg.generator.walker.decisionbased;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;

import java.util.stream.Stream;

public interface OptionPicker {
    DecisionNode pickDecision (ConstraintNode constraintNode);
    Stream<ConstraintNode> streamOptions(DecisionNode decisionNode);
}

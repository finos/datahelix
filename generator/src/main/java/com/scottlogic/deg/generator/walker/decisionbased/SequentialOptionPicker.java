package com.scottlogic.deg.generator.walker.decisionbased;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;

import java.util.stream.Stream;

public class SequentialOptionPicker implements OptionPicker {
    @Override
    public DecisionNode pickDecision(ConstraintNode constraintNode) {
        return constraintNode.getDecisions().iterator().next();
    }

    @Override
    public Stream<ConstraintNode> streamOptions(DecisionNode decisionNode) {
        return decisionNode.getOptions().stream();
    }
}

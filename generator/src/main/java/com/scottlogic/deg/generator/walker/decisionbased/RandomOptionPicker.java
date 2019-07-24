package com.scottlogic.deg.generator.walker.decisionbased;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Stream;

public class RandomOptionPicker implements OptionPicker {
    private final Random random;

    public RandomOptionPicker() {
        this.random = new Random();
    }

    @Override
    public DecisionNode pickDecision(ConstraintNode constraintNode) {
        return constraintNode.getDecisions().stream()
            .skip(random.nextInt(constraintNode.getDecisions().size()))
            .findFirst().get();
    }

    @Override
    public Stream<ConstraintNode> streamOptions(DecisionNode decisionNode) {
        ArrayList options = new ArrayList(decisionNode.getOptions());
        Collections.shuffle(options);
        return options.stream();
    }
}

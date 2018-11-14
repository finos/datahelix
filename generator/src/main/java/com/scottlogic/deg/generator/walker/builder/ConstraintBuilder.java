package com.scottlogic.deg.generator.walker.builder;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.walker.ConstraintIterator;
import com.scottlogic.deg.generator.walker.LeafConstraintIterator;

import java.util.ArrayList;

public class ConstraintBuilder {

    public static IConstraintIterator build(ConstraintNode constraintNode, int decisionIndexFromParent){
        if (constraintNode.getDecisions().isEmpty()){
            return new LeafConstraintIterator(decisionIndexFromParent);
        }

        IDecisionIterator decisions = DecisionBuilder.build(new ArrayList<>(constraintNode.getDecisions()));
        return new ConstraintIterator(decisions, decisionIndexFromParent);
    }

    public static IConstraintIterator build(ConstraintNode constraintNode){
        return build(constraintNode, 0);
    }
}

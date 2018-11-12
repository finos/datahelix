package com.scottlogic.deg.generator.walker.builder;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.walker.ConstraintIterator;
import com.scottlogic.deg.generator.walker.LeafConstraintIterator;

public class ConstraintBuilder {

    public static IConstraintIterator build(ConstraintNode constraintNode, int decisionIndexFromParent){
        if (constraintNode.getDecisions().isEmpty()){
            return new LeafConstraintIterator(decisionIndexFromParent);
        }
        return new ConstraintIterator(constraintNode, decisionIndexFromParent);
    }

    public static IConstraintIterator build(ConstraintNode constraintNode){
        return build(constraintNode, 0);
    }
}

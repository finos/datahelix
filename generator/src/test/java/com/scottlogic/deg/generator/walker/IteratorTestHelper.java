package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsEqualToConstantConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.decisiontree.TreeDecisionNode;

import java.util.Arrays;
import java.util.Collections;

public class IteratorTestHelper {
    static ConstraintNode endConstraint(String name) { return constraint(name); }
    static DecisionNode singleDecision() { return new TreeDecisionNode(endConstraint("single")); }
    static DecisionNode doubleDecision(String name) {
        return new TreeDecisionNode(
            endConstraint(name + " left"), endConstraint(name + " right")); }
    static ConstraintNode constraintSingle() {
        return constraint("constraintSingle", singleDecision()); }
    static ConstraintNode constraintDouble() { return constraint("constraintDouble", doubleDecision("")); }

    static ConstraintNode constraintSingleDouble() { return constraint("constraintSingleDouble",
        singleDecision(), doubleDecision("right")); }

    static ConstraintNode constraintDoubleDouble(String name) { return constraint(name,
        doubleDecision(" left"), doubleDecision(" right")); }

    static ConstraintNode constraintTripleDouble() { return constraint("constraintTripleDouble",
        doubleDecision("left"), doubleDecision("middle"), doubleDecision("right")); }

    static ConstraintNode constraintDoubleLayered(){return constraint("constraintDoubleLayered",
            doubleDecision("left"), new TreeDecisionNode(constraintDoubleDouble("right")));
    }

    static ConstraintNode constraintBiggy(){ return constraint("constraintBiggy",
            new TreeDecisionNode(constraintDoubleDouble("left left"), endConstraint("left right")),
            new TreeDecisionNode(constraintDoubleDouble("right left"), endConstraint("right right")));
    }



    private static ConstraintNode constraint(String name, DecisionNode... decisions){
        return new TreeConstraintNode(
            Collections.singletonList(new IsEqualToConstantConstraint(new Field(name), name, Collections.emptySet())),
            Arrays.asList(decisions));
    }
}

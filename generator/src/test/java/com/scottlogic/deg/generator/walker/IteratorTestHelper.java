package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IsEqualToConstantConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.decisiontree.TreeDecisionNode;

import java.util.Arrays;
import java.util.Collections;

public class IteratorTestHelper {
    static ConstraintNode endConstraint() { return constraint("endConstraint"); }
    static DecisionNode singleDecision() { return new TreeDecisionNode(endConstraint()); }
    static DecisionNode doubleDecision() { return new TreeDecisionNode(endConstraint(), endConstraint()); }
    static ConstraintNode constraintSingle() {
        return constraint("constraintSingle", singleDecision()); }
    static ConstraintNode constraintDouble() { return constraint("constraintDouble", doubleDecision()); }
    static ConstraintNode constraintSingleDouble() { return constraint("constraintSingleDouble",
        singleDecision(), doubleDecision()); }
    static ConstraintNode constraintDoubleDouble() { return constraint("constraintDoubleDouble",
        doubleDecision(), doubleDecision()); }
    static ConstraintNode constraintTripleDouble() { return constraint("constraintTripleDouble",
        doubleDecision(), doubleDecision(), doubleDecision()); }
    static ConstraintNode constraintDoubleLayered(){return constraint("constraintDoubleLayered",
            doubleDecision(), new TreeDecisionNode(constraintDoubleDouble()));
    }
    static ConstraintNode constraintBiggy(){ return constraint("constraintBiggy",
            new TreeDecisionNode(constraintDoubleDouble(), endConstraint()),
            new TreeDecisionNode(constraintDoubleDouble(), endConstraint()));
    }



    private static ConstraintNode constraint(String name, DecisionNode... decisions){
        return new TreeConstraintNode(
            Collections.singletonList(new IsEqualToConstantConstraint(new Field(name), name)),
            Arrays.asList(decisions));
    }
}

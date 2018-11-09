package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;

import java.util.Arrays;
import java.util.Collections;

public class IteratorTestHelper {
    static ConstraintNode endConstraint() { return new ConstraintNode(); }
    static DecisionNode singleDecision() { return new DecisionNode(endConstraint()); }
    static DecisionNode doubleDecision() { return new DecisionNode(endConstraint(), endConstraint()); }
    static ConstraintNode constraintSingle() {
        return new ConstraintNode(Collections.emptyList(), Arrays.asList(singleDecision())); }
    static ConstraintNode constraintDouble() {
        return new ConstraintNode(Collections.emptyList(), Arrays.asList(doubleDecision())); }
    static ConstraintNode constraintSingleDouble() {
        return new ConstraintNode(Collections.emptyList(), Arrays.asList(singleDecision(), doubleDecision())); }
    static ConstraintNode constraintDoubleDouble() { return new ConstraintNode(Collections.emptyList(),
        Arrays.asList(doubleDecision(), doubleDecision())); }
    static ConstraintNode constraintTripleDouble() { return new ConstraintNode(Collections.emptyList(),
        Arrays.asList(doubleDecision(), doubleDecision(), doubleDecision())); }
    static ConstraintNode constraintDoubleLayered(){
        return new ConstraintNode(Collections.emptyList(), Arrays.asList(doubleDecision(),
            new DecisionNode(constraintDoubleDouble())));
    }
}

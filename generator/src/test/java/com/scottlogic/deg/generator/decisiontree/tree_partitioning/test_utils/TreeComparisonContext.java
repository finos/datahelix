package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class TreeComparisonContext {
    public DecisionTree expectedTree;
    public DecisionTree actualTree;

    private final ArrayList<Error> errors = new ArrayList<>();
    private final Stack<StackEntry> stack = new Stack<>();

    public void pushToStack(Object expected, Object actual) {
        stack.push(new StackEntry(expected, actual));
    }

    public void popFromStack(){
        stack.pop();
    }

    class StackEntry {
        public final Object expected;
        public final Object actual;

        public StackEntry(Object expected, Object actual) {
            this.expected = expected;
            this.actual = actual;
        }
    }

    class Error {
        public final ErrorContext expected;
        public final ErrorContext actual;
        public final TreeElementType type;
        public final StackEntry[] stack;

        public Error(ErrorContext expected, ErrorContext actual, TreeElementType type, Stack<StackEntry> currentStack) {
            this.expected = expected;
            this.actual = actual;
            this.type = type;
            this.stack = new StackEntry[currentStack.size()];
            currentStack.copyInto(this.stack);
        }
    }

    class ErrorContext {
        public final DecisionTree tree;
        public final Object value;

        public ErrorContext(DecisionTree tree, Object value) {
            this.tree = tree;
            this.value = value;
        }
    }

    public enum TreeElementType {
        Decision,
        AtomicConstraint
    }

    public void setTrees(DecisionTree expectedTree, DecisionTree actualTree) {
        this.expectedTree = expectedTree;
        this.actualTree = actualTree;
    }

    public Collection<Error> getErrors(){
        return this.errors;
    }

    public void reportDecisionDifferences(ArrayList missingExpectedDecisions, ArrayList missingActualDecisions) {
        errors.add(new Error(
            new ErrorContext(expectedTree, missingExpectedDecisions),
            new ErrorContext(actualTree, missingActualDecisions),
            TreeElementType.Decision,
            this.stack));
    }

    public void reportAtomicConstraintDifferences(
        ArrayList missingExpectedAtomicConstraints,
        ArrayList missingActualAtomicConstraints) {
        errors.add(new Error(
            new ErrorContext(expectedTree, missingExpectedAtomicConstraints),
            new ErrorContext(actualTree, missingActualAtomicConstraints),
            TreeElementType.AtomicConstraint,
            this.stack));
    }
}


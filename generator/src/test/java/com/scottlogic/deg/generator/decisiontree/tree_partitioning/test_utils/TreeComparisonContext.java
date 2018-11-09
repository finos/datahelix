package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class TreeComparisonContext {
    private DecisionTree expectedTree;
    private DecisionTree actualTree;

    private final ArrayList<Error> errors = new ArrayList<>();
    private final Stack<StackEntry> stack = new Stack<>();

    public void pushToStack(Object expected, Object actual) {
        stack.push(new StackEntry(expected, actual));
    }

    public void popFromStack() {
        stack.pop();
    }

    public void setTrees(DecisionTree expectedTree, DecisionTree actualTree) {
        this.expectedTree = expectedTree;
        this.actualTree = actualTree;
    }

    public DecisionTree getExpectedTree() {
        return this.expectedTree;
    }

    public DecisionTree getActualTree() {
        return this.actualTree;
    }

    public Collection<Error> getErrors() {
        return this.errors;
    }

    public void reportDifferences(Collection missingExpected, Collection missingActual, TreeElementType elementType) {
        errors.add(new Error(
            new ErrorContext(expectedTree, missingExpected),
            new ErrorContext(actualTree, missingActual),
            elementType,
            this.stack));
    }

    class StackEntry {
        final Object expected;
        final Object actual;

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
        AtomicConstraint,
        Fields
    }
}


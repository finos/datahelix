package com.scottlogic.deg.generator.decisiontree.testutils;

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
        return expectedTree;
    }

    public DecisionTree getActualTree() {
        return actualTree;
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
        final ErrorContext expected;
        final ErrorContext actual;
        final TreeElementType type;
        final StackEntry[] stack;

        public Error(ErrorContext expected, ErrorContext actual, TreeElementType type, Stack<StackEntry> currentStack) {
            this.expected = expected;
            this.actual = actual;
            this.type = type;
            this.stack = new StackEntry[currentStack.size()];
            currentStack.copyInto(this.stack);
        }
    }

    class ErrorContext {
        final DecisionTree tree;
        final Object value;

        public ErrorContext(DecisionTree tree, Object value) {
            this.tree = tree;
            this.value = value;
        }
    }

    public enum TreeElementType {
        DECISION,
        ATOMIC_CONSTRAINT,
        FIELDS
    }
}


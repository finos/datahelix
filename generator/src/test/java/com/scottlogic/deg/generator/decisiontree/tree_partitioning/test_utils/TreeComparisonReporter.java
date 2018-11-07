package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreeComparisonReporter {
    public void reportMessages(TreeComparisonContext context) {
        System.out.println(String.format("--- Looking for: %s --- ", context.expectedTree));

        for (TreeComparisonContext.Error error : context.getErrors()){
            writeError(error);
        }

        System.out.println("");
    }


    public void writeError(TreeComparisonContext.Error error){
        System.out.println(" " + error.expected.tree + ": " + this.getExpectedMessage(error));
        System.out.println(" Path: " + getExpectedPath(error.stack));
        System.out.println(" " + error.actual.tree + ": " + this.getActualMessage(error));
        System.out.println(" Path: " + getActualPath(error.stack));
        System.out.println("");
    }

    private String getActualPath(TreeComparisonContext.StackEntry[] stack) {
        return getPathMessage(Arrays.stream(stack).map(se -> se.actual));
    }

    private String getExpectedPath(TreeComparisonContext.StackEntry[] stack) {
        return getPathMessage(Arrays.stream(stack).map(se -> se.expected));
    }

    private String getPathMessage(Stream<Object> path){
        return "\\" + String.join(
            "\\",
            path.map(e -> e == null ? "<null>" : e.getClass().getSimpleName()).collect(Collectors.toList()));
    }

    private String getExpectedMessage(TreeComparisonContext.Error error){
        return getMessage(error.type, "Expected", error.expected.value);
    }

    private String getActualMessage(TreeComparisonContext.Error error){
        return getMessage(error.type, "Got", error.actual.value);
    }

    private String getMessage(TreeComparisonContext.TreeElementType type, String prefix, Object value){
        return String.format("%s %s %s", prefix, type.toString(), value);
    }
}

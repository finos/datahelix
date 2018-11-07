package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

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
        System.out.println(" " + error.actual.tree + ": " + this.getActualMessage(error));
        System.out.println("");
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

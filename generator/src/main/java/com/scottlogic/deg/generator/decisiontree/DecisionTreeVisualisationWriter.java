package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.IConstraint;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.stream.Collectors;

public class DecisionTreeVisualisationWriter {
    private final Writer outputStringWriter;
    private final boolean shouldWriteRootInfo;
    private final boolean shouldWriteOptionInfo;
    private final boolean shouldWriteDecisionNodeInfo;
    private int nextId = 0;

    public DecisionTreeVisualisationWriter(Writer stringWriter) {
        this(stringWriter, true, false, false);
    }

    public DecisionTreeVisualisationWriter(Writer stringWriter, boolean shouldWriteRootInfo, boolean shouldWriteOptionInfo, boolean shouldWriteDecisionNodeInfo) {
        this.outputStringWriter = stringWriter;
        this.shouldWriteRootInfo = shouldWriteRootInfo;
        this.shouldWriteOptionInfo = shouldWriteOptionInfo;
        this.shouldWriteDecisionNodeInfo = shouldWriteDecisionNodeInfo;
    }

    /**
     * Generates a DOT formatted representation of the decision tree
     * See https://en.wikipedia.org/wiki/DOT_(graph_description_language)
     * @param graphName The name of the generated graph notation
     * @param description This string is added as a comment at the beginning of the exported file for added context
     * @return
     */
    public void writeDot(DecisionTree decisionTree, String graphName, String description) throws IOException {
        writeLine("graph " + graphName + " {");

        writeLine("  bgcolor=\"transparent\"");

        if (description != null && description.length() > 0) {
            writeLine("  label=\"" + description + "\""); // NOTE: no effort at escaping "s
            writeLine("  labelloc=\"t\"");
            writeLine("  fontsize=\"20\"");
        }

        TreeInfo info = visit(decisionTree.getRootNode(), null);

        if (this.shouldWriteRootInfo) {
            writeTreeInfo(info, null, "red");
        }

        writeLine("}");
    }

    private TreeInfo visit(ConstraintNode constraintNode, String parentNodeId) throws IOException {
        String nodeId = "c" + nextId++;

        TreeInfo treeInfo = new TreeInfo();
        treeInfo.constraintNodes = 1;
        treeInfo.rowSpecs = 1;

        declareConstraintNode(nodeId, constraintNode.getAtomicConstraints(), treeInfo);

        if (parentNodeId != null) {
            declareParenthood(parentNodeId, nodeId);
        }

        for (DecisionNode decisionNode : constraintNode.getDecisions()) {
            TreeInfo thisOptionTreeInfo = visit(decisionNode, nodeId);

            treeInfo.addExceptRowSpecCount(thisOptionTreeInfo);
            treeInfo.rowSpecs *= thisOptionTreeInfo.rowSpecs;
        }

        if (constraintNode.getDecisions().isEmpty() && this.shouldWriteDecisionNodeInfo && parentNodeId != null) {
            writeTreeInfo(treeInfo, nodeId, "blue");
        }

        return treeInfo;
    }

    private TreeInfo visit(DecisionNode decisionNode, String parentNodeId) throws IOException {
        String nodeId = "d" + nextId++;

        declareDecisionNode(nodeId);
        declareParenthood(parentNodeId, nodeId);

        TreeInfo treeInfo = new TreeInfo();
        treeInfo.decisions = 1;
        for (ConstraintNode childNode : decisionNode.getOptions()) {
            TreeInfo thisConstraintTreeInfo = visit(childNode, nodeId);

            treeInfo.addExceptRowSpecCount(thisConstraintTreeInfo);
            treeInfo.rowSpecs += thisConstraintTreeInfo.rowSpecs;
        }

        if (this.shouldWriteOptionInfo) {
            writeTreeInfo(treeInfo, nodeId, "green");
        }

        return treeInfo;
    }

    private void declareDecisionNode(String id) throws IOException {
        writeLine("  " + id + "[bgcolor=\"white\"][label=\"\"][shape=invtriangle]");
    }

    private void declareConstraintNode(String id, Collection<IConstraint> constraints, TreeInfo treeInfo) throws IOException {
        String label = constraints
            .stream()
            .map(IConstraint::toDotLabel)
            .collect(Collectors.joining("\r\n"));
        treeInfo.atomicConstraints += constraints.size();

        writeLine("  " + id + "[bgcolor=\"white\"][fontsize=\"12\"][label=\"" + label + "\"][shape=box]");
    }

    private void declareParenthood(String parentNodeId, String childNodeId) throws IOException {
        writeLine("  " + parentNodeId + " -- " + childNodeId);
    }

    private void writeTreeInfo(TreeInfo info, String parentNodeId, String fontColour) throws IOException {
        String infoNodeId = "c" + nextId++;

        if (parentNodeId != null) {
            declareParenthood(parentNodeId, infoNodeId);
        }

        writeLine(String.format(
            "%s[fontcolor=\"%s\"][label=\"%s\"][fontsize=\"10\"][shape=box][style=\"dotted\"]",
            infoNodeId,
            fontColour,
            String.format("Counts:\nDecisions: %d\nAtomic constraints: %d\nConstraints: %d\nExpected RowSpecs: %d",
                info.decisions,
                info.atomicConstraints,
                info.constraintNodes,
                info.rowSpecs)));
    }

    private void writeLine(String line) throws IOException {
        outputStringWriter.write(line);
        outputStringWriter.write(System.lineSeparator());
    }

    private class TreeInfo
    {
        int constraintNodes = 0;
        int atomicConstraints = 0;
        int decisions = 0;
        int rowSpecs = 0;

        public void addExceptRowSpecCount(TreeInfo tree){
            constraintNodes += tree.constraintNodes;
            atomicConstraints += tree.atomicConstraints;
            decisions += tree.decisions;
        }
    }
}

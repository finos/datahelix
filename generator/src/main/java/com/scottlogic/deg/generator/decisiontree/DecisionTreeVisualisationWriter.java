package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.IConstraint;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.stream.Collectors;

public class DecisionTreeVisualisationWriter {
    private final Writer outputStringWriter;
    private int nextId = 0;

    public DecisionTreeVisualisationWriter(Writer stringWriter) {
        this.outputStringWriter = stringWriter;
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

        TreeInfo info = new TreeInfo();
        visit(decisionTree.getRootNode(), null, info);

        writeLine(String.format(
                "c%d[fontcolor=\"red\"][label=\"%s\"][fontsize=\"10\"][shape=box][style=\"dotted\"]",
                nextId++,
                String.format("Counts:\nDecisions: %d\nAtomic constraints: %d\nConstraints: %d",
                    info.decisions,
                    info.atomicConstraints,
                    info.constraintNodes)));

        writeLine("}");
    }

    private void visit(ConstraintNode constraintNode, String parentNodeId, TreeInfo treeInfo) throws IOException {
        String nodeId = "c" + nextId++;

        treeInfo.constraintNodes++;
        declareConstraintNode(nodeId, constraintNode, treeInfo);

        if (parentNodeId != null) {
            declareParenthood(parentNodeId, nodeId);
        }

        for (DecisionNode decisionNode : constraintNode.getDecisions()) {
            treeInfo.decisions++;
            visit(decisionNode, nodeId, treeInfo);
        }
    }

    private void visit(DecisionNode decisionNode, String parentNodeId, TreeInfo treeInfo) throws IOException {
        String nodeId = "d" + nextId++;

        declareDecisionNode(nodeId);
        declareParenthood(parentNodeId, nodeId);

        for (ConstraintNode childNode : decisionNode.getOptions()) {
            visit(childNode, nodeId, treeInfo);
        }
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

    private void writeLine(String line) throws IOException {
        outputStringWriter.write(line);
        outputStringWriter.write(System.lineSeparator());
    }

    class TreeInfo
    {
        int constraintNodes = 0;
        int atomicConstraints = 0;
        int decisions = 0;
    }
}

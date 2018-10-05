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

        if (description != null) {
            writeLine("  label=\"" + description + "\""); // NOTE: no effort at escaping "s
            writeLine("  labelloc=\"t\"");
            writeLine("  fontsize=\"20\"");
        }

        visit(decisionTree.getRootNode(), null);

        writeLine("}");
    }

    private void visit(ConstraintNode constraintNode, String parentNodeId) throws IOException {
        String nodeId = "c" + nextId++;

        declareConstraintNode(nodeId, constraintNode.getAtomicConstraints());

        if (parentNodeId != null) {
            declareParenthood(parentNodeId, nodeId);
        }

        for (DecisionNode decisionNode : constraintNode.getDecisions()) {
            visit(decisionNode, nodeId);
        }
    }

    private void visit(DecisionNode decisionNode, String parentNodeId) throws IOException {
        String nodeId = "d" + nextId++;

        declareDecisionNode(nodeId);
        declareParenthood(parentNodeId, nodeId);

        for (ConstraintNode childNode : decisionNode.getOptions()) {
            visit(childNode, nodeId);
        }
    }

    private void declareDecisionNode(String id) throws IOException {
        writeLine("  " + id + "[label=\"\"][shape=invtriangle]");
    }

    private void declareConstraintNode(String id, Collection<IConstraint> constraints) throws IOException {
        String label = constraints
            .stream()
            .map(IConstraint::toDotLabel)
            .collect(Collectors.joining("\r\n"));

        writeLine("  " + id + "[fontsize=\"12\"][label=\"" + label + "\"][shape=box]");
    }

    private void declareParenthood(String parentNodeId, String childNodeId) throws IOException {
        writeLine("  " + parentNodeId + " -- " + childNodeId);
    }

    private void writeLine(String line) throws IOException {
        outputStringWriter.write(line);
        outputStringWriter.write(System.lineSeparator());
    }
}

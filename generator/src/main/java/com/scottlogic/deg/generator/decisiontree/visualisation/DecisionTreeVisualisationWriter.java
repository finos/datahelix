/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.decisiontree.visualisation;

import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.*;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Collectors;

public class DecisionTreeVisualisationWriter {
    private final Writer outputStringWriter;
    private final boolean shouldWriteRootInfo;
    private final boolean shouldWriteOptionInfo;
    private final boolean shouldWriteDecisionNodeInfo;
    private int nextId = 0;
    private final NodeVisualiser nodeWriter = new NodeVisualiser();

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
        treeInfo.rowSpecs = BigInteger.valueOf(1);

        declareConstraintNode(constraintNode, nodeId, treeInfo);

        if (parentNodeId != null) {
            declareParenthood(parentNodeId, nodeId);
        }

        for (DecisionNode decisionNode : constraintNode.getDecisions()) {
            TreeInfo thisOptionTreeInfo = visit(decisionNode, nodeId);

            treeInfo.addExceptRowSpecCount(thisOptionTreeInfo);
            treeInfo.rowSpecs = treeInfo.rowSpecs.multiply(thisOptionTreeInfo.rowSpecs);
        }

        if (constraintNode.getDecisions().isEmpty() && this.shouldWriteDecisionNodeInfo && parentNodeId != null) {
            writeTreeInfo(treeInfo, nodeId, "blue");
        }

        return treeInfo;
    }

    private TreeInfo visit(DecisionNode decisionNode, String parentNodeId) throws IOException {
        String nodeId = "d" + nextId++;

        declareDecisionNode(decisionNode, nodeId);
        declareParenthood(parentNodeId, nodeId);

        TreeInfo treeInfo = new TreeInfo();
        treeInfo.decisions = 1;
        for (ConstraintNode childNode : decisionNode.getOptions()) {
            TreeInfo thisConstraintTreeInfo = visit(childNode, nodeId);

            treeInfo.addExceptRowSpecCount(thisConstraintTreeInfo);
            treeInfo.rowSpecs = treeInfo.rowSpecs.add(thisConstraintTreeInfo.rowSpecs);
        }

        if (this.shouldWriteOptionInfo) {
            writeTreeInfo(treeInfo, nodeId, "green");
        }

        return treeInfo;
    }

    private void declareDecisionNode(DecisionNode decisionNode, String id) throws IOException {
        writeLine(nodeWriter.renderNode(id, decisionNode));
    }

    private void declareConstraintNode(ConstraintNode constraintNode, String id, TreeInfo treeInfo) throws IOException {
        treeInfo.atomicConstraints += constraintNode.getAtomicConstraints().size();
        writeLine(nodeWriter.renderNode(id, constraintNode));
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
            String.format("Counts:\nDecisions: %d\nAtomic constraints: %d\nConstraints: %d\nExpected RowSpecs: %s",
                info.decisions,
                info.atomicConstraints,
                info.constraintNodes,
                info.rowSpecs.compareTo(BigInteger.valueOf(1000)) > 0
                    ? info.getRowSpecCountInScientificNotation()
                    : info.rowSpecs.longValue())));
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
        BigInteger rowSpecs = BigInteger.valueOf(0);

        public void addExceptRowSpecCount(TreeInfo tree){
            atomicConstraints += tree.atomicConstraints;
            constraintNodes += tree.constraintNodes;
            decisions += tree.decisions;
        }

        public String getRowSpecCountInScientificNotation() {
            BigDecimal decimal = new BigDecimal(rowSpecs);
            NumberFormat formatter = new DecimalFormat(
                "0.########E0",
                DecimalFormatSymbols.getInstance(Locale.ROOT));
            return formatter.format(decimal);
        }
    }
}

class NodeVisualiser {

    private final int MAX_LENGTH_FOR_LABEL = 16816;

    String renderNode(String id, DecisionNode node){
        return "  " + id + determineNodeColour(node) + "[bgcolor=\"white\"][label=\"\"][shape=invtriangle]";
    }

    String renderNode(String id, ConstraintNode node){
        String label = node.getAtomicConstraints()
            .stream()
            .sorted(Comparator.comparing(ac -> ac.getField().name))
            .map(AtomicConstraint::toString)
            .collect(Collectors.joining("\r\n"));

        if (label.length() > MAX_LENGTH_FOR_LABEL) {
            String suffix = "...";
            label = label.substring(0, MAX_LENGTH_FOR_LABEL - suffix.length() - 1) + suffix;
        }

        return "  " + id + determineNodeColour(node) + "[bgcolor=\"white\"][fontsize=\"12\"][label=\"" + label + "\"][shape=box]";
    }

    private String determineNodeColour(Node node){
        if (node.hasMarking(NodeMarking.VIOLATED)){
            return "[color=\"green\"]";
        }

        if (node.hasMarking(NodeMarking.CONTRADICTORY)) {
            return "[color=\"red\"]";
        }

        return "";
    }
}

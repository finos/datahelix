package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.utils.MutableInt;

import java.util.ArrayList;
import java.util.List;

public class DecisionTree {
    private final ConstraintNode rootNode;
    private final ProfileFields fields;

    public DecisionTree(ConstraintNode rootNode, ProfileFields fields) {
        this.rootNode = rootNode;
        this.fields = fields;
    }

    public ConstraintNode getRootNode() {
        return rootNode;
    }

    public ProfileFields getFields() {
        return fields;
    }

    //
    // https://en.wikipedia.org/wiki/DOT_(graph_description_language)

    /**
     * Generates a DOT formatted representation of the decision tree
     * See https://en.wikipedia.org/wiki/DOT_(graph_description_language)
     * @param graphName The name of the generated graph notation
     * @param description This string is added as a comment at the beginning of the exported file for added context
     * @return
     */
    public String toDot(String graphName, String description) {
        StringBuilder sb = new StringBuilder();

        sb.append("/*").append(description).append("*/\r\n");
        sb.append("graph " + graphName + " {\r\n");
        visit(getRootNode(), null, sb, new MutableInt(0));
        sb.append("}");

        return sb.toString();
    }

    private void visit(ConstraintNode constraintNode, String parentName, StringBuilder sb, MutableInt nodeIndex) {
        String nodeName = "c" + nodeIndex.get();

        if (parentName != null) {
            sb.append(parentName + " -- " + nodeName + "\r\n");
        }

        if(!constraintNode.getAtomicConstraints().isEmpty()) {

            List<String> labels = new ArrayList<>();
            for (IConstraint atomicConstraint : constraintNode.getAtomicConstraints()) {
                labels.add(atomicConstraint.toDotLabel());
            }

            sb.append(nodeName + " [label=\"" + String.join("\r\n", labels) + "\"][shape=box]" + "\r\n");
        }

        if(!constraintNode.getDecisions().isEmpty()) {
            for (DecisionNode decisionNode : constraintNode.getDecisions()) {
                nodeIndex.set(nodeIndex.get() + 1);
                visit(decisionNode, nodeName, sb, nodeIndex);
            }
        }
    }

    private void visit(DecisionNode decisionNode, String parentName, StringBuilder sb, MutableInt nodeIndex) {
        String nodeName = "d" + nodeIndex.get();

        sb.append(nodeName + "[label=\"\"][shape=invtriangle]" + "\r\n");
        sb.append(parentName + " -- " + nodeName + "\r\n");

        for (ConstraintNode x : decisionNode.getOptions()) {
            nodeIndex.set(nodeIndex.get() + 1);
            visit(x, nodeName, sb, nodeIndex);
        }
    }
}

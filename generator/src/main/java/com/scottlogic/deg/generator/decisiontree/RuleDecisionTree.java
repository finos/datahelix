package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.utils.MutableInt;

import java.util.ArrayList;
import java.util.List;

public class RuleDecisionTree {
    private final String description;
    private final ConstraintNode root;

    RuleDecisionTree(String description, ConstraintNode root) {
        this.description = description;
        this.root = root;
    }

    public String getDescription() {
        return description;
    }

    public ConstraintNode getRootNode() {
        return root;
    }

    public String toDot(String graphName) {
        StringBuilder sb = new StringBuilder();

        sb.append("/*").append(description).append("*/\r\n");
        sb.append("graph " + graphName + " {\r\n");
        visit(root, null, sb, new MutableInt(0));
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
                labels.add(atomicConstraint.toString());
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

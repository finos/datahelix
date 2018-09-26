package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.IConstraint;

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

    public String toDot() {

        StringBuilder sb = new StringBuilder();

        sb.append(" graph graphname {\r\n");
        visit(root, null, sb, 0);
        sb.append("}");

        return sb.toString();

    }

    private void visit(ConstraintNode constraintNode, String parentName, StringBuilder sb, int nodeIndex) {

        String nodeName = "c" + nodeIndex++;

        // Append node definition
        ////sb.append(nodeName + " [label=\"Lorem\"]\r\n");

        // Create relationship
        if (parentName != null) {
            sb.append(parentName + " -- " + nodeName + "\r\n");
        }

        if(!constraintNode.getDecisions().isEmpty()) {
            for (DecisionNode decisionNode : constraintNode.getDecisions()) {
                visit(decisionNode, nodeName, sb, nodeIndex++);
            }
        }

        if(!constraintNode.getAtomicConstraints().isEmpty()) {
            String constraintsNodeName = "a" + nodeIndex;
            String label = "";
            for (IConstraint atomicConstraint : constraintNode.getAtomicConstraints()) {

                label = label + atomicConstraint.toString() + "\r\n";

            }

            sb.append(nodeName + " [label=\"" + label + "\"][shape=box]" + "\r\n");
            //sb.append(nodeName + " -- " + constraintsNodeName + "\r\n");
        }

    }

    private void visit(DecisionNode decisionNode, String parentName, StringBuilder sb, int nodeIndex) {

        String nodeName = "d" + nodeIndex++;

        sb.append(nodeName + "[label=\"\"][shape=invtriangle]" + "\r\n");

        sb.append(parentName + " -- " + nodeName+ "\r\n");

        // Append node definition
        ////sb.append(nodeName + " [label=\"Lorem\"]");

        for (ConstraintNode x : decisionNode.getOptions()) {
            visit(x, nodeName, sb, nodeIndex++);
        }

    }
}

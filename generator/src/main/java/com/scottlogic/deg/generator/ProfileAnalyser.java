package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileAnalyser implements IProfileAnalyser {
    @Override
    public IAnalysedProfile analyse(Profile profile) {
        ArrayList<AnalysedRule> analysedRules = new ArrayList<>();
        for (Rule rule : profile.rules) {
            analysedRules.add(analyseRule(rule));
        }
        return new AnalysedProfile(new ArrayList<>(profile.fields), analysedRules);
    }

    private AnalysedRule analyseRule(Rule rule) {
        IConstraintTreeNode root = getNodeForConstraintCollection(rule.constraints);
        root = flattenTree(root);
        return new AnalysedRule(rule.description, root);
    }

    private boolean isConstraintAtomic(IConstraint constraint) {
        if (constraint instanceof AndConstraint ||
                constraint instanceof OrConstraint ||
                constraint instanceof ConditionalConstraint) {
            return false;
        }
        return true;
    }

    private IConstraintTreeNode getNodeForConstraint(IConstraint constraint) {
        if (isConstraintAtomic(constraint)) {
            return new ConstraintTreeNode(constraint);
        }
        if (constraint instanceof AndConstraint) {
            return getNodeForAndConstraint((AndConstraint)constraint);
        }
        if (constraint instanceof OrConstraint) {
            return getNodeForOrConstraint((OrConstraint)constraint);
        }
        if (constraint instanceof ConditionalConstraint) {
            return getNodeForConditionalConstraint((ConditionalConstraint)constraint);
        }

        // never reached
        return null;
    }

    private IConstraintTreeNode getNodeForTwoConstraints(IConstraint constraintA, IConstraint constraintB) {
        return getNodeForConstraint(constraintA).merge(getNodeForConstraint(constraintB));
    }

    private IConstraintTreeNode getNodeForAndConstraint(AndConstraint constraint) {
        return getNodeForConstraintCollection(constraint.subConstraints);
    }

    private IConstraintTreeNode getNodeForConstraintCollection(Collection<IConstraint> constraintCollection) {
        Collection<IConstraintTreeNode> nodeTips = new ArrayList<>();
        ConstraintTreeNode node = new ConstraintTreeNode();
        nodeTips.add(node);
        for (IConstraint subConstraint : constraintCollection) {
            if (isConstraintAtomic(subConstraint)) {
                node.addAtomicConstraint(subConstraint);
            }
            else {
                IConstraintTreeNode newChild = getNodeForConstraint(subConstraint);
                addChildConstraintToAllNodes(newChild, nodeTips);
                if (newChild.getChildNodes().size() > 0) {
                    nodeTips = newChild.getChildNodes();
                }
                else {
                    nodeTips = new ArrayList<>();
                    nodeTips.add(newChild);
                }
            }
        }
        return node;
    }

    private void addChildConstraintToAllNodes(IConstraintTreeNode child, Collection<IConstraintTreeNode> nodes) {
        for (IConstraintTreeNode node : nodes) {
            node.addChild(child);
        }
    }

    private IConstraintTreeNode getNodeForOrConstraint(OrConstraint constraint) {
        ConstraintTreeNode node = new ConstraintTreeNode();
        for (IConstraint subConstraint : constraint.subConstraints) {
            node.addChild(getNodeForConstraint(subConstraint));
        }
        return node;
    }

    private IConstraintTreeNode getNodeForConditionalConstraint(ConditionalConstraint constraint) {
        IConstraintTreeNode nodeBranchWhen = getNodeForTwoConstraints(constraint.condition, constraint.whenConditionIsTrue);
        IConstraintTreeNode nodeBranchWhenNot;
        NotConstraint negatedCondition = new NotConstraint(constraint.condition);
        if (constraint.whenConditionIsFalse == null) {
            nodeBranchWhenNot = getNodeForConstraint(negatedCondition);
        }
        else {
            nodeBranchWhenNot = getNodeForTwoConstraints(negatedCondition, constraint.whenConditionIsFalse);
        }
        return new ConstraintTreeNode(nodeBranchWhen, nodeBranchWhenNot);
    }

    private IConstraintTreeNode flattenTree(IConstraintTreeNode root) {
        for (IConstraintTreeNode node : root.getChildNodes()) {
            flattenTree(node);
        }
        if (root.getChildNodes().size() == 1) {
            IConstraintTreeNode onlyChild = root.getChildNodes().get(0);
            root.getChildNodes().remove(0);
            root.merge(onlyChild);
        }
        return root;
    }
}

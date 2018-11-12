package com.scottlogic.deg.generator.decisiontree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public final class TreeDecisionNode implements DecisionNode {
    private final Collection<ConstraintNode> options;
    private final boolean optimised;

    TreeDecisionNode(Collection<ConstraintNode> options) {
        this.options = new ArrayList<>(options);
        this.optimised = false;
    }

    public TreeDecisionNode(boolean optimised) {
        this.optimised = optimised;
        this.options = new ArrayList<>();
    }

    public TreeDecisionNode(ConstraintNode... options) {
        this.options = new ArrayList<>(Arrays.asList(options));
        this.optimised = false;
    }

    public Collection<ConstraintNode> getOptions() {
        return new ArrayList<>(options);
    }

    public void addOption(ConstraintNode newConstraint) {
        options.add(newConstraint);
    }

    public boolean isOptimised(){
        return optimised;
    }

    public String toString(){
        return this.options.size() >= 5
            ? String.format("Options: %d", this.options.size())
            : String.format("Options [%d]: %s",
                this.options.size(),
                String.join(
                    " OR ",
                    this.options.stream().map(o -> o.toString()).collect(Collectors.toList())));
    }
}

package com.scottlogic.deg.generator.decisiontree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public final class TreeDecisionNode implements DecisionNode {
    private final Collection<ConstraintNode> options;

    TreeDecisionNode(Collection<ConstraintNode> options) {
        this.options = new ArrayList<>(options);
    }

    public TreeDecisionNode(ConstraintNode... options) {
        this.options = new ArrayList<>(Arrays.asList(options));
    }

    public Collection<ConstraintNode> getOptions() {
        return new ArrayList<>(options);
    }

    public void addOption(ConstraintNode newConstraint) {
        options.add(newConstraint);
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

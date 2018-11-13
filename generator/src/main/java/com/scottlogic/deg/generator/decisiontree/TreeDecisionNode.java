package com.scottlogic.deg.generator.decisiontree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TreeDecisionNode implements DecisionNode {
    private final Collection<ConstraintNode> options;

    public TreeDecisionNode(Collection<ConstraintNode> options) {
        this.options = new ArrayList<>(options);
    }

    public TreeDecisionNode(ConstraintNode... options) {
        this.options = new ArrayList<>(Arrays.asList(options));
    }

    public Collection<ConstraintNode> getOptions() {
        return new ArrayList<>(options);
    }

    public DecisionNode addOption(ConstraintNode newConstraint) {
        return new TreeDecisionNode(
            Stream
                .concat(
                    this.options.stream(),
                    Stream.of(newConstraint))
                .collect(Collectors.toList()));
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

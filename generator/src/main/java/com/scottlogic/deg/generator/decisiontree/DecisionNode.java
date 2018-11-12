package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public final class DecisionNode {
    private final Collection<ConstraintNode> options;
    private final boolean optimised;

    DecisionNode(Collection<ConstraintNode> options) {
        this.options = new ArrayList<>(options);
        this.optimised = false;
    }

    public DecisionNode(boolean optimised) {
        this.optimised = optimised;
        this.options = new ArrayList<>();
    }

    public DecisionNode(ConstraintNode... options) {
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

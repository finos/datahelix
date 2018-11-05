package com.scottlogic.deg.generator.decisiontree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public final class DecisionNode {
    private final Collection<ConstraintNode> options;

    public DecisionNode(Collection<ConstraintNode> options) {
        this.options = options;
    }

    public DecisionNode(ConstraintNode... options) {
        this.options = Arrays.asList(options);
    }

    public Collection<ConstraintNode> getOptions() {
        return new ArrayList<>(options);
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

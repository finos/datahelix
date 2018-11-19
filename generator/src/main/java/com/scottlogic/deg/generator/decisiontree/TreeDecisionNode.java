package com.scottlogic.deg.generator.decisiontree;

import java.util.*;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TreeDecisionNode implements DecisionNode {
    private final Collection<ConstraintNode> options;

    public TreeDecisionNode(Collection<ConstraintNode> options) {
        this.options = Collections.unmodifiableCollection(options);
    }

    public TreeDecisionNode(ConstraintNode... options) {
        this.options = Collections.unmodifiableCollection(Arrays.asList(options));
    }

    @Override
    public Collection<ConstraintNode> getOptions() {
        return new HashSet<>(options);
    }

    @Override
    public DecisionNode setOptions(Collection<ConstraintNode> options){
        return new TreeDecisionNode(options);
    }

    @Override
    public String toString(){
        return this.options.size() >= 5
            ? String.format("Options: %d", this.options.size())
            : String.format("Options [%d]: %s",
                this.options.size(),
                String.join(
                    " OR ",
                    this.options.stream().map(o -> o.toString()).collect(Collectors.toList())));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeDecisionNode that = (TreeDecisionNode) o;
        return Objects.equals(options, that.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(options);
    }
}

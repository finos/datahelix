package com.scottlogic.deg.generator.decisiontree;

import java.util.*;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TreeDecisionNode implements DecisionNode {
    private final Collection<ConstraintNode> options;
    private final Set<NodeMarking> nodeMarkings;

    public TreeDecisionNode(ConstraintNode... options) {
        this(Collections.unmodifiableCollection(Arrays.asList(options)));
    }

    public TreeDecisionNode(Collection<ConstraintNode> options) {
        this(options, Collections.emptySet());
    }

    public TreeDecisionNode(Collection<ConstraintNode> options, Set<NodeMarking> nodeMarkings) {
        this.options = Collections.unmodifiableCollection(options);
        this.nodeMarkings = Collections.unmodifiableSet(nodeMarkings);
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
    public boolean hasMarking(NodeMarking detail) {
        return this.nodeMarkings.contains(detail);
    }

    @Override
    public DecisionNode markNode(NodeMarking marking) {
        Set<NodeMarking> newMarkings = Stream.of(Collections.singleton(marking), this.nodeMarkings)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
        return new TreeDecisionNode(this.options, newMarkings);
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

    @Override
    public DecisionNode accept(NodeVisitor visitor){
        Stream<ConstraintNode> options = getOptions().stream().map(c->c.accept(visitor));
        return visitor.visit(
            new TreeDecisionNode(
                options.collect(Collectors.toSet()),
                nodeMarkings
            )
        );
    }
}

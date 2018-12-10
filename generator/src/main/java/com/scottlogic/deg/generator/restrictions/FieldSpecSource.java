package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.ConstraintRule;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldSpecSource {
    final static FieldSpecSource Empty = new FieldSpecSource(Collections.emptySet(), Collections.emptySet());

    private final Set<AtomicConstraint> constraints;
    private final Set<ConstraintRule> rules;

    private FieldSpecSource(Set<AtomicConstraint> constraints, Set<ConstraintRule> rules) {
        this.constraints = constraints;
        this.rules = rules;
    }

    static FieldSpecSource fromConstraint(AtomicConstraint constraint, boolean negate, ConstraintRule rule) {
        return new FieldSpecSource(
            Collections.singleton(
                negate
                    ? constraint.negate()
                    : constraint),
            Collections.singleton(rule));
    }

    static FieldSpecSource fromFieldSpecs(FieldSpec left, FieldSpec right) {
        if (left == null){
            return right == null ? FieldSpecSource.Empty : right.getFieldSpecSource();
        }

        if (right == null){
            return left.getFieldSpecSource();
        }

        return left.getFieldSpecSource().combine(right.getFieldSpecSource());
    }

    public Set<AtomicConstraint> getConstraints() {
        return this.constraints;
    }

    public Set<ConstraintRule> getRules() {
        return rules;
    }

    FieldSpecSource combine(FieldSpecSource source) {
        if (this == FieldSpecSource.Empty){
            return source;
        }

        if (source == FieldSpecSource.Empty){
            return this;
        }

        return new FieldSpecSource(
            concat(this.constraints, source.constraints),
            concat(this.rules, source.rules)
        );
    }

    private static <T> Set<T> concat(Collection<T> left, Collection<T> right){
        return Stream.concat(
            left.stream(),
            right.stream()
        ).collect(Collectors.toSet());
    }
}

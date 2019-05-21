package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.DataBagValueSource;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldSpecSource {
    public final static FieldSpecSource Empty = new FieldSpecSource(Collections.emptySet(), Collections.emptySet());

    private final Set<AtomicConstraint> constraints;
    private final Set<AtomicConstraint> violatedConstraints;

    private FieldSpecSource(Set<AtomicConstraint> constraints, Set<AtomicConstraint> violatedConstraints) {
        this.constraints = constraints;
        this.violatedConstraints = violatedConstraints;
    }

    static FieldSpecSource fromConstraint(AtomicConstraint constraint, boolean negate, boolean violated) {
        return new FieldSpecSource(
            Collections.singleton(
                negate
                    ? constraint.negate()
                    : constraint),
            violated
                ? Collections.singleton(constraint)
                : Collections.emptySet());
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

    public Set<AtomicConstraint> getViolatedConstraints() {
        return this.violatedConstraints;
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
            concat(this.violatedConstraints, source.violatedConstraints)
        );
    }

    public DataBagValueSource toDataBagValueSource(){
        return new DataBagValueSource(getConstraints(), getViolatedConstraints());
    }

    private static <T> Set<T> concat(Collection<T> left, Collection<T> right){
        return Stream.concat(
            left.stream(),
            right.stream()
        ).collect(Collectors.toSet());
    }
}

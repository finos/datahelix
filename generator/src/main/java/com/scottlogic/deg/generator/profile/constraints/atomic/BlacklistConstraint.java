package com.scottlogic.deg.generator.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;

import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

public class BlacklistConstraint implements AtomicConstraint {
    public final Field field;
    public final DistributedList<Object> legalValues;

    public BlacklistConstraint(Field field, DistributedList<Object> legalValues) {
        this.field = field;
        this.legalValues = legalValues;

        if (legalValues.isEmpty()) {
            throw new IllegalArgumentException("Cannot create an IsInSetConstraint for field '" +
                field.name + "' with an empty set.");
        }

        if (legalValues.list().contains(null)) {
            throw new IllegalArgumentException("Cannot create an IsInSetConstraint for field '" +
                field.name + "' with a set containing null.");
        }
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AtomicConstraint negate() {
        return new IsInSetConstraint(field, legalValues);
    }

    @Override
    public FieldSpec toFieldSpec() {
        return FieldSpecFactory.fromType(field.getType()).withBlacklist(new HashSet<>(legalValues.list()));
    }

    public String toString(){
        boolean overLimit = legalValues.list().size() > 3;
        return String.format("%s in [%s%s](%d values)",
            field.name,
            legalValues.stream().limit(3).map(Object::toString).collect(Collectors.joining(", ")),
            overLimit ? ", ..." : "",
            legalValues.list().size());
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o instanceof ViolatedAtomicConstraint) {
            return o.equals(this);
        }
        if (o == null || getClass() != o.getClass()) return false;
        BlacklistConstraint constraint = (BlacklistConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(legalValues, constraint.legalValues);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, legalValues);
    }
}

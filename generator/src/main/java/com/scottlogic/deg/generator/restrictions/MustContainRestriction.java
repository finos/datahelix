package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.fieldspecs.FieldSpec;

import java.util.Objects;
import java.util.Set;

public class MustContainRestriction {
    private Set<FieldSpec> requiredObjects;

    public MustContainRestriction(Set<FieldSpec> requiredObjects) {
        this.requiredObjects = requiredObjects;
    }

    public Set<FieldSpec> getRequiredObjects() {
        return requiredObjects;
    }

    @Override
    public String toString() {
        return Objects.toString(this.requiredObjects);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MustContainRestriction that = (MustContainRestriction) o;
        return Objects.equals(requiredObjects, that.requiredObjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requiredObjects);
    }
}

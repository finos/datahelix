package com.scottlogic.deg.generator.restrictions;

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
}

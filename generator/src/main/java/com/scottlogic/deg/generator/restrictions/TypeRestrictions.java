package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

import java.lang.reflect.Type;
import java.util.*;

public class TypeRestrictions {

    public final static TypeRestrictions all = new TypeRestrictions(Arrays.asList(IsOfTypeConstraint.Types.values()));

    public TypeRestrictions(Collection<IsOfTypeConstraint.Types> allowedTypes) {
        if (allowedTypes.size() == 0)
            throw new UnsupportedOperationException("Cannot have a type restriction with no types");

        this.allowedTypes = new HashSet<>();
        this.allowedTypes.addAll(allowedTypes);
    }

    public static TypeRestrictions createFromWhiteList(IsOfTypeConstraint.Types... types) {
        return new TypeRestrictions(Arrays.asList(types));
    }

    public TypeRestrictions except(IsOfTypeConstraint.Types... types) {
        if (types.length == 0)
            return this;

        ArrayList<IsOfTypeConstraint.Types> allowedTypes = new ArrayList<>(this.allowedTypes);
        allowedTypes.removeAll(Arrays.asList(types));

        return new TypeRestrictions(allowedTypes);
    }

    private final Set<IsOfTypeConstraint.Types> allowedTypes;

    public boolean isTypeAllowed(IsOfTypeConstraint.Types type){
        return allowedTypes.contains(type);
    }

    public String toString() {
        if (this == all)
            return "Types: <all>";

        return String.format(
                "Types: %s",
                Objects.toString(allowedTypes));
    }

    public TypeRestrictions intersect(TypeRestrictions other) {
        if (this == all)
            return other;

        if (other == all)
            return this;

        ArrayList<IsOfTypeConstraint.Types> allowedTypes = new ArrayList<>(this.allowedTypes);
        allowedTypes.retainAll(other.allowedTypes);

        if (allowedTypes.isEmpty())
            return null;

        //micro-optimisation; if there is only one value in allowedTypes then there must have been only one value in either this.allowedTypes or other.allowedTypes
        if (allowedTypes.size() == 1) {
            return other.allowedTypes.size() == 1
                    ? other
                    : this;
        }

        return new TypeRestrictions(allowedTypes);
    }

    public Set<IsOfTypeConstraint.Types> getAllowedTypes() {
        return allowedTypes;
    }
}

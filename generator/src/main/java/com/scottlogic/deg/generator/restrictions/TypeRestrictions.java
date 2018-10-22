package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

import java.util.*;

public class TypeRestrictions implements ITypeRestrictions {

    public final static ITypeRestrictions all = new NoTypeRestriction();

    public TypeRestrictions(Collection<IsOfTypeConstraint.Types> allowedTypes) {
        if (allowedTypes.size() == 0)
            throw new UnsupportedOperationException("Cannot have a type restriction with no types");

        this.allowedTypes = new HashSet<>(allowedTypes);
    }

    public static ITypeRestrictions createFromWhiteList(IsOfTypeConstraint.Types... types) {
        return new TypeRestrictions(Arrays.asList(types));
    }

    public ITypeRestrictions except(IsOfTypeConstraint.Types... types) {
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
        if (allowedTypes.size() == 1)
            return String.format("Type = %s", allowedTypes.toArray()[0]);

        return String.format(
                "Types: %s",
                Objects.toString(allowedTypes));
    }

    public ITypeRestrictions intersect(ITypeRestrictions other) {
        if (other == all)
            return this;

        ArrayList<IsOfTypeConstraint.Types> allowedTypes = new ArrayList<>(this.allowedTypes);
        allowedTypes.retainAll(other.getAllowedTypes());

        if (allowedTypes.isEmpty())
            return null;

        //micro-optimisation; if there is only one value in allowedTypes then there must have been only one value in either this.allowedTypes or other.allowedTypes
        if (allowedTypes.size() == 1) {
            return other.getAllowedTypes().size() == 1
                    ? other
                    : this;
        }

        return new TypeRestrictions(allowedTypes);
    }

    public Set<IsOfTypeConstraint.Types> getAllowedTypes() {
        return allowedTypes;
    }
}


package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TypeRestrictions {

    public static TypeRestrictions createFromWhiteList(IsOfTypeConstraint.Types... types) {
        TypeRestrictions newRestrictions = new TypeRestrictions();

        newRestrictions.allowedTypes = new HashSet<>();
        newRestrictions.allowedTypes.addAll(Arrays.asList(types));

        return newRestrictions;
    }

    public static TypeRestrictions createFromBlackList(IsOfTypeConstraint.Types... types) {
        TypeRestrictions newRestrictions = new TypeRestrictions();

        newRestrictions.allowedTypes = new HashSet<>();
        newRestrictions.allowedTypes.addAll(Arrays.asList(IsOfTypeConstraint.Types.values()));
        newRestrictions.allowedTypes.removeAll(Arrays.asList(types));

        return newRestrictions;
    }

    public static TypeRestrictions createAllowAll() {
        TypeRestrictions newRestrictions = new TypeRestrictions();

        newRestrictions.allowedTypes = new HashSet<>();
        newRestrictions.allowedTypes.addAll(Arrays.asList(IsOfTypeConstraint.Types.values()));

        return newRestrictions;
    }


    public Set<IsOfTypeConstraint.Types> allowedTypes;

    public boolean isTypeAllowed(IsOfTypeConstraint.Types type){
        return allowedTypes.contains(type);
    }

    public String toString() {
        return String.format(
                "Types: %s",
                Objects.toString(allowedTypes));
    }
}

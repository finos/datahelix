package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NoTypeRestriction implements ITypeRestrictions {
    private final static Set<IsOfTypeConstraint.Types> allTypes = new HashSet<>(Arrays.asList(IsOfTypeConstraint.Types.values()));

    public boolean isTypeAllowed(IsOfTypeConstraint.Types type) {
        return true;
    }

    public String toString() {
        return "Types: <all>";
    }

    public ITypeRestrictions intersect(ITypeRestrictions other) {
        return other;
    }


    public ITypeRestrictions except(IsOfTypeConstraint.Types... types) {
        if (types.length == 0)
            return this;

        ArrayList<IsOfTypeConstraint.Types> allowedTypes = new ArrayList<>(allTypes);
        allowedTypes.removeAll(Arrays.asList(types));

        return new TypeRestrictions(allowedTypes);
    }

    public Set<IsOfTypeConstraint.Types> getAllowedTypes() {
        return allTypes;
    }
}

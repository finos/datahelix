package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.constraint.atomic.IsOfTypeConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AnyTypeRestriction implements TypeRestrictions {
    private final static Set<IsOfTypeConstraint.Types> allTypes = new HashSet<>(Arrays.asList(IsOfTypeConstraint.Types.values()));

    public boolean isTypeAllowed(IsOfTypeConstraint.Types type) {
        return true;
    }

    public String toString() {
        return "Any type";
    }

    public TypeRestrictions intersect(TypeRestrictions other) {
        return other;
    }


    public TypeRestrictions except(IsOfTypeConstraint.Types... types) {
        if (types.length == 0)
            return this;

        ArrayList<IsOfTypeConstraint.Types> allowedTypes = new ArrayList<>(allTypes);
        allowedTypes.removeAll(Arrays.asList(types));

        return new DataTypeRestrictions(allowedTypes);
    }

    public Set<IsOfTypeConstraint.Types> getAllowedTypes() {
        return allTypes;
    }

    public int hashCode(){
        return this.getClass().hashCode();
    }

    public boolean equals(Object obj){
        return obj instanceof AnyTypeRestriction;
    }
}

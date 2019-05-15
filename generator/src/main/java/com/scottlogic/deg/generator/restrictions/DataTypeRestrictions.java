package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;

import java.util.*;

public class DataTypeRestrictions implements TypeRestrictions {

    public final static TypeRestrictions ALL_TYPES_PERMITTED = new AnyTypeRestriction();
    public final static TypeRestrictions NO_TYPES_PERMITTED = new NoAllowedTypesRestriction();

    public DataTypeRestrictions(Collection<IsOfTypeConstraint.Types> allowedTypes) {
        if (allowedTypes.size() == 0)
            throw new UnsupportedOperationException("Cannot have a type restriction with no types");

        this.allowedTypes = new HashSet<>(allowedTypes);
    }

    public static TypeRestrictions createFromWhiteList(IsOfTypeConstraint.Types... types) {
        return new DataTypeRestrictions(Arrays.asList(types));
    }

    public TypeRestrictions except(IsOfTypeConstraint.Types... types) {
        if (types.length == 0)
            return this;

        ArrayList<IsOfTypeConstraint.Types> allowedTypes = new ArrayList<>(this.allowedTypes);
        allowedTypes.removeAll(Arrays.asList(types));

        if (allowedTypes.isEmpty()){
            return NO_TYPES_PERMITTED;
        }

        return new DataTypeRestrictions(allowedTypes);
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

    public TypeRestrictions intersect(TypeRestrictions other) {
        if (other == ALL_TYPES_PERMITTED)
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

        return new DataTypeRestrictions(allowedTypes);
    }

    public Set<IsOfTypeConstraint.Types> getAllowedTypes() {
        return allowedTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataTypeRestrictions that = (DataTypeRestrictions) o;
        return Objects.equals(allowedTypes, that.allowedTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowedTypes);
    }
}


package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types;

import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.common.util.HeterogeneousTypeContainer;
import com.scottlogic.deg.generator.restrictions.set.SetRestrictions;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Details a column's atomic constraints
 */
public class FieldSpec {
    public static final FieldSpec Empty = new FieldSpec(new HeterogeneousTypeContainer<>());

    private final HeterogeneousTypeContainer<Restrictions> restrictions;

    private FieldSpec(HeterogeneousTypeContainer<Restrictions> restrictions) {
        this.restrictions = restrictions;
    }

    public SetRestrictions getSetRestrictions() {
        return restrictions.get(SetRestrictions.class).orElse(null);
    }

    public NumericRestrictions getNumericRestrictions() {
        return restrictions.get(NumericRestrictions.class).orElse(null);
    }

    public StringRestrictions getStringRestrictions() {
        return restrictions.get(StringRestrictions.class).orElse(null);
    }

    public NullRestrictions getNullRestrictions() {
        return restrictions.get(NullRestrictions.class).orElse(null);
    }

    public TypeRestrictions getTypeRestrictions() {
        return restrictions.get(TypeRestrictions.class).orElse(null);
    }

    public DateTimeRestrictions getDateTimeRestrictions() {
        return restrictions.get(DateTimeRestrictions.class).orElse(null);
    }

    public FormatRestrictions getFormatRestrictions() {
        return restrictions.get(FormatRestrictions.class).orElse(null);
    }

    public FieldSpec withSetRestrictions(SetRestrictions setRestrictions) {
        return withConstraint(SetRestrictions.class, setRestrictions);
    }

    public FieldSpec withNumericRestrictions(NumericRestrictions numericRestrictions) {
        return withConstraint(NumericRestrictions.class, numericRestrictions);
    }

    public FieldSpec withStringRestrictions(StringRestrictions stringRestrictions) {
        return withConstraint(StringRestrictions.class, stringRestrictions);
    }

    public FieldSpec withTypeRestrictions(TypeRestrictions typeRestrictions) {
        return withConstraint(TypeRestrictions.class, typeRestrictions);
    }

    public FieldSpec withNullRestrictions(NullRestrictions nullRestrictions) {
        return withConstraint(NullRestrictions.class, nullRestrictions);
    }

    public FieldSpec withDateTimeRestrictions(DateTimeRestrictions dateTimeRestrictions) {
        return withConstraint(DateTimeRestrictions.class, dateTimeRestrictions);
    }

    public FieldSpec withFormatRestrictions(FormatRestrictions formatRestrictions) {
        return withConstraint(FormatRestrictions.class, formatRestrictions);
    }

    private <T extends Restrictions> FieldSpec withConstraint(Class<T> type, T restriction) {
        return new FieldSpec(restrictions.put(type, restriction));
    }

    @Override
    public String toString() {
        List<String> propertyStrings = restrictions.values()
            .stream()
            .filter(Objects::nonNull)
            .map(Object::toString)
            .collect(Collectors.toList());

        if (propertyStrings.isEmpty()) {
            return "<empty>";
        }

        return String.join(" & ", propertyStrings);
    }

    /**
     * Create a predicate that returns TRUE for all (and only) values permitted by this FieldSpec
     */
    public boolean permits(Object value) {
        TypeRestrictions typeRestrictions = getTypeRestrictions();
        if (typeRestrictions != null) {
            for (Types type : Types.values()) {
                if (!typeRestrictions.isTypeAllowed(type) && type.isInstanceOf(value)) {
                    return false;
                }
            }
        }

        Set<Class<? extends Restrictions>> keys = new HashSet<>();
        keys.add(NumericRestrictions.class);
        keys.add(DateTimeRestrictions.class);
        keys.add(StringRestrictions.class);

        Set<TypedRestrictions> toCheckForMatch = restrictions.getMultiple(keys)
            .stream()
            .map(r -> (TypedRestrictions) r)
            .collect(Collectors.toSet());
        for (TypedRestrictions restriction : toCheckForMatch) {
            if (restriction != null && restriction.isInstanceOf(value) && !restriction.match(value)) {
                return false;
            }
        }

        return true;
    }

    public int hashCode() {
        return restrictions.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        FieldSpec other = (FieldSpec) obj;
        return restrictions.equals(other.restrictions);
    }
}

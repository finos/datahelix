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
    public static final FieldSpec Empty = new FieldSpec(
        new HeterogeneousTypeContainer<>(),
        FieldSpecSource.Empty);

    private final HeterogeneousTypeContainer<Restrictions> restrictions;
    private final FieldSpecSource source;

    private FieldSpec(HeterogeneousTypeContainer<Restrictions> restrictions,
                      FieldSpecSource source) {
        this.restrictions = restrictions;
        this.source = source;
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

    public FieldSpecSource getFieldSpecSource() {
        return source;
    }

    public FieldSpec withSetRestrictions(SetRestrictions setRestrictions, FieldSpecSource source) {
        return withConstraint(SetRestrictions.class, setRestrictions, source);
    }

    public FieldSpec withNumericRestrictions(NumericRestrictions numericRestrictions, FieldSpecSource source) {
        return withConstraint(NumericRestrictions.class, numericRestrictions, source);
    }

    public FieldSpec withStringRestrictions(StringRestrictions stringRestrictions, FieldSpecSource source) {
        return withConstraint(StringRestrictions.class, stringRestrictions, source);
    }

    public FieldSpec withTypeRestrictions(TypeRestrictions typeRestrictions, FieldSpecSource source) {
        return withConstraint(TypeRestrictions.class, typeRestrictions, source);
    }

    public FieldSpec withNullRestrictions(NullRestrictions nullRestrictions, FieldSpecSource source) {
        return withConstraint(NullRestrictions.class, nullRestrictions, source);
    }

    public FieldSpec withDateTimeRestrictions(DateTimeRestrictions dateTimeRestrictions, FieldSpecSource source) {
        return withConstraint(DateTimeRestrictions.class, dateTimeRestrictions, source);
    }

    public FieldSpec withFormatRestrictions(FormatRestrictions formatRestrictions, FieldSpecSource source) {
        return withConstraint(FormatRestrictions.class, formatRestrictions, source);
    }

    private <T extends Restrictions> FieldSpec withConstraint(Class<T> type, T restriction, FieldSpecSource source) {
        return new FieldSpec(restrictions.put(type, restriction), this.source.combine(source));
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

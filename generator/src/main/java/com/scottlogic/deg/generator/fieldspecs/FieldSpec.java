package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types;

import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.common.util.HeterogeneousTypeContainer;
import com.scottlogic.deg.generator.restrictions.SetRestrictions;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Details a column's atomic constraints
 */
public class FieldSpec {
    public static final FieldSpec Empty =
        new FieldSpec(new HeterogeneousTypeContainer<>(), true, null);

    private final boolean nullable;
    private final String formatting;

    private final HeterogeneousTypeContainer<Restrictions> restrictions;

    private FieldSpec(
        HeterogeneousTypeContainer<Restrictions> restrictions,
        boolean nullable,
        String formatting
    ) {
        this.restrictions = restrictions;
        this.nullable = nullable;
        this.formatting = formatting;
    }

    public boolean isNullable() {
        return nullable;
    }

    public SetRestrictions getSetRestrictions() {
        return restrictions.get(SetRestrictions.class).orElse(null);
    }

    public BlacklistRestrictions getBlacklistRestrictions() {
        return restrictions.get(BlacklistRestrictions.class).orElse(null);
    }

    public NumericRestrictions getNumericRestrictions() {
        return restrictions.get(NumericRestrictions.class).orElse(null);
    }

    public StringRestrictions getStringRestrictions() {
        return restrictions.get(StringRestrictions.class).orElse(null);
    }

    public TypeRestrictions getTypeRestrictions() {
        return restrictions.get(TypeRestrictions.class).orElse(null);
    }

    public DateTimeRestrictions getDateTimeRestrictions() {
        return restrictions.get(DateTimeRestrictions.class).orElse(null);
    }

    public String getFormatting() {
        return formatting;
    }

    public FieldSpec withSetRestrictions(SetRestrictions setRestrictions) {
        return withConstraint(SetRestrictions.class, setRestrictions);
    }

    public FieldSpec withNumericRestrictions(NumericRestrictions numericRestrictions) {
        return withConstraint(NumericRestrictions.class, numericRestrictions);
    }

    public FieldSpec withBlacklistRestrictions(BlacklistRestrictions blacklistRestrictions) {
        return withConstraint(BlacklistRestrictions.class, blacklistRestrictions);
    }

    public FieldSpec withTypeRestrictions(TypeRestrictions typeRestrictions) {
        return withConstraint(TypeRestrictions.class, typeRestrictions);
    }

    public FieldSpec withStringRestrictions(StringRestrictions stringRestrictions) {
        return withConstraint(StringRestrictions.class, stringRestrictions);
    }

    public FieldSpec withNotNull() {
        return new FieldSpec(restrictions, false, formatting);
    }

    public static FieldSpec mustBeNull() {
        return FieldSpec.Empty.withSetRestrictions(
            SetRestrictions.fromWhitelist(Collections.emptySet())
        );
    }

    public FieldSpec withDateTimeRestrictions(DateTimeRestrictions dateTimeRestrictions) {
        return withConstraint(DateTimeRestrictions.class, dateTimeRestrictions);
    }

    public FieldSpec withFormatting(String formatting) {
        return new FieldSpec(restrictions, nullable, formatting);
    }

    private <T extends Restrictions> FieldSpec withConstraint(Class<T> type, T restriction) {
        return new FieldSpec(restrictions.put(type, restriction), nullable, formatting);
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
        keys.add(BlacklistRestrictions.class);

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
        return Objects.hash(nullable, restrictions, formatting);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        FieldSpec other = (FieldSpec) obj;
        return Objects.equals(nullable, other.nullable)
            && Objects.equals(restrictions, other.restrictions)
            && Objects.equals(formatting, other.formatting);
    }
}

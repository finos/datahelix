package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types;

import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.utils.SetUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Details a column's atomic constraints
 */
public class FieldSpec {
    public static final FieldSpec Empty = new FieldSpec(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        FieldSpecSource.Empty);

    private enum RestrictionMapping {
        TYPE,
        SET,
        NUMERIC,
        STRING,
        NULL,
        DATE_TIME,
        FORMAT,
        MUST_CONTAIN
    }

    private final Map<RestrictionMapping, Restrictions> restrictions;
    @NotNull
    private final FieldSpecSource source;

    private FieldSpec(Map<RestrictionMapping, Restrictions> restrictions,
                      FieldSpecSource source) {
        EnumMap<RestrictionMapping, Restrictions> mappings = new EnumMap<>(RestrictionMapping.class);
        mappings.putAll(restrictions);
        this.restrictions = Collections.unmodifiableMap(mappings);
        this.source = source;
    }

    private FieldSpec(
        SetRestrictions setRestrictions,
        NumericRestrictions numericRestrictions,
        StringRestrictions stringRestrictions,
        NullRestrictions nullRestrictions,
        TypeRestrictions typeRestrictions,
        DateTimeRestrictions dateTimeRestrictions,
        FormatRestrictions formatRestrictions,
        MustContainRestriction mustContainRestriction,
        @NotNull FieldSpecSource source) {
        EnumMap<RestrictionMapping, Restrictions> mappings = new EnumMap<>(RestrictionMapping.class);

        mappings.put(RestrictionMapping.SET, setRestrictions);
        mappings.put(RestrictionMapping.NULL, nullRestrictions);
        mappings.put(RestrictionMapping.MUST_CONTAIN, mustContainRestriction);
        mappings.put(RestrictionMapping.FORMAT, formatRestrictions);

        if (!(setRestrictions != null && setRestrictions.getWhitelist() != null && !setRestrictions.getWhitelist().isEmpty())) {
            mappings.put(RestrictionMapping.NUMERIC, nullRestrictions);
            mappings.put(RestrictionMapping.STRING, stringRestrictions);
            mappings.put(RestrictionMapping.TYPE, typeRestrictions);
            mappings.put(RestrictionMapping.DATE_TIME, dateTimeRestrictions);
        }

        this.source = source;
        this.restrictions = Collections.unmodifiableMap(mappings);
    }

    public SetRestrictions getSetRestrictions() {
        return (SetRestrictions) restrictions.get(RestrictionMapping.SET);
    }

    public NumericRestrictions getNumericRestrictions() {
        return (NumericRestrictions) restrictions.get(RestrictionMapping.NUMERIC);
    }

    public StringRestrictions getStringRestrictions() {
        return (StringRestrictions) restrictions.get(RestrictionMapping.STRING);
    }

    public NullRestrictions getNullRestrictions() {
        return (NullRestrictions) restrictions.get(RestrictionMapping.NULL);
    }

    public TypeRestrictions getTypeRestrictions() {
        return (TypeRestrictions) restrictions.get(RestrictionMapping.TYPE);
    }

    public DateTimeRestrictions getDateTimeRestrictions() {
        return (DateTimeRestrictions) restrictions.get(RestrictionMapping.DATE_TIME);
    }

    public MustContainRestriction getMustContainRestriction() {
        return (MustContainRestriction) restrictions.get(RestrictionMapping.MUST_CONTAIN);
    }

    public FormatRestrictions getFormatRestrictions() {
        return (FormatRestrictions) restrictions.get(RestrictionMapping.FORMAT);
    }

    @NotNull
    public FieldSpecSource getFieldSpecSource() {
        return this.source;
    }

    public FieldSpec withSetRestrictions(SetRestrictions setRestrictions, FieldSpecSource source) {
        EnumMap<RestrictionMapping, Restrictions> mappings = new EnumMap<>(restrictions);
        mappings.put(RestrictionMapping.SET, setRestrictions);
        return new FieldSpec(
            Collections.unmodifiableMap(mappings),
            this.source.combine(source));
    }

    public FieldSpec withNumericRestrictions(NumericRestrictions numericRestrictions, FieldSpecSource source) {
        EnumMap<RestrictionMapping, Restrictions> mappings = new EnumMap<>(restrictions);
        mappings.put(RestrictionMapping.NUMERIC, numericRestrictions);
        return new FieldSpec(
            Collections.unmodifiableMap(mappings),
            this.source.combine(source));
    }

    public FieldSpec withStringRestrictions(StringRestrictions stringRestrictions, FieldSpecSource source) {
        EnumMap<RestrictionMapping, Restrictions> mappings = new EnumMap<>(restrictions);
        mappings.put(RestrictionMapping.STRING, stringRestrictions);
        return new FieldSpec(
            Collections.unmodifiableMap(mappings),
            this.source.combine(source));
    }

    public FieldSpec withTypeRestrictions(TypeRestrictions typeRestrictions, FieldSpecSource source) {
        EnumMap<RestrictionMapping, Restrictions> mappings = new EnumMap<>(restrictions);
        mappings.put(RestrictionMapping.TYPE, typeRestrictions);
        return new FieldSpec(
            Collections.unmodifiableMap(mappings),
            this.source.combine(source));
    }

    public FieldSpec withNullRestrictions(NullRestrictions nullRestrictions, FieldSpecSource source) {
        EnumMap<RestrictionMapping, Restrictions> mappings = new EnumMap<>(restrictions);
        mappings.put(RestrictionMapping.NULL, nullRestrictions);
        return new FieldSpec(
            Collections.unmodifiableMap(mappings),
            this.source.combine(source));
    }

    public FieldSpec withDateTimeRestrictions(DateTimeRestrictions dateTimeRestrictions, FieldSpecSource source) {
        EnumMap<RestrictionMapping, Restrictions> mappings = new EnumMap<>(restrictions);
        mappings.put(RestrictionMapping.DATE_TIME, dateTimeRestrictions);
        return new FieldSpec(
            Collections.unmodifiableMap(mappings),
            this.source.combine(source));
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

    public FieldSpec withFormatRestrictions(FormatRestrictions formatRestrictions, FieldSpecSource source) {
        EnumMap<RestrictionMapping, Restrictions> mappings = new EnumMap<>(restrictions);
        mappings.put(RestrictionMapping.FORMAT, formatRestrictions);
        return new FieldSpec(
            Collections.unmodifiableMap(mappings),
            this.source.combine(source));
    }

    public FieldSpec withMustContainRestriction(MustContainRestriction mustContainRestriction) {
        EnumMap<RestrictionMapping, Restrictions> mappings = new EnumMap<>(restrictions);
        mappings.put(RestrictionMapping.MUST_CONTAIN, mustContainRestriction);
        return new FieldSpec(
            Collections.unmodifiableMap(mappings),
            this.source);
    }

    /**
     * Create a predicate that returns TRUE for all (and only) values permitted by this FieldSpec
     */
    public boolean permits(@NotNull Object value) {
        TypeRestrictions typeRestrictions = getTypeRestrictions();
        if (typeRestrictions != null) {
            for (Types type : Types.values()) {
                if (!typeRestrictions.isTypeAllowed(type) && type.getIsInstanceOf().apply(value)) {
                    return false;
                }
            }
        }

        Set<RestrictionMapping> keys = SetUtils.setOf(RestrictionMapping.NUMERIC,
            RestrictionMapping.DATE_TIME,
            RestrictionMapping.STRING);
        Set<TypedRestrictions> toCheckForMatch = getMultipleFromMap(restrictions, keys)
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

    private <K, T> Set<T> getMultipleFromMap(Map<K, T> map, Set<K> keys) {
        return keys.stream().map(map::get).collect(Collectors.toSet());
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

        return equals((FieldSpec) obj);
    }

    private boolean equals(FieldSpec other) {
        return restrictions.equals(other.restrictions);
    }

    private static boolean propertiesAreEqual(Object myProperty, Object otherProperty) {
        if (myProperty == null && otherProperty == null) {
            return true;
        }

        if (myProperty == null || otherProperty == null) {
            return false; //one of the properties are null, but the other one cannot be (the first IF guards against this)
        }

        if (!myProperty.getClass().equals(otherProperty.getClass())) {
            return false;
        }

        return myProperty.equals(otherProperty);
    }
}

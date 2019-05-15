package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.scottlogic.deg.generator.restrictions.DateTimeRestrictions.isDateTime;
import static com.scottlogic.deg.generator.restrictions.NumericRestrictions.isNumeric;
import static com.scottlogic.deg.generator.restrictions.StringRestrictions.isString;

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

    private final SetRestrictions setRestrictions;
    private final NumericRestrictions numericRestrictions;
    private final StringRestrictions stringRestrictions;
    private final NullRestrictions nullRestrictions;
    private final TypeRestrictions typeRestrictions;
    private final DateTimeRestrictions dateTimeRestrictions;
    private final FormatRestrictions formatRestrictions;
    private final MustContainRestriction mustContainRestriction;
    @NotNull private final FieldSpecSource source;

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

        this.setRestrictions = setRestrictions;
        this.nullRestrictions = nullRestrictions;
        this.mustContainRestriction = mustContainRestriction;
        this.formatRestrictions = formatRestrictions;

        if (setRestrictions != null && setRestrictions.getWhitelist() != null && setRestrictions.getWhitelist().size() > 0) {
            this.numericRestrictions = null;
            this.stringRestrictions = null;
            this.typeRestrictions = null;
            this.dateTimeRestrictions = null;
        } else {
            this.numericRestrictions = numericRestrictions;
            this.stringRestrictions = stringRestrictions;
            this.typeRestrictions = typeRestrictions;
            this.dateTimeRestrictions = dateTimeRestrictions;
        }

        this.source = source;
    }

    public SetRestrictions getSetRestrictions() { return setRestrictions; }
    public NumericRestrictions getNumericRestrictions() { return numericRestrictions; }
    public StringRestrictions getStringRestrictions() { return stringRestrictions; }
    public NullRestrictions getNullRestrictions() { return nullRestrictions; }
    public TypeRestrictions getTypeRestrictions() { return typeRestrictions; }
    public DateTimeRestrictions getDateTimeRestrictions() { return dateTimeRestrictions; }
    public MustContainRestriction getMustContainRestriction() { return mustContainRestriction; }
    public FormatRestrictions getFormatRestrictions() { return formatRestrictions; }
    @NotNull public FieldSpecSource getFieldSpecSource() { return this.source; }

    public FieldSpec withSetRestrictions(SetRestrictions setRestrictions, FieldSpecSource source) {
        return new FieldSpec(
            setRestrictions,
            this.numericRestrictions,
            this.stringRestrictions,
            this.nullRestrictions,
            this.typeRestrictions,
            this.dateTimeRestrictions,
            this.formatRestrictions,
            this.mustContainRestriction,
            this.source.combine(source));
    }

    public FieldSpec withNumericRestrictions(NumericRestrictions numericRestrictions, FieldSpecSource source) {
        return new FieldSpec(
            this.setRestrictions,
            numericRestrictions,
            this.stringRestrictions,
            this.nullRestrictions,
            this.typeRestrictions,
            this.dateTimeRestrictions,
            this.formatRestrictions,
            this.mustContainRestriction,
            this.source.combine(source));
    }

    public FieldSpec withStringRestrictions(StringRestrictions stringRestrictions, FieldSpecSource source) {
        return new FieldSpec(
            this.setRestrictions,
            this.numericRestrictions,
            stringRestrictions,
            this.nullRestrictions,
            this.typeRestrictions,
            this.dateTimeRestrictions,
            this.formatRestrictions,
            this.mustContainRestriction,
            this.source.combine(source));
    }

    public FieldSpec withTypeRestrictions(TypeRestrictions typeRestrictions, FieldSpecSource source) {
        return new FieldSpec(
            this.setRestrictions,
            this.numericRestrictions,
            this.stringRestrictions,
            this.nullRestrictions,
            typeRestrictions,
            this.dateTimeRestrictions,
            this.formatRestrictions,
            this.mustContainRestriction,
            this.source.combine(source));
    }

    public FieldSpec withNullRestrictions(NullRestrictions nullRestrictions, FieldSpecSource source) {
        return new FieldSpec(
            this.setRestrictions,
            this.numericRestrictions,
            this.stringRestrictions,
            nullRestrictions,
            this.typeRestrictions,
            this.dateTimeRestrictions,
            this.formatRestrictions,
            this.mustContainRestriction,
            this.source.combine(source));
    }

    public FieldSpec withDateTimeRestrictions(DateTimeRestrictions dateTimeRestrictions, FieldSpecSource source) {
        return new FieldSpec(
            this.setRestrictions,
            this.numericRestrictions,
            this.stringRestrictions,
            this.nullRestrictions,
            this.typeRestrictions,
            dateTimeRestrictions,
            this.formatRestrictions,
            this.mustContainRestriction,
            this.source.combine(source));
    }

    @Override
    public String toString() {
        List<String> propertyStrings = Arrays
            .stream(getPropertiesToCompare(this))
            .filter(Objects::nonNull)
            .map(Object::toString)
            .collect(Collectors.toList());

        if (propertyStrings.isEmpty()) { return "<empty>"; }

        return String.join(" & ", propertyStrings);
    }

    public FieldSpec withFormatRestrictions(FormatRestrictions formatRestrictions, FieldSpecSource source) {
        return new FieldSpec(
            this.setRestrictions,
            this.numericRestrictions,
            this.stringRestrictions,
            this.nullRestrictions,
            this.typeRestrictions,
            this.dateTimeRestrictions,
            formatRestrictions,
            this.mustContainRestriction,
            this.source.combine(source));
    }

    public FieldSpec withMustContainRestriction(MustContainRestriction mustContainRestriction) {
        return new FieldSpec(
            this.setRestrictions,
            this.numericRestrictions,
            this.stringRestrictions,
            this.nullRestrictions,
            this.typeRestrictions,
            this.dateTimeRestrictions,
            this.formatRestrictions,
            mustContainRestriction,
            this.source);
    }

    /** Create a predicate that returns TRUE for all (and only) values permitted by this FieldSpec */
    public boolean permits(@NotNull Object value) {
        if (typeRestrictions != null) {
            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.NUMERIC)) {
                if (isNumeric(value)) return false;
            }

            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.STRING)) {
                if (isString(value)) return false;
            }

            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.DATETIME)) {
                if (isDateTime(value)) return false;
            }
        }

        if (numericRestrictions != null) {
            if (isNumeric(value) && !numericRestrictions.match(value)) return false;
        }

        if (dateTimeRestrictions != null) {
            if (isDateTime(value) && !dateTimeRestrictions.match(value)) return false;
        }

        if (stringRestrictions != null) {
            if (isString(value) && !stringRestrictions.match(value)) return false;
        }

        return true;
    }

    public int hashCode(){
        return Arrays.hashCode(getPropertiesToCompare(this));
    }

    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }

        if (!(obj instanceof FieldSpec)){
            return false;
        }

        return equals((FieldSpec)obj);
    }

    private boolean equals(FieldSpec other){
        Iterator<Object> myProperties = Arrays.asList(getPropertiesToCompare(this)).iterator();
        Iterator<Object> otherPropertiesToCompare = Arrays.asList(getPropertiesToCompare(other)).iterator();

        //effectively Stream.zip(myProperties, otherProperties).allMatch((x, y) -> propertiesAreEqual(x, y));
        while (myProperties.hasNext()){
            Object myProperty = myProperties.next();

            if (!otherPropertiesToCompare.hasNext()){
                return false;
            }

            Object otherProperty = otherPropertiesToCompare.next();

            if (!propertiesAreEqual(myProperty, otherProperty)){
                return false;
            }
        }

        return true;
    }

    private static boolean propertiesAreEqual(Object myProperty, Object otherProperty) {
        if (myProperty == null && otherProperty == null){
            return true;
        }

        if (myProperty == null || otherProperty == null){
            return false; //one of the properties are null, but the other one cannot be (the first IF guards against this)
        }

        if (!myProperty.getClass().equals(otherProperty.getClass())){
            return false;
        }

        return myProperty.equals(otherProperty);
    }

    private static Object[] getPropertiesToCompare(FieldSpec fieldSpec){
        return new Object[]{
            fieldSpec.dateTimeRestrictions,
            fieldSpec.formatRestrictions,
            fieldSpec.mustContainRestriction,
            fieldSpec.nullRestrictions,
            fieldSpec.numericRestrictions,
            fieldSpec.setRestrictions,
            fieldSpec.stringRestrictions,
            fieldSpec.typeRestrictions
        };
    }
}

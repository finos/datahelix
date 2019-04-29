package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
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

    @Nullable private final SetRestrictions setRestrictions;
    @Nullable private final NumericRestrictions numericRestrictions;
    @Nullable private final StringRestrictions stringRestrictions;
    @Nullable private final NullRestrictions nullRestrictions;
    @Nullable private final TypeRestrictions typeRestrictions;
    @Nullable private final DateTimeRestrictions dateTimeRestrictions;
    @Nullable private final FormatRestrictions formatRestrictions;
    @Nullable private final MustContainRestriction mustContainRestriction;
    @NotNull  private final FieldSpecSource source;

    private FieldSpec(
        @Nullable SetRestrictions setRestrictions,
        @Nullable NumericRestrictions numericRestrictions,
        @Nullable StringRestrictions stringRestrictions,
        @Nullable NullRestrictions nullRestrictions,
        @Nullable TypeRestrictions typeRestrictions,
        @Nullable DateTimeRestrictions dateTimeRestrictions,
        @Nullable FormatRestrictions formatRestrictions,
        @Nullable MustContainRestriction mustContainRestriction,
        @NotNull  FieldSpecSource source) {

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

    @Nullable public SetRestrictions getSetRestrictions() { return setRestrictions; }
    @Nullable public NumericRestrictions getNumericRestrictions() { return numericRestrictions; }
    @Nullable public StringRestrictions getStringRestrictions() { return stringRestrictions; }
    @Nullable public NullRestrictions getNullRestrictions() { return nullRestrictions; }
    @Nullable public TypeRestrictions getTypeRestrictions() { return typeRestrictions; }
    @Nullable public DateTimeRestrictions getDateTimeRestrictions() { return dateTimeRestrictions; }
    @Nullable public MustContainRestriction getMustContainRestriction() { return mustContainRestriction; }
    @Nullable public FormatRestrictions getFormatRestrictions() { return formatRestrictions; }
    @NotNull  public FieldSpecSource getFieldSpecSource() { return this.source; }

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
    Predicate<Object> getValueValidityPredicate() {
        List<Predicate<Object>> predicates = new ArrayList<>();

        if (typeRestrictions != null) {
            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.NUMERIC)) {
                predicates.add(x -> !isNumeric(x));
            }

            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.STRING)) {
                predicates.add(x -> !isString(x));
            }

            if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.DATETIME)) {
                predicates.add(x -> !isDateTime(x));
            }
        }

        if (stringRestrictions != null) {
            predicates.add(x -> !isString(x) || stringRestrictions.match(x));
        }

        if (numericRestrictions != null) {
            predicates.add(x -> !isNumeric(x) || numericRestrictions.match(x));
        }

        if (dateTimeRestrictions != null) {
            predicates.add(x -> !isDateTime(x) || dateTimeRestrictions.match(x));
        }

        return x -> predicates.stream().allMatch(p -> p.test(x));
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

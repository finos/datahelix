package com.scottlogic.deg.generator.restrictions;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Details a column's atomic constraints
 */
public class FieldSpec {
    public static final FieldSpec Empty = new FieldSpec(null,
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
    private final GranularityRestrictions granularityRestrictions;
    private final MustContainRestriction mustContainRestriction;
    private final FieldSpecSource source;

    public FieldSpec(
        SetRestrictions setRestrictions,
        NumericRestrictions numericRestrictions,
        StringRestrictions stringRestrictions,
        NullRestrictions nullRestrictions,
        TypeRestrictions typeRestrictions,
        DateTimeRestrictions dateTimeRestrictions,
        FormatRestrictions formatRestrictions,
        GranularityRestrictions granularityRestrictions,
        MustContainRestriction mustContainRestriction,
        FieldSpecSource source) {
        this.setRestrictions = setRestrictions;
        this.numericRestrictions = numericRestrictions;
        this.stringRestrictions = stringRestrictions;
        this.nullRestrictions = nullRestrictions;
        this.typeRestrictions = typeRestrictions;
        this.dateTimeRestrictions = dateTimeRestrictions;
        this.formatRestrictions = formatRestrictions;
        this.granularityRestrictions = granularityRestrictions;
        this.mustContainRestriction = mustContainRestriction;
        this.source = source;
    }

    public SetRestrictions getSetRestrictions() {
        return setRestrictions;
    }

    public NumericRestrictions getNumericRestrictions() {
        return numericRestrictions;
    }

    public StringRestrictions getStringRestrictions() {
        return stringRestrictions;
    }

    public NullRestrictions getNullRestrictions() {
        return nullRestrictions;
    }

    public TypeRestrictions getTypeRestrictions() {
        return typeRestrictions;
    }

    public DateTimeRestrictions getDateTimeRestrictions() { return dateTimeRestrictions; }

    public GranularityRestrictions getGranularityRestrictions() { return granularityRestrictions; }

    public MustContainRestriction getMustContainRestriction() {
        return mustContainRestriction;
    }

    public FieldSpec withSetRestrictions(SetRestrictions setRestrictions, FieldSpecSource source) {
        return new FieldSpec(
            setRestrictions,
            this.numericRestrictions,
            this.stringRestrictions,
            this.nullRestrictions,
            this.typeRestrictions,
            this.dateTimeRestrictions,
            this.formatRestrictions,
            this.granularityRestrictions,
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
            this.granularityRestrictions,
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
            this.granularityRestrictions,
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
            this.granularityRestrictions,
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
            this.granularityRestrictions,
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
            this.granularityRestrictions,
            this.mustContainRestriction,
            this.source.combine(source));
    }

    public FieldSpec withGranularityRestrictions(GranularityRestrictions granularityRestrictions, FieldSpecSource source) {
        return new FieldSpec(
            this.setRestrictions,
            this.numericRestrictions,
            this.stringRestrictions,
            this.nullRestrictions,
            this.typeRestrictions,
            this.dateTimeRestrictions,
            this.formatRestrictions,
            granularityRestrictions,
            this.mustContainRestriction,
            this.source.combine(source));
    }

    @Override
    public String toString() {
        return String.join(
            " & ",
            Arrays
                .stream(getPropertiesToCompare(this))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toList()));
    }

    public FormatRestrictions getFormatRestrictions() {
        return formatRestrictions;
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
            this.granularityRestrictions,
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
            this.granularityRestrictions,
            mustContainRestriction,
            this.source);
    }

    public FieldSpecSource getFieldSpecSource() {
        return this.source;
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
            fieldSpec.granularityRestrictions,
            fieldSpec.mustContainRestriction,
            fieldSpec.nullRestrictions,
            fieldSpec.numericRestrictions,
            fieldSpec.setRestrictions,
            fieldSpec.stringRestrictions,
            fieldSpec.typeRestrictions
        };
    }
}

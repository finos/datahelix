package com.scottlogic.deg.generator.restrictions;

import java.util.Objects;

/**
 * Details a column's atomic constraints
 */
public class FieldSpec {
    static final FieldSpec Empty = new FieldSpec();

    private SetRestrictions setRestrictions;
    private NumericRestrictions numericRestrictions;
    private StringRestrictions stringRestrictions;
    private NullRestrictions nullRestrictions;
    private TypeRestrictions typeRestrictions;
    private DateTimeRestrictions dateTimeRestrictions;

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

    void setSetRestrictions(SetRestrictions setRestrictions) {
        this.setRestrictions = setRestrictions;
    }

    void setNumericRestrictions(NumericRestrictions numericRestrictions) {
        this.numericRestrictions = numericRestrictions;
    }

    void setStringRestrictions(StringRestrictions stringRestrictions) {
        this.stringRestrictions = stringRestrictions;
    }

    void setTypeRestrictions(TypeRestrictions typeRestrictions) {
        this.typeRestrictions = typeRestrictions;
    }

    void setNullRestrictions(NullRestrictions nullRestrictions) {
        this.nullRestrictions = nullRestrictions;
    }

    void setDateTimeRestrictions(DateTimeRestrictions dateTimeRestrictions) {
        this.dateTimeRestrictions = dateTimeRestrictions;
    }

    @Override
    public String toString() {
        return String.format(
                "%s | %s | %s | %s | %s | %s",
                Objects.toString(setRestrictions, "-"),
                Objects.toString(numericRestrictions, "-"),
                Objects.toString(stringRestrictions, "-"),
                Objects.toString(nullRestrictions, "-"),
                Objects.toString(typeRestrictions, "-"),
                Objects.toString(dateTimeRestrictions, "-")
        );
    }
}

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
    private DateTimeRestrictions dateTimeRestrictions;
    private FormatRestrictions formatRestrictions;

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

    public DateTimeRestrictions getDateTimeRestrictions() { return dateTimeRestrictions; }

    public void setSetRestrictions(SetRestrictions setRestrictions) {
        this.setRestrictions = setRestrictions;
    }

    public void setNumericRestrictions(NumericRestrictions numericRestrictions) {
        this.numericRestrictions = numericRestrictions;
    }

    public void setStringRestrictions(StringRestrictions stringRestrictions) {
        this.stringRestrictions = stringRestrictions;
    }

    public void setNullRestrictions(NullRestrictions nullRestrictions) {
        this.nullRestrictions = nullRestrictions;
    }

    public void setDateTimeRestrictions(DateTimeRestrictions dateTimeRestrictions) {
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
                Objects.toString(dateTimeRestrictions, "-")
        );
    }

    public FormatRestrictions getFormatRestrictions() {
        return formatRestrictions;
    }

    public void setFormatRestrictions(FormatRestrictions formatRestrictions) {
        this.formatRestrictions = formatRestrictions;
    }
}

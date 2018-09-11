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
    private FormatRestrictions formatRestrictions;
    private GranularityRestrictions granularityRestrictions;

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

    public void setSetRestrictions(SetRestrictions setRestrictions) {
        this.setRestrictions = setRestrictions;
    }

    public void setNumericRestrictions(NumericRestrictions numericRestrictions) {
        this.numericRestrictions = numericRestrictions;
    }

    public void setStringRestrictions(StringRestrictions stringRestrictions) {
        this.stringRestrictions = stringRestrictions;
    }

    public void setTypeRestrictions(TypeRestrictions typeRestrictions) {
        this.typeRestrictions = typeRestrictions;
    }

    public void setNullRestrictions(NullRestrictions nullRestrictions) {
        this.nullRestrictions = nullRestrictions;
    }

    public void setDateTimeRestrictions(DateTimeRestrictions dateTimeRestrictions) {
        this.dateTimeRestrictions = dateTimeRestrictions;
    }

    public void setGranularityRestrictions(GranularityRestrictions granularityRestrictions) {
        this.granularityRestrictions = granularityRestrictions;
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
                Objects.toString(dateTimeRestrictions, "-"),
                Objects.toString(granularityRestrictions, "-")
        );
    }

    public FormatRestrictions getFormatRestrictions() {
        return formatRestrictions;
    }

    public void setFormatRestrictions(FormatRestrictions formatRestrictions) {
        this.formatRestrictions = formatRestrictions;
    }
}

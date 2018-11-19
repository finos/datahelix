package com.scottlogic.deg.generator.restrictions;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return String.join(
            " & ",
            Stream.of(
                Objects.toString(setRestrictions, null),
                Objects.toString(numericRestrictions, null),
                Objects.toString(stringRestrictions, null),
                Objects.toString(nullRestrictions, null),
                Objects.toString(typeRestrictions, null),
                Objects.toString(dateTimeRestrictions, null),
                Objects.toString(granularityRestrictions, null))
            .filter(s -> s != null)
            .collect(Collectors.toList()));
    }

    public FormatRestrictions getFormatRestrictions() {
        return formatRestrictions;
    }

    public void setFormatRestrictions(FormatRestrictions formatRestrictions) {
        this.formatRestrictions = formatRestrictions;
    }
}

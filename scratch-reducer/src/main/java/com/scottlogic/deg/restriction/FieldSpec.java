package com.scottlogic.deg.restriction;

/**
 * Details a column's atomic constraints
 */
public class FieldSpec {
    private final String name;

    private SetRestrictions setRestrictions = new SetRestrictions();
    private NumericRestrictions numericRestrictions = new NumericRestrictions();
    private StringRestrictions stringRestrictions = new StringRestrictions();
    private NullRestrictions nullRestrictions = new NullRestrictions();

    public FieldSpec(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
}

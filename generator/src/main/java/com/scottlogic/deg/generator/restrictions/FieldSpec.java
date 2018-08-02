package com.scottlogic.deg.generator.restrictions;

/**
 * Details a column's atomic constraints
 */
public class FieldSpec {
    private final String name;

    private SetRestrictions setRestrictions;
    private NumericRestrictions numericRestrictions;
    private StringRestrictions stringRestrictions;
    private NullRestrictions nullRestrictions;
    private TypeRestrictions typeRestrictions;

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

    public TypeRestrictions getTypeRestrictions() {
        return typeRestrictions;
    }

    public void setTypeRestrictions(TypeRestrictions typeRestrictions) {
        this.typeRestrictions = typeRestrictions;
    }

    public void setNullRestrictions(NullRestrictions nullRestrictions) {
        this.nullRestrictions = nullRestrictions;
    }
}

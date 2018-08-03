package com.scottlogic.deg.generator.restrictions;

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
}

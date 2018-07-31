package com.scottlogic.deg.restriction;

/**
 * Details a column's atomic constraints
 */
public class FieldSpec {
    private final String name;

    private SetRestrictions<?> setRestrictions;
    private NumericRestrictions<? extends Number> numericRestrictions;
    private StringRestrictions stringRestrictions = new StringRestrictions();
    private NullRestrictions nullRestrictions = new NullRestrictions();

    public FieldSpec(String name) {
        this.name = name;
    }

}

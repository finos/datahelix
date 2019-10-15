package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.restrictions.StringRestrictions;
import com.scottlogic.deg.generator.restrictions.TypedRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;

import java.math.BigDecimal;
import java.util.Set;

public class RestrictionsFieldSpec extends BaseFieldSpec {

    private final TypedRestrictions restrictions;
    private final Set<Object> blacklist;

    public RestrictionsFieldSpec(TypedRestrictions restrictions, Set<Object> blacklist, boolean nullable) {
        super(nullable);
        this.restrictions = restrictions;
        this.blacklist = blacklist;
    }

    @Override
    public boolean permits(Object value) {
        return !blacklist.contains(value) && restrictions.match(value);
    }

    @Override
    public FieldValueSource getFieldValueSource() {
        if (restrictions instanceof StringRestrictions){
            return ((StringRestrictions) restrictions).createGenerator();
        }
        //TODO
        return null;
    }


    @Override
    public String toString() {
        return String.format("%s%s",
            nullable ? " " : " Not Null ",
            restrictions == null ? "" : restrictions);
    }
}

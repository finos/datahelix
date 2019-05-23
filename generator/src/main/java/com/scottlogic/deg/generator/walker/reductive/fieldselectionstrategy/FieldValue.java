package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.restrictions.FormatRestrictions;

public class FieldValue {
    private final Field field;
    private final DataBagValue dataBagValue;

    public FieldValue(Field field, DataBagValue dataBagValue){
        this.field = field;
        this.dataBagValue = dataBagValue;
    }

    public DataBagValue getDataBagValue() {
        return dataBagValue;
    }

    public Field getField() {
        return field;
    }

    @Override
    public String toString() {
        return dataBagValue.toString();
    }
}

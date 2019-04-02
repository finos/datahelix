package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.generator.restrictions.Nullness;
import com.scottlogic.deg.generator.restrictions.SetRestrictions;

import java.util.Collections;
import java.util.HashSet;

public class FieldSpecHelper {
    public FieldSpec getFieldSpecForValue(DataBagValue fieldValue){
        if (fieldValue.getValue() == null) {
            return getNullRequiredFieldSpec(FieldSpecSource.Empty);//TODO PAUL
        }
        return FieldSpec.Empty
            .withSetRestrictions(new SetRestrictions(new HashSet<>(Collections.singletonList(fieldValue.getValue())), null), FieldSpecSource.Empty)//TODO PAUL
            .withNullRestrictions(new NullRestrictions(Nullness.MUST_NOT_BE_NULL), FieldSpecSource.Empty);//TODO PAUL
    }

    private FieldSpec getNullRequiredFieldSpec(FieldSpecSource fieldSpecSource) {
        return FieldSpec.Empty
            .withNullRestrictions(new NullRestrictions(Nullness.MUST_BE_NULL), fieldSpecSource);
    }
}

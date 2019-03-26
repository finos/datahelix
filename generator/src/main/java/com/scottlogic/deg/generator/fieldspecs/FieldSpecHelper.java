package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.generator.restrictions.Nullness;
import com.scottlogic.deg.generator.restrictions.SetRestrictions;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FieldValue;

import java.util.Collections;
import java.util.HashSet;

public class FieldSpecHelper {
    public FieldSpec getFieldSpecForValue(FieldValue fieldValue){
        if (fieldValue.getValue() == null) {
            return getNullRequiredFieldSpec(fieldValue.getFieldSpecSource());
        }
        return FieldSpec.Empty
            .withSetRestrictions(new SetRestrictions(new HashSet<>(Collections.singletonList(fieldValue.getValue())), null), fieldValue.getFieldSpecSource())
            .withNullRestrictions(new NullRestrictions(Nullness.MUST_NOT_BE_NULL), fieldValue.getFieldSpecSource());
    }

    private FieldSpec getNullRequiredFieldSpec(FieldSpecSource fieldSpecSource) {
        return FieldSpec.Empty
            .withNullRestrictions(new NullRestrictions(Nullness.MUST_BE_NULL), fieldSpecSource);
    }
}

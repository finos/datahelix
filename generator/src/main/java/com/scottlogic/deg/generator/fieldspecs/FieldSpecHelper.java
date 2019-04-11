package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.Value;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.generator.restrictions.Nullness;
import com.scottlogic.deg.generator.restrictions.SetRestrictions;

import java.util.Collections;
import java.util.HashSet;

public class FieldSpecHelper {
    public FieldSpec getFieldSpecForValue(Value fieldValue){
        if (fieldValue.getValue() == null) {
            return getNullRequiredFieldSpec(fieldValue.source);
        }
        return FieldSpec.Empty
            .withSetRestrictions(new SetRestrictions(new HashSet<>(Collections.singletonList(fieldValue.getValue())), null), fieldValue.source)
            .withNullRestrictions(new NullRestrictions(Nullness.MUST_NOT_BE_NULL), fieldValue.source);
    }

    private FieldSpec getNullRequiredFieldSpec(FieldSpecSource fieldSpecSource) {
        return FieldSpec.Empty
            .withNullRestrictions(new NullRestrictions(Nullness.MUST_BE_NULL), fieldSpecSource);
    }
}

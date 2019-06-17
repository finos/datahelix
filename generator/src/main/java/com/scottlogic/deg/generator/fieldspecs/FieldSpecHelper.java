package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.restrictions.SetRestrictions;


import java.util.Collections;

public class FieldSpecHelper {
    public FieldSpec getFieldSpecForValue(DataBagValue fieldValue) {
        if (fieldValue.getUnformattedValue() == null) {
            return getNullRequiredFieldSpec();
        }
        return FieldSpec.Empty
            .withSetRestrictions(
                SetRestrictions.fromWhitelist(
                    Collections.singleton(fieldValue.getUnformattedValue())))
            .withNotNull();
    }

    private FieldSpec getNullRequiredFieldSpec() {
        return FieldSpec.mustBeNull();
    }
}

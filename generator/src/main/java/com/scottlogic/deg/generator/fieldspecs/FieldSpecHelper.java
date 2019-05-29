package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.common.profile.constraintdetail.Nullness;
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
                    Collections.singleton(fieldValue.getUnformattedValue())), FieldSpecSource.Empty)
            .withNullRestrictions(
                new NullRestrictions(Nullness.MUST_NOT_BE_NULL), FieldSpecSource.Empty);
    }

    private FieldSpec getNullRequiredFieldSpec() {
        return FieldSpec.Empty
            .withNullRestrictions(
                new NullRestrictions(Nullness.MUST_BE_NULL),
                FieldSpecSource.Empty);
    }
}

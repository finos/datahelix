package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.common.profile.constraintdetail.Nullness;
import com.scottlogic.deg.generator.restrictions.set.SetRestrictions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

class FieldSpecHelperTests {

    private FieldSpecHelper fieldSpecHelper = new FieldSpecHelper();

    @Test
    void getFieldSpecForValue() {
        DataBagValue input = new DataBagValue("value");

        FieldSpec actual = fieldSpecHelper.getFieldSpecForValue(input);

        FieldSpec expected = FieldSpec.Empty
            .withSetRestrictions(SetRestrictions.fromWhitelist(Collections.singleton("value")))
            .withNullRestrictions(new NullRestrictions(Nullness.MUST_NOT_BE_NULL));

        assertEquals(actual, expected);
    }

    @Test
    void getFieldSpecForNullValue() {
        DataBagValue input = new DataBagValue(null);

        FieldSpec actual = fieldSpecHelper.getFieldSpecForValue(input);

        FieldSpec expected = FieldSpec.Empty
            .withNullRestrictions(new NullRestrictions(Nullness.MUST_BE_NULL));

        assertEquals(actual, expected);
    }
}
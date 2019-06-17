package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.restrictions.SetRestrictions;
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
            .withNotNull();

        assertEquals(actual, expected);
    }

    @Test
    void getFieldSpecForNullValue() {
        DataBagValue input = new DataBagValue(null);

        FieldSpec actual = fieldSpecHelper.getFieldSpecForValue(input);

        FieldSpec expected = FieldSpec.mustBeNull();

        assertEquals(actual, expected);
    }
}
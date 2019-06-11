package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class ReductiveStateTests {
    Field field1 = new Field("field1");
    Field field2 = new Field("field2");
    ReductiveState reductiveState =
        new ReductiveState(new ProfileFields(Arrays.asList(field1, field2)));
    DataBagValue value1 = new DataBagValue("v1");
    DataBagValue value2 = new DataBagValue("v2");

    @Test
    void withFixedFieldValue() {
        ReductiveState stateWithOneFixedField = reductiveState.withFixedFieldValue(field1, value1);

        Map<Field, DataBagValue> expected = new HashMap<>();
        expected.put(field1, value1);

        assertThat(stateWithOneFixedField.allFieldsAreFixed(), sameBeanAs(false));
        assertThat(stateWithOneFixedField.getFieldValues(), sameBeanAs(expected));
    }

    @Test
    void withTwoFixedFieldValue() {
        ReductiveState stateWithBothFixedFields =
            reductiveState.withFixedFieldValue(field1, value1).withFixedFieldValue(field2, value2);

        Map<Field, DataBagValue> expected = new HashMap<>();
        expected.put(field1, value1);
        expected.put(field2, value2);

        assertThat(stateWithBothFixedFields.allFieldsAreFixed(), sameBeanAs(true));

        assertThat(stateWithBothFixedFields.getFieldValues(), sameBeanAs(expected));
    }
}

package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.common.output.DataBagValueSource;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FieldValue;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class ReductiveStateTests {
    Field field1 = new Field("field1");
    Field field2 = new Field("field2");
    ReductiveState reductiveState = new ReductiveState(new ProfileFields(Arrays.asList(field1, field2)));
    FieldValue value1 = new FieldValue(field1, new DataBagValue("v1", DataBagValueSource.Empty));
    FieldValue value2 = new FieldValue(field2, new DataBagValue("v2", DataBagValueSource.Empty));

    @Test
    void withFixedFieldValue() {
        ReductiveState stateWithOneFixedField = reductiveState.withFixedFieldValue(value1);

        Map<Field, FieldValue> expected = new HashMap<>();
        expected.put(field1, value1);

        assertThat(stateWithOneFixedField.allFieldsAreFixed(), sameBeanAs(false));
        assertThat(stateWithOneFixedField.getFieldValues(), sameBeanAs(expected));
    }

    @Test
    void withTwoFixedFieldValue() {
        ReductiveState stateWithBothFixedFields = reductiveState.withFixedFieldValue(value1).withFixedFieldValue(value2);

        Map<Field, FieldValue> expected = new HashMap<>();
        expected.put(field1, value1);
        expected.put(field2, value2);

        assertThat(stateWithBothFixedFields.allFieldsAreFixed(), sameBeanAs(true));

        assertThat(stateWithBothFixedFields.getFieldValues(), sameBeanAs(expected));
    }
}
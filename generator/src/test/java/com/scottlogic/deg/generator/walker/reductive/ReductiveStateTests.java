package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ReductiveStateTests {
    @Test
    void allFieldsAreFixed_lastFixedFieldNotNullAndFixedFieldsSizePlusOneEqualsFieldsSize_returnsTrue() {
        HashMap<Field, FixedField> fixedFields = new HashMap<>();
        fixedFields.put(
            new Field("Second Field"),
            new FixedField(new Field("Second Field"), null, null)
        );
        FixedField lastFixedField = new FixedField(
            new Field("First Field"),
            null,
            null
        );
        ReductiveState reductiveState = getReductiveState(
            Arrays.asList("First Field", "Second Field"),
            fixedFields
        ).with(lastFixedField);

        boolean result = reductiveState.allFieldsAreFixed();

        Assert.assertTrue(result);
    }

    @Test
    void allFieldsAreFixed_lastFixedFieldNullAndFixedFieldsSizeEqualToFieldSize_returnsTrue() {
        HashMap<Field, FixedField> fixedFields = new HashMap<>();
        fixedFields.put(
            new Field("First Field"),
            new FixedField(new Field("First Field"), null,null)
        );
        fixedFields.put(
            new Field("Second Field"),
            new FixedField(new Field("Second Field"), null,null)
        );
        ReductiveState reductiveState = getReductiveState(
            Arrays.asList("First Field", "Second Field"),
            fixedFields
        );

        boolean result = reductiveState.allFieldsAreFixed();

        Assert.assertTrue(result);
    }

    @Test
    void allFieldsAreFixed_lastFixedFieldNotNullAndFixedFieldsSizePlusOneDoesNotEqualFieldsSize_returnsFalse() {
        HashMap<Field, FixedField> fixedFields = new HashMap<>();
        fixedFields.put(
            new Field("Second Field"),
            new FixedField(new Field("Second Field"), null, null)
        );
        FixedField lastFixedField = new FixedField(
            new Field("First Field"),
            null,
            null
        );
        ReductiveState reductiveState = getReductiveState(
            Arrays.asList("First Field", "Second Field", "Third Field"),
            fixedFields
        );

        boolean result = reductiveState.allFieldsAreFixed();

        Assert.assertFalse(result);
    }

    @Test
    void allFieldsAreFixed_lastFixedFieldNullAndFixedFieldsSizeDoesNotEqualFieldsSize_returnsFalse() {
        HashMap<Field, FixedField> fixedFields = new HashMap<>();
        fixedFields.put(
            new Field("Second Field"),
            new FixedField(new Field("Second Field"), null, null)
        );
        ReductiveState reductiveState = getReductiveState(
            Arrays.asList("First Field", "Second Field"),
            fixedFields);

        boolean result = reductiveState.allFieldsAreFixed();

        Assert.assertFalse(result);
    }

    private ReductiveState getReductiveState(List<String> profileFields, HashMap<Field, FixedField> fixedFields) {
        ReductiveState initialState = new ReductiveState(
            new ProfileFields(profileFields
                .stream()
                .map(Field::new)
                .collect(Collectors.toList())));

        return fixedFields.values().stream().reduce(
            initialState,
            ReductiveState::with,
            (a, b) -> null);
    }
}

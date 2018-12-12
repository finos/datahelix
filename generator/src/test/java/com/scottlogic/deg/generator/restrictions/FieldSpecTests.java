package com.scottlogic.deg.generator.restrictions;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

class FieldSpecTests {
    @Test
    void equals_objIsNull_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty;

        boolean result = fieldSpec.equals(null);

        assertFalse(
            "Expected that when the other object is null a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_objTypeIsNotFieldSpec_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty;

        boolean result = fieldSpec.equals("Test");

        assertFalse(
            "Expected that when the other object type is not FieldSpec a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecHasSetRestrictionsAndOtherObjectSetRestrictionsNull_returnsFalse() {
        FieldSpec fieldSpec = new FieldSpec(
            new SetRestrictions(null, null),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        boolean result = fieldSpec.equals(FieldSpec.Empty);

        assertFalse(
            "Expected that when the field spec has set restrictions and the other object set restrictions are null a false value should be returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecSetRestrictionsNullAndOtherObjectHasSetRestrictions_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty;

        boolean result = fieldSpec.equals(
            new FieldSpec(
                new SetRestrictions(null, null),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
        );

        assertFalse(
            "Expected that when the field spec does not have set restrictions and the other object has set restrictions a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecSetRestrictionsNotNullAndOtherObjectSetRestrictionsNotNullAndSetRestrictionsAreNotEqual_returnsFalse() {
        FieldSpec fieldSpec = new FieldSpec(
            new SetRestrictions(
                new HashSet<>(
                    Arrays.asList(1, 2, 3)
                ),
                null
            ),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        boolean result = fieldSpec.equals(
            new FieldSpec(
                new SetRestrictions(
                    new HashSet<>(
                        Arrays.asList(1, 2, 3, 4)
                    ),
                    null
                ),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
        );

        assertFalse(
            "Expected that when the items in the set restrictions are not equal a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecNumericRestrictionsNotNullAndOtherObjectNumericRestrictionsNull_returnsFalse(){
        FieldSpec fieldSpec = new FieldSpec(
            null,
            new NumericRestrictions(),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        boolean result = fieldSpec.equals(FieldSpec.Empty);

        assertFalse(
            "Expected that when the field spec numeric restrictions is not null and the other object numeric restrictions are null a false value is returned but was true",
            result
        );
    }
}

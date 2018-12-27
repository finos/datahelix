package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
        FieldSpec fieldSpec = FieldSpec.Empty
            .withSetRestrictions(new SetRestrictions(null, null), null);

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
            FieldSpec.Empty.withSetRestrictions(
                new SetRestrictions(null, null),
                null)
        );

        assertFalse(
            "Expected that when the field spec does not have set restrictions and the other object has set restrictions a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecSetRestrictionsNotNullAndOtherObjectSetRestrictionsNotNullAndSetRestrictionsAreNotEqual_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty
            .withSetRestrictions(
            new SetRestrictions(
                new HashSet<>(
                    Arrays.asList(1, 2, 3)
                ),
                null
            ),
            null);

        boolean result = fieldSpec.equals(
            FieldSpec.Empty.withSetRestrictions(
                new SetRestrictions(
                    new HashSet<>(
                        Arrays.asList(1, 2, 3, 4)
                    ),
                    null
                ),
                null)
        );

        assertFalse(
            "Expected that when the items in the set restrictions are not equal a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecNumericRestrictionsNotNullAndOtherObjectNumericRestrictionsNull_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(),
            null);

        boolean result = fieldSpec.equals(FieldSpec.Empty);

        assertFalse(
            "Expected that when the field spec numeric restrictions is not null and the other object numeric restrictions are null a false value is returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecNumericRestrictionsNullAndOtherObjectNumericRestrictionsNotNull_returnsFalse() {
        FieldSpec fieldSpec = FieldSpec.Empty;

        boolean result = fieldSpec.equals(
            FieldSpec.Empty.withNumericRestrictions(
                new NumericRestrictions(),
                null)
        );

        assertFalse(
            "Expected that when the field spec does not have numeric restrictions and the other object has numeric restricitons are false value should be returned but was true",
            result
        );
    }

    @Test
    void equals_fieldSpecNumericRestrictionsNotNullAndOtherObjectNumericRestrictionsNotNullAndNumericRestrictionsAreNotEqual_returnsFalse() {
        NumericRestrictions firstFieldSpecRestrictions = new NumericRestrictions();
        firstFieldSpecRestrictions.min = new NumericLimit<>(new BigDecimal(1), false);
        firstFieldSpecRestrictions.max = new NumericLimit<>(new BigDecimal(20), false);
        FieldSpec fieldSpec = FieldSpec.Empty.withNumericRestrictions(firstFieldSpecRestrictions, null);

        NumericRestrictions secondFieldSpecRestrictions = new NumericRestrictions();
        secondFieldSpecRestrictions.min = new NumericLimit<>(new BigDecimal(5), false);
        secondFieldSpecRestrictions.max = new NumericLimit<>(new BigDecimal(20), false);
        boolean result = fieldSpec.equals(
            FieldSpec.Empty.withNumericRestrictions(secondFieldSpecRestrictions, null)
        );

        assertFalse(
            "Expected that when the numeric restriction values differ a false value is returned but was true",
            result
        );
    }
}

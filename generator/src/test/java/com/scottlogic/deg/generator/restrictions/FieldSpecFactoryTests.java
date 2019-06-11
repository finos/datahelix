package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import org.junit.jupiter.api.Test;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

class FieldSpecFactoryTests {
    private static final StringRestrictionsFactory stringRestrictionsFactory = new StringRestrictionsFactory();
    private FieldSpecFactory fieldSpecFactory = new FieldSpecFactory(stringRestrictionsFactory);


    @Test
    void construct_stringHasLengthConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        StringHasLengthConstraint constraint = new StringHasLengthConstraint(
            new Field("Test"),
            10,
            null
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_stringHasLengthConstraintViolatedTwice_returnsTheSameGeneratorInstance() {
        ViolatedAtomicConstraint constraint = new ViolatedAtomicConstraint(
            new StringHasLengthConstraint(
                new Field("Test"),
                10,
                null
            )
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_twoInstancesOfStringHasLengthConstraintCalledWithEqualValues_returnsTheSameGeneratorInstance() {
        StringHasLengthConstraint firstConstraint = new StringHasLengthConstraint(
            new Field("Test"),
            20,
            null
        );
        StringHasLengthConstraint secondConstraint = new StringHasLengthConstraint(
            new Field("Test"),
            20,
            null
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(firstConstraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(secondConstraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_isStringLongerThanConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        IsStringLongerThanConstraint constraint = new IsStringLongerThanConstraint(
            new Field("Test"),
            15,
            null
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_isStringLongerThanConstraintViolatedTwice_returnsTheSameGeneratorInstance() {
        ViolatedAtomicConstraint constraint = new ViolatedAtomicConstraint(
            new IsStringLongerThanConstraint(
                new Field("Test"),
                10,
                null
            )
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_twoInstancesOfIsStringLongerThanConstraintCalledWithEqualValues_returnsTheSameGeneratorInstance() {
        IsStringLongerThanConstraint firstConstraint = new IsStringLongerThanConstraint(
            new Field("Test"),
            20,
            null
        );
        IsStringLongerThanConstraint secondConstraint = new IsStringLongerThanConstraint(
            new Field("Test"),
            20,
            null
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(firstConstraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(secondConstraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_isStringShorterThanConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        IsStringShorterThanConstraint constraint = new IsStringShorterThanConstraint(
            new Field("Test"),
            25,
            null
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_isStringShorterThanConstraintViolatedTwice_returnsTheSameGeneratorInstance() {
        ViolatedAtomicConstraint constraint = new ViolatedAtomicConstraint(
            new IsStringShorterThanConstraint(
                new Field("Test"),
                10,
                null
            )
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }

    @Test
    void construct_twoInstancesOfIsStringShorterThanConstraintCalledWithEqualValues_returnsTheSameGeneratorInstance() {
        IsStringShorterThanConstraint firstConstraint = new IsStringShorterThanConstraint(
            new Field("Test"),
            20,
            null
        );
        IsStringShorterThanConstraint secondConstraint = new IsStringShorterThanConstraint(
            new Field("Test"),
            20,
            null
        );

        final FieldSpec firstInstance = fieldSpecFactory.construct(firstConstraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(secondConstraint);

        assertEquals(firstInstance.getStringRestrictions(), secondInstance.getStringRestrictions());
    }
}

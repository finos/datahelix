package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

class FieldSpecFactoryTests {
    @Test
    void toMustContainRestrictionFieldSpec_constraintsContainsNotConstraint_returnsMustContainsRestrictionWithNotConstraint() {
        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();
        FieldSpec nullFieldSpec = FieldSpec.Empty.withNullRestrictions(new NullRestrictions(Nullness.MUST_NOT_BE_NULL), FieldSpecSource.Empty);

        FieldSpec actualFieldSpec = fieldSpecFactory.toMustContainRestrictionFieldSpec(FieldSpec.Empty, Collections.singleton(nullFieldSpec));

        FieldSpec expectedFieldSpec = FieldSpec.Empty.withMustContainRestriction(
            new MustContainRestriction(
                new HashSet<FieldSpec>() {{
                    add(
                        FieldSpec.Empty.withNullRestrictions(
                            new NullRestrictions(Nullness.MUST_NOT_BE_NULL), FieldSpecSource.Empty)
                        .withTypeRestrictions(DataTypeRestrictions.ALL_TYPES_PERMITTED, null)
                    );
                }}
            )
        );

        Assert.assertEquals(expectedFieldSpec, actualFieldSpec);
    }

    @Test
    void construct_stringHasLengthConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        StringHasLengthConstraint constraint = new StringHasLengthConstraint(
            new Field("Test"),
            10,
            null
        );
        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        Assert.assertSame(firstInstance.getStringRestrictions().stringGenerator, secondInstance.getStringRestrictions().stringGenerator);
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
        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        Assert.assertSame(firstInstance.getStringRestrictions().stringGenerator, secondInstance.getStringRestrictions().stringGenerator);
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
        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();

        final FieldSpec firstInstance = fieldSpecFactory.construct(firstConstraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(secondConstraint);

        Assert.assertSame(firstInstance.getStringRestrictions().stringGenerator, secondInstance.getStringRestrictions().stringGenerator);
    }

    @Test
    void construct_stringHasLengthConstraintInstancesAreNotEqual_returnsDifferentStringGeneratorInstances() {
        StringHasLengthConstraint firstConstraint = new StringHasLengthConstraint(
            new Field("Test"),
            20,
            null
        );
        StringHasLengthConstraint secondConstraint = new StringHasLengthConstraint(
            new Field("Different"),
            20,
            null
        );

        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();

        final FieldSpec firstInstance = fieldSpecFactory.construct(firstConstraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(secondConstraint);

        Assert.assertNotSame(firstInstance.getStringRestrictions().stringGenerator, secondInstance.getStringRestrictions().stringGenerator);
    }

    @Test
    void construct_isStringLongerThanConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        IsStringLongerThanConstraint constraint = new IsStringLongerThanConstraint(
            new Field("Test"),
            15,
            null
        );
        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        Assert.assertSame(firstInstance.getStringRestrictions().stringGenerator, secondInstance.getStringRestrictions().stringGenerator);
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
        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        Assert.assertSame(firstInstance.getStringRestrictions().stringGenerator, secondInstance.getStringRestrictions().stringGenerator);
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
        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();

        final FieldSpec firstInstance = fieldSpecFactory.construct(firstConstraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(secondConstraint);

        Assert.assertSame(firstInstance.getStringRestrictions().stringGenerator, secondInstance.getStringRestrictions().stringGenerator);
    }

    @Test
    void construct_isStringLongerThanConstraintInstancesAreNotEqual_returnsDifferentStringGeneratorInstances() {
        IsStringLongerThanConstraint firstConstraint = new IsStringLongerThanConstraint(
            new Field("Test"),
            20,
            null
        );
        IsStringLongerThanConstraint secondConstraint = new IsStringLongerThanConstraint(
            new Field("Different"),
            20,
            null
        );

        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();

        final FieldSpec firstInstance = fieldSpecFactory.construct(firstConstraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(secondConstraint);

        Assert.assertNotSame(firstInstance.getStringRestrictions().stringGenerator, secondInstance.getStringRestrictions().stringGenerator);
    }

    @Test
    void construct_isStringShorterThanConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        IsStringShorterThanConstraint constraint = new IsStringShorterThanConstraint(
            new Field("Test"),
            25,
            null
        );
        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        Assert.assertSame(firstInstance.getStringRestrictions().stringGenerator, secondInstance.getStringRestrictions().stringGenerator);
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
        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();

        final FieldSpec firstInstance = fieldSpecFactory.construct(constraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(constraint);

        Assert.assertSame(firstInstance.getStringRestrictions().stringGenerator, secondInstance.getStringRestrictions().stringGenerator);
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
        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();

        final FieldSpec firstInstance = fieldSpecFactory.construct(firstConstraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(secondConstraint);

        Assert.assertSame(firstInstance.getStringRestrictions().stringGenerator, secondInstance.getStringRestrictions().stringGenerator);
    }

    @Test
    void construct_isStringShorterThanConstraintInstancesAreNotEqual_returnsDifferentStringGeneratorInstances() {
        IsStringShorterThanConstraint firstConstraint = new IsStringShorterThanConstraint(
            new Field("Test"),
            20,
            null
        );
        IsStringShorterThanConstraint secondConstraint = new IsStringShorterThanConstraint(
            new Field("Different"),
            20,
            null
        );

        FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();

        final FieldSpec firstInstance = fieldSpecFactory.construct(firstConstraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(secondConstraint);

        Assert.assertNotSame(firstInstance.getStringRestrictions().stringGenerator, secondInstance.getStringRestrictions().stringGenerator);
    }
}

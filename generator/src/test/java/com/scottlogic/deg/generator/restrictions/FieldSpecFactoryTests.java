package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.StringConstraintsCollection;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class FieldSpecFactoryTests {
    FieldSpecFactory fieldSpecFactory = new FieldSpecFactory(new FieldSpecMerger());
    TypeRestrictions typeRestrictions = new DataTypeRestrictions(Collections.singletonList(IsOfTypeConstraint.Types.STRING));
    StringRestrictions longerThanRestriction = new StringRestrictions(new StringConstraintsCollection(Collections.singleton(new IsStringLongerThanConstraint(null, 2 , null))));
    StringRestrictions shorterThanRestriction = new StringRestrictions(new StringConstraintsCollection(Collections.singleton(new IsStringShorterThanConstraint(null, 5 , null))));

    @Test
    void toMustContainRestrictionFieldSpec_constraintsContainsNotConstraint_returnsMustContainsRestrictionWithNotConstraint() {
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
    void toMustContainRestrictionFieldSpec_nonNullRootAndDecisionConstraint_returnsCorrectlyMerged() {
        //Arrange
        FieldSpec rootFieldSpec = FieldSpec.Empty.withTypeRestrictions(typeRestrictions, FieldSpecSource.Empty);

        Set<FieldSpec> decisionFieldSpecs = Collections.singleton(
            FieldSpec.Empty.withStringRestrictions(longerThanRestriction, FieldSpecSource.Empty));

        //Act
        FieldSpec actualFieldSpec = fieldSpecFactory.toMustContainRestrictionFieldSpec(rootFieldSpec, decisionFieldSpecs);

        //Assert
        FieldSpec expectedFieldSpec = rootFieldSpec.withMustContainRestriction(
            new MustContainRestriction(Collections.singleton(
            rootFieldSpec.withStringRestrictions(longerThanRestriction, FieldSpecSource.Empty)
        )));

        assertThat(actualFieldSpec, sameBeanAs(expectedFieldSpec));
    }

    @Test
    void toMustContainRestrictionFieldSpec_nonNullRootAndMultipleDecisionConstraints_returnsCorrectlyMerged() {
        //Arrange
        FieldSpec rootFieldSpec = FieldSpec.Empty.withTypeRestrictions(typeRestrictions, FieldSpecSource.Empty);

        Set<FieldSpec> decisionFieldSpecs = new HashSet<>(Arrays.asList(
            FieldSpec.Empty.withStringRestrictions(longerThanRestriction, FieldSpecSource.Empty),
            FieldSpec.Empty.withStringRestrictions(shorterThanRestriction, FieldSpecSource.Empty)
        ));

        //Act
        FieldSpec actualFieldSpec = fieldSpecFactory.toMustContainRestrictionFieldSpec(rootFieldSpec, decisionFieldSpecs);

        //Assert
        FieldSpec expectedFieldSpec = rootFieldSpec.withMustContainRestriction(
            new MustContainRestriction(new HashSet<>(Arrays.asList(
                rootFieldSpec.withStringRestrictions(longerThanRestriction, FieldSpecSource.Empty),
                rootFieldSpec.withStringRestrictions(shorterThanRestriction, FieldSpecSource.Empty)
            ))));

        assertThat(actualFieldSpec, sameBeanAs(expectedFieldSpec));
    }

    @Test
    void toMustContainRestrictionFieldSpec_multipleRootAndMultipleDecisionConstraints_returnsCorrectlyMerged() {
        //Arrange
        FieldSpec rootFieldSpec = FieldSpec.Empty.withTypeRestrictions(typeRestrictions, FieldSpecSource.Empty)
            .withNullRestrictions(new NullRestrictions(Nullness.MUST_NOT_BE_NULL), FieldSpecSource.Empty);

        Set<FieldSpec> decisionFieldSpecs = new HashSet<>(Arrays.asList(
            FieldSpec.Empty.withStringRestrictions(longerThanRestriction, FieldSpecSource.Empty),
            FieldSpec.Empty.withStringRestrictions(shorterThanRestriction, FieldSpecSource.Empty)
        ));

        //Act
        FieldSpec actualFieldSpec = fieldSpecFactory.toMustContainRestrictionFieldSpec(rootFieldSpec, decisionFieldSpecs);

        //Assert
        FieldSpec expectedFieldSpec = rootFieldSpec.withMustContainRestriction(
            new MustContainRestriction(new HashSet<>(Arrays.asList(
                rootFieldSpec.withStringRestrictions(longerThanRestriction, FieldSpecSource.Empty),
                rootFieldSpec.withStringRestrictions(shorterThanRestriction, FieldSpecSource.Empty)
            ))));

        assertThat(actualFieldSpec, sameBeanAs(expectedFieldSpec));
    }

    @Test
    void toMustContainRestrictionFieldSpec_emptyRootAndDecisionConstraint_returnsCorrectlyMerged() {
        //Arrange
        FieldSpec rootFieldSpec = FieldSpec.Empty;

        Set<FieldSpec> decisionFieldSpecs = new HashSet<>(Arrays.asList(
            FieldSpec.Empty.withStringRestrictions(longerThanRestriction, FieldSpecSource.Empty)
        ));

        //Act
        FieldSpec actualFieldSpec = fieldSpecFactory.toMustContainRestrictionFieldSpec(rootFieldSpec, decisionFieldSpecs);

        //Assert
        FieldSpec expectedFieldSpec = rootFieldSpec.withMustContainRestriction(
            new MustContainRestriction(new HashSet<>(Arrays.asList(
                rootFieldSpec.withStringRestrictions(longerThanRestriction, FieldSpecSource.Empty)
                    .withTypeRestrictions(DataTypeRestrictions.ALL_TYPES_PERMITTED, FieldSpecSource.Empty)
            ))));

        assertThat(actualFieldSpec, sameBeanAs(expectedFieldSpec));
    }

    @Test
    void toMustContainRestrictionFieldSpec_RootAndEmptyDecisionConstraints_returnsCorrectlyMerged() {
        //Arrange
        FieldSpec rootFieldSpec = FieldSpec.Empty.withTypeRestrictions(typeRestrictions, FieldSpecSource.Empty);

        Set<FieldSpec> decisionFieldSpecs = new HashSet<>();

        //Act
        FieldSpec actualFieldSpec = fieldSpecFactory.toMustContainRestrictionFieldSpec(rootFieldSpec, decisionFieldSpecs);

        //Assert
        FieldSpec expectedFieldSpec = rootFieldSpec;

        assertThat(actualFieldSpec, sameBeanAs(expectedFieldSpec));
    }


    @Test
    void construct_stringHasLengthConstraintRetrievedTwice_returnsTheSameGeneratorInstance() {
        StringHasLengthConstraint constraint = new StringHasLengthConstraint(
            new Field("Test"),
            10,
            null
        );

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

        final FieldSpec firstInstance = fieldSpecFactory.construct(firstConstraint);
        final FieldSpec secondInstance = fieldSpecFactory.construct(secondConstraint);

        Assert.assertNotSame(firstInstance.getStringRestrictions().stringGenerator, secondInstance.getStringRestrictions().stringGenerator);
    }
}

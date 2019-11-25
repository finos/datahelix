/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.datahelix.generator.orchestrator.violate.violator;

import com.scottlogic.datahelix.generator.common.profile.UnviolatableConstraintException;
import com.scottlogic.datahelix.generator.common.util.NumberUtils;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.LessThanConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.ViolatedAtomicConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.datahelix.generator.core.violations.filters.ViolationFilter;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createField;
import static com.scottlogic.datahelix.generator.orchestrator.violate.violator.TypeEqualityHelper.*;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests the behaviour of the RuleViolator class.
 */
public class ConstraintViolatorTests
{
    private ConstraintViolator target;

    private AtomicConstraint atomicConstraint1;
    private AtomicConstraint atomicConstraint2;
    private AtomicConstraint atomicConstraint3;
    private String ruleInformation;

    @Mock
    private ViolationFilter mockFilter;

    private Collection<Constraint> inputConstraints = new ArrayList<>();

    @BeforeEach
    public void commonTestSetup()
    {
        MockitoAnnotations.initMocks(this);

        List<ViolationFilter> inputFilters = Collections.singletonList(mockFilter);
        when(mockFilter.canViolate(any(Constraint.class))).thenReturn(true);

        target = new ConstraintViolator(inputFilters);

        atomicConstraint1 = new LessThanConstraint(createField("foo"), NumberUtils.coerceToBigDecimal(10));
        atomicConstraint2 = new LessThanConstraint(createField("bar"), NumberUtils.coerceToBigDecimal(20));
        atomicConstraint3 = new LessThanConstraint(createField("foobar"), NumberUtils.coerceToBigDecimal(30));
    }

    /**
     * Tests that the violate method with an AND constraint returns the correct OR constraint.
     * VIOLATE(AND(X, Y)) reduces to
     * OR(
     * AND(VIOLATE(X), Y),
     * AND(X, VIOLATE(Y)))
     */
    @Test
    public void violateRule_withAndConstraint_returnsOrConstraint()
    {
        //Arrange
        AndConstraint inputConstraint = new AndConstraint(atomicConstraint1, atomicConstraint2);

        //Act
        Constraint outputConstraint = target.violateConstraint(inputConstraint);

        //Assert
        Constraint expectedConstraint = new OrConstraint(
            new AndConstraint(new ViolatedAtomicConstraint(atomicConstraint1.negate()), atomicConstraint2),
            new AndConstraint(atomicConstraint1, new ViolatedAtomicConstraint(atomicConstraint2.negate()))
        );

        assertThat("The violate method should have returned the correct shaped constraint", outputConstraint, sameBeanAs(expectedConstraint));
        assertConstraintTypeEquality(expectedConstraint, outputConstraint);
    }

    /**
     * Tests that the violate method with a negated AND constraint returns the inner AND constraint.
     * VIOLATE(NOT(AND(X,Y))) reduces to AND(X,Y)
     */
    @Test
    public void violateRule_withNegatedAndConstraint_returnsAndConstraint()
    {
        //Arrange
        AndConstraint andConstraint = new AndConstraint(atomicConstraint1, atomicConstraint2);
        Constraint inputConstraint = andConstraint.negate();

        //Act
        Constraint outputConstraint = target.violateConstraint(inputConstraint);

        //Assert
        assertThat("The violate method should have returned the correct shaped constraint", outputConstraint, sameBeanAs(andConstraint));
        assertConstraintTypeEquality(andConstraint, outputConstraint);
    }

    /**
     * Tests that the violate method with an OR constraint returns the correct AND constraint.
     * VIOLATE(OR(X, Y)) reduces to AND(VIOLATE(X), VIOLATE(Y))
     */
    @Test
    public void violateRule_withOrConstraint_returnsAndConstraint()
    {
        //Arrange
        OrConstraint inputConstraint = new OrConstraint(atomicConstraint1, atomicConstraint2);

        //Act
        Constraint outputConstraint = target.violateConstraint(inputConstraint);

        //Assert
        Constraint expectedConstraint = new AndConstraint(
            new ViolatedAtomicConstraint(atomicConstraint1.negate()),
            new ViolatedAtomicConstraint(atomicConstraint2.negate())
        );

        assertThat("The violate method should have returned the correct shaped constraint", outputConstraint, sameBeanAs(expectedConstraint));
        assertConstraintTypeEquality(expectedConstraint, outputConstraint);
    }

    /**
     * Tests that the violate method with a negated OR constraint returns the inner OR constraint.
     * VIOLATE(NOT(OR(X, Y))) reduces to OR(X,Y)
     */
    @Test
    public void violateRule_withNegatedOrConstraint_returnsOrConstraint()
    {
        //Arrange
        OrConstraint orConstraint = new OrConstraint(atomicConstraint1, atomicConstraint2);
        Constraint inputConstraint = orConstraint.negate();

        //Act
        Constraint outputConstraint = target.violateConstraint(inputConstraint);

        //Assert
        assertThat("The violate method should have returned the correct shaped constraint", outputConstraint, sameBeanAs(orConstraint));
        assertConstraintTypeEquality(orConstraint, outputConstraint);
    }

    /**
     * Tests that the violate method with an IF THEN constraint (conditional constraint) returns the correct AND
     * constraint. VIOLATE(IF(X, then: Y)) reduces to AND(X, VIOLATE(Y))
     */
    @Test
    public void violateRule_withIfThenConstraint_returnsAndConstraint()
    {
        //Arrange
        ConditionalConstraint inputConstraint = new ConditionalConstraint(atomicConstraint1, atomicConstraint2);

        //Act
        Constraint outputConstraint = target.violateConstraint(inputConstraint);

        //Assert
        Constraint expectedConstraint = new AndConstraint(
            atomicConstraint1,
            new ViolatedAtomicConstraint(atomicConstraint2.negate())
        );

        assertThat("The violate method should have returned the correct shaped constraint", outputConstraint, sameBeanAs(expectedConstraint));
        assertConstraintTypeEquality(expectedConstraint, outputConstraint);
    }

    /**
     * Tests that the violate method with a negated IF constraint returns the inner IF constraint.
     * VIOLATE(NOT(IF(X, then: Y))) reduces to IF(X, then: Y)
     */
    @Test
    public void violateRule_withNegatedIfThenConstraint_returnsIfThenConstraint()
    {
        //Arrange
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(atomicConstraint1, atomicConstraint2);
        Constraint inputConstraint = conditionalConstraint.negate();

        //Act
        Constraint outputConstraint = target.violateConstraint(inputConstraint);

        //Assert
        assertThat("The violate method should have returned the correct shaped constraint", outputConstraint, sameBeanAs(conditionalConstraint));
        assertConstraintTypeEquality(conditionalConstraint, outputConstraint);
    }

    /**
     * Tests that the violate method with an IF THEN ELSE constraint (conditional constraint) returns the correct OR
     * constraint.
     * VIOLATE(IF(X, then: Y, else: Z)) reduces to OR(AND(X, VIOLATE(Y)), AND(VIOLATE(X), VIOLATE(Z)))
     */
    @Test
    public void violateRule_withIfThenElseConstraint_returnsAndConstraint()
    {
        //Arrange
        ConditionalConstraint inputConstraint = new ConditionalConstraint(atomicConstraint1, atomicConstraint2, atomicConstraint3);

        //Act
        Constraint outputConstraint = target.violateConstraint(inputConstraint);

        //Assert
        Constraint expectedConstraint = new OrConstraint(
            new AndConstraint(
                atomicConstraint1,
                new ViolatedAtomicConstraint(atomicConstraint2.negate())),
            new AndConstraint(
                new ViolatedAtomicConstraint(atomicConstraint1.negate()),
                new ViolatedAtomicConstraint(atomicConstraint3.negate()))
        );

        assertThat("The violate method should have returned the correct shaped constraint", outputConstraint, sameBeanAs(expectedConstraint));
        assertConstraintTypeEquality(expectedConstraint, outputConstraint);
    }

    /**
     * Tests that the violate method with a negated OR constraint returns the inner OR constraint.
     * VIOLATE(NOT(IF(X, then: Y, else: Z))) reduces to IF(X, then: Y, else: Z)
     */
    @Test
    public void violateRule_withNegatedIfThenElseConstraint_returnsIfThenElseConstraint()
    {
        //Arrange
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(atomicConstraint1, atomicConstraint2, atomicConstraint3);
        Constraint inputConstraint = conditionalConstraint.negate();

        //Act
        Constraint outputConstraint = target.violateConstraint(inputConstraint);

        //Assert
        assertThat("The violate method should have returned the correct shaped constraint", outputConstraint, sameBeanAs(conditionalConstraint));
        assertConstraintTypeEquality(conditionalConstraint, outputConstraint);
    }

    /**
     * Tests that the violate method with an atomic constraint returns the correct negated atomic constraint.
     * VIOLATE(AtomicConstraint) reduces to NEGATE(AtomicConstraint)
     */
    @Test
    public void violateRule_withAtomicConstraint_returnsNegatedViolatedAtomicConstraint()
    {
        //Act
        Constraint outputConstraint = target.violateConstraint(atomicConstraint1);

        //Assert
        Constraint expectedConstraint = new ViolatedAtomicConstraint(atomicConstraint1.negate());

        assertThat("The violate method should have returned the correct shaped constraint", outputConstraint, sameBeanAs(expectedConstraint));
        assertConstraintTypeEquality(expectedConstraint, outputConstraint);
    }

    /**
     * Tests that the violate method with multiple atomic constraint returns the correct combinations of negated atomic
     * constraints.
     * VIOLATE(RULE(X,Y,Z)) reduces to VIOLATE(X),VIOLATE(Y),VIOLATE(Z)
     */
    @Test
    public void violateRule_withMultipleAtomicConstraints_returnsViolatedConstraints()
    {
        //Arrange
        inputConstraints.add(atomicConstraint1);
        inputConstraints.add(atomicConstraint2);
        inputConstraints.add(atomicConstraint3);

        //Act
        Constraint outputConstraint = target.violateConstraint(new AndConstraint(inputConstraints));

        //Assert
        Constraint expectedConstraint = new OrConstraint(
            new AndConstraint(
                new ViolatedAtomicConstraint(atomicConstraint1.negate()),
                atomicConstraint2,
                atomicConstraint3
            ),
            new AndConstraint(
                atomicConstraint1,
                new ViolatedAtomicConstraint(atomicConstraint2.negate()),
                atomicConstraint3
            ),
            new AndConstraint(
                atomicConstraint1,
                atomicConstraint2,
                new ViolatedAtomicConstraint(atomicConstraint3.negate())
            )
        );

        assertThat("The violate method should have returned the correct shaped constraint", outputConstraint, sameBeanAs(expectedConstraint));
        assertConstraintTypeEquality(expectedConstraint, outputConstraint);
    }

    /**
     * Tests that the violate method with unsupported constraint type throws UnviolatableConstraintException exception.
     */
    @Test
    public void violateRule_withUnsupportedConstraints_throwsRuntimeException()
    {
        //Arrange
        Constraint inputConstraint = Mockito.mock(Constraint.class);

        //Act/Assert
        assertThrows(UnviolatableConstraintException.class,
            () -> target.violateConstraint(inputConstraint),
            "Violate method should throw with a non-specific Constraint type object."
        );
    }

    /**
     * Tests that the violate method with empty violation filter ignores missing filter.
     */
    @Test
    public void violateRule_withEmptyViolationFilter_doesNotThrow()
    {
        //Arrange
        target = new ConstraintViolator(new ArrayList<>());

        //Act
        Constraint outputConstraint = target.violateConstraint(atomicConstraint1);

        //Assert
        Assert.assertNotNull("Rule violation should complete successfully.", outputConstraint);
    }

    /**
     * Tests that the violate method with multiple atomic constraints all filtered out returns non violated rule.
     * VIOLATE(RULE(X,Y)), ignore constraints X,Y reduces to X,Y
     */
    @Test
    public void violateRule_withViolationFilterOnly_returnsNonViolatedConstraints()
    {
        //Arrange
        inputConstraints.add(atomicConstraint1);
        inputConstraints.add(atomicConstraint2);
        Mockito.reset(mockFilter);
        when(mockFilter.canViolate(atomicConstraint1)).thenReturn(false);
        when(mockFilter.canViolate(atomicConstraint2)).thenReturn(false);

        //Act
        List<Constraint> outputConstraints = inputConstraints.stream().map(target::violateConstraint)
            .collect(Collectors.toList());

        //Assert
        List<Constraint> expectedConstraints = Arrays.asList(atomicConstraint1, atomicConstraint2);

        assertThat("The violate method should have returned the correct shaped constraint", outputConstraints, sameBeanAs(expectedConstraints));
        assertConstraintListTypeEquality(expectedConstraints, outputConstraints);
    }

    /**
     * Tests that the violate method with multiple atomic constraint and the first constraint filtered out returns a
     * rule including the non-violated constraint and the combinations of the remaining negated constraint.
     * VIOLATE(RULE(X,Y,Z)), ignore constraint X reduces to X,VIOLATE(Y),VIOLATE(Z)
     */
    @Test
    public void violateRule_withViolationFilterFirst_returnsViolatedAndNonViolatedConstraints()
    {
        //Arrange
        inputConstraints.add(atomicConstraint1);
        inputConstraints.add(atomicConstraint2);
        inputConstraints.add(atomicConstraint3);

        Mockito.reset(mockFilter);
        when(mockFilter.canViolate(atomicConstraint1)).thenReturn(false);
        when(mockFilter.canViolate(atomicConstraint2)).thenReturn(true);
        when(mockFilter.canViolate(atomicConstraint3)).thenReturn(true);

        //Act
        Constraint outputConstraint = target.violateConstraint(new AndConstraint(inputConstraints));

        //Assert
        Constraint expectedConstraint = new OrConstraint(
                new AndConstraint(
                    atomicConstraint1,
                    atomicConstraint2,
                    atomicConstraint3
                ),
                new AndConstraint(
                    atomicConstraint1,
                    new ViolatedAtomicConstraint(atomicConstraint2.negate()),
                    atomicConstraint3
                ),
                new AndConstraint(
                    atomicConstraint1,
                    atomicConstraint2,
                    new ViolatedAtomicConstraint(atomicConstraint3.negate())
                )
            );

        assertThat("The violate method should have returned the correct shaped constraint", outputConstraint, sameBeanAs(expectedConstraint));
        assertConstraintTypeEquality(expectedConstraint, outputConstraint);
    }

    /**
     * Tests that the violate method with multiple atomic constraint and the last constraint filtered out returns a
     * rule including the non-violated constraint and the combinations of the remaining negated constraint.
     * VIOLATE(RULE(X,Y,Z)), ignore constraint Z reduces to Z,VIOLATE(X),VIOLATE(Y)
     */
    @Test
    public void violateRule_withViolationFilterLast_returnsViolatedAndNonViolatedConstraints()
    {
        //Arrange
        inputConstraints.add(atomicConstraint1);
        inputConstraints.add(atomicConstraint2);
        inputConstraints.add(atomicConstraint3);

        Mockito.reset(mockFilter);
        when(mockFilter.canViolate(atomicConstraint1)).thenReturn(true);
        when(mockFilter.canViolate(atomicConstraint2)).thenReturn(true);
        when(mockFilter.canViolate(atomicConstraint3)).thenReturn(false);


        //Act
        Constraint outputConstraint = target.violateConstraint(new AndConstraint(inputConstraints));

        //Assert
        Constraint expectedConstraint = new OrConstraint(
            new AndConstraint(
                new ViolatedAtomicConstraint(atomicConstraint1.negate()),
                atomicConstraint2,
                atomicConstraint3
            ),
            new AndConstraint(
                atomicConstraint1,
                new ViolatedAtomicConstraint(atomicConstraint2.negate()),
                atomicConstraint3
            ),
            new AndConstraint(
                atomicConstraint1,
                atomicConstraint2,
                atomicConstraint3
            )
        );

        assertThat("The violate method should have returned the correct shaped constraint", outputConstraint, sameBeanAs(expectedConstraint));
        assertConstraintTypeEquality(expectedConstraint, outputConstraint);
    }
}
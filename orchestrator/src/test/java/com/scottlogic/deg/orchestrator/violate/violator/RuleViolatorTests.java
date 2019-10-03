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

package com.scottlogic.deg.orchestrator.violate.violator;

import com.scottlogic.deg.generator.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraintdetail.UnviolatableConstraintException;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.IsLessThanConstantConstraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.ViolatedAtomicConstraint;
import com.scottlogic.deg.generator.profile.RuleInformation;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static com.scottlogic.deg.orchestrator.violate.violator.TypeEqualityHelper.assertRuleTypeEquality;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

/**
 * Tests the behaviour of the RuleViolator class.
 */
public class RuleViolatorTests {

    private RuleViolator target;

    private AtomicConstraint atomicConstraint1;
    private AtomicConstraint atomicConstraint2;
    private AtomicConstraint atomicConstraint3;
    private RuleInformation ruleInformation;

    @Mock private ViolationFilter mockFilter;

    private Collection<Constraint> inputConstraints = new ArrayList<>();

    @BeforeEach
    public void commonTestSetup() {
        MockitoAnnotations.initMocks(this);

        List<ViolationFilter> inputFilters = Collections.singletonList(mockFilter);
        when(mockFilter.canViolate(any(Constraint.class))).thenReturn(true);

        target = new RuleViolator(inputFilters);

        atomicConstraint1 = new IsLessThanConstantConstraint(createField("foo"), BigDecimal.valueOf(10));
        atomicConstraint2 = new IsLessThanConstantConstraint(createField("bar"), BigDecimal.valueOf(20));
        atomicConstraint3 = new IsLessThanConstantConstraint(createField("foobar"), BigDecimal.valueOf(30));
        ruleInformation = new RuleInformation();
    }

    /**
     * Tests that the violate method with an AND constraint returns the correct OR constraint.
     * VIOLATE(AND(X, Y)) reduces to
     *         OR(
     *            AND(VIOLATE(X), Y),
     *            AND(X, VIOLATE(Y)))
     */
    @Test
    public void violateRule_withAndConstraint_returnsOrConstraint() {
        //Arrange
        AndConstraint andConstraint = new AndConstraint(atomicConstraint1, atomicConstraint2);
        inputConstraints.add(andConstraint);

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        Rule expectedRule = new Rule(
            ruleInformation,
            Collections.singleton(
                new OrConstraint(
                    new AndConstraint(new ViolatedAtomicConstraint(atomicConstraint1.negate()), atomicConstraint2),
                    new AndConstraint(atomicConstraint1, new ViolatedAtomicConstraint(atomicConstraint2.negate()))
                ))
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }

    /**
     * Tests that the violate method with a negated AND constraint returns the inner AND constraint.
     * VIOLATE(NOT(AND(X,Y))) reduces to AND(X,Y)
     */
    @Test
    public void violateRule_withNegatedAndConstraint_returnsAndConstraint() {
        //Arrange
        AndConstraint andConstraint = new AndConstraint(atomicConstraint1, atomicConstraint2);
        inputConstraints.add(andConstraint.negate());

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        Rule expectedRule = new Rule(
            ruleInformation,
            Collections.singleton(andConstraint)
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }

    /**
     * Tests that the violate method with an OR constraint returns the correct AND constraint.
     * VIOLATE(OR(X, Y)) reduces to AND(VIOLATE(X), VIOLATE(Y))
     */
    @Test
    public void violateRule_withOrConstraint_returnsAndConstraint() {
        //Arrange
        OrConstraint orConstraint = new OrConstraint(atomicConstraint1, atomicConstraint2);
        inputConstraints.add(orConstraint);

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        Rule expectedRule = new Rule(
            ruleInformation,
            Collections.singleton(
                new AndConstraint(
                    new ViolatedAtomicConstraint(atomicConstraint1.negate()),
                    new ViolatedAtomicConstraint(atomicConstraint2.negate())
                ))
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }

    /**
     * Tests that the violate method with a negated OR constraint returns the inner OR constraint.
     * VIOLATE(NOT(OR(X, Y))) reduces to OR(X,Y)
     */
    @Test
    public void violateRule_withNegatedOrConstraint_returnsOrConstraint() {
        //Arrange
        OrConstraint orConstraint = new OrConstraint(atomicConstraint1, atomicConstraint2);
        inputConstraints.add(orConstraint.negate());

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        Rule expectedRule = new Rule(
            ruleInformation,
            Collections.singleton(orConstraint)
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }

    /**
     * Tests that the violate method with an IF THEN constraint (conditional constraint) returns the correct AND
     * constraint. VIOLATE(IF(X, then: Y)) reduces to AND(X, VIOLATE(Y))
     */
    @Test
    public void violateRule_withIfThenConstraint_returnsAndConstraint() {
        //Arrange
        ConditionalConstraint ifThenConstraint = new ConditionalConstraint(atomicConstraint1, atomicConstraint2);
        inputConstraints.add(ifThenConstraint);

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        Rule expectedRule = new Rule(
            ruleInformation,
            Collections.singleton(
                new AndConstraint(
                    atomicConstraint1,
                    new ViolatedAtomicConstraint(atomicConstraint2.negate())
                ))
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }

    /**
     * Tests that the violate method with a negated IF constraint returns the inner IF constraint.
     * VIOLATE(NOT(IF(X, then: Y))) reduces to IF(X, then: Y)
     */
    @Test
    public void violateRule_withNegatedIfThenConstraint_returnsIfThenConstraint() {
        //Arrange
        ConditionalConstraint ifThenConstraint = new ConditionalConstraint(atomicConstraint1, atomicConstraint2);
        inputConstraints.add(ifThenConstraint.negate());

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        Rule expectedRule = new Rule(
            ruleInformation,
            Collections.singleton(ifThenConstraint)
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }

    /**
     * Tests that the violate method with an IF THEN ELSE constraint (conditional constraint) returns the correct OR
     * constraint.
     * VIOLATE(IF(X, then: Y, else: Z)) reduces to OR(AND(X, VIOLATE(Y)), AND(VIOLATE(X), VIOLATE(Z)))
     */
    @Test
    public void violateRule_withIfThenElseConstraint_returnsAndConstraint() {
        //Arrange
        ConditionalConstraint ifThenElseConstraint = new ConditionalConstraint(atomicConstraint1, atomicConstraint2, atomicConstraint3);
        inputConstraints.add(ifThenElseConstraint);

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        Rule expectedRule = new Rule(
            ruleInformation,
            Collections.singleton(
                new OrConstraint(
                    new AndConstraint(
                        atomicConstraint1,
                        new ViolatedAtomicConstraint(atomicConstraint2.negate())),
                    new AndConstraint(
                        new ViolatedAtomicConstraint(atomicConstraint1.negate()),
                        new ViolatedAtomicConstraint(atomicConstraint3.negate()))
                ))
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }

    /**
     * Tests that the violate method with a negated OR constraint returns the inner OR constraint.
     * VIOLATE(NOT(IF(X, then: Y, else: Z))) reduces to IF(X, then: Y, else: Z)
     */
    @Test
    public void violateRule_withNegatedIfThenElseConstraint_returnsIfThenElseConstraint() {
        //Arrange
        ConditionalConstraint ifThenElseConstraint = new ConditionalConstraint(atomicConstraint1, atomicConstraint2, atomicConstraint3);
        inputConstraints.add(ifThenElseConstraint.negate());

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        Rule expectedRule = new Rule(
            ruleInformation,
            Collections.singleton(ifThenElseConstraint)
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }

    /**
     * Tests that the violate method with an atomic constraint returns the correct negated atomic constraint.
     * VIOLATE(AtomicConstraint) reduces to NEGATE(AtomicConstraint)
     */
    @Test
    public void violateRule_withAtomicConstraint_returnsNegatedViolatedAtomicConstraint() {
        //Arrange
        inputConstraints.add(atomicConstraint1);

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        Rule expectedRule = new Rule(
            ruleInformation,
            Collections.singleton(
                new ViolatedAtomicConstraint(atomicConstraint1.negate())
                )
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }

    /**
     * Tests that the violate method with multiple atomic constraint returns the correct combinations of negated atomic
     * constraints.
     * VIOLATE(RULE(X,Y,Z)) reduces to VIOLATE(X),VIOLATE(Y),VIOLATE(Z)
     */
    @Test
    public void violateRule_withMultipleAtomicConstraints_returnsViolatedConstraints() {
        //Arrange
        inputConstraints.add(atomicConstraint1);
        inputConstraints.add(atomicConstraint2);
        inputConstraints.add(atomicConstraint3);

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        Rule expectedRule = new Rule(
            ruleInformation,
            Arrays.asList(
                new OrConstraint(
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
                )
            )
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }

    /**
     * Tests that the violate method with unsupported constraint type throws UnviolatableConstraintException exception.
     */
    @Test
    public void violateRule_withUnsupportedConstraints_throwsRuntimeException() {
        //Arrange
        Constraint mockConstraint = Mockito.mock(Constraint.class);
        inputConstraints.add(mockConstraint);

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act/Assert
        assertThrows(
            UnviolatableConstraintException.class,
            () -> target.violateRule(inputRule),
            "Violate method should throw with a non-specific Constraint type object."
        );
    }

    /**
     * Tests that the violate method with empty violation filter ignores missing filter.
     */
    @Test
    public void violateRule_withEmptyViolationFilter_doesNotThrow() {
        //Arrange
        target = new RuleViolator(new ArrayList<>());

        inputConstraints.add(atomicConstraint1);
        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        Assert.assertNotNull("Rule violation should complete successfully.", outputRule);
    }

    /**
     * Tests that the violate method with multiple atomic constraints all filtered out returns non violated rule.
     * VIOLATE(RULE(X,Y)), ignore constraints X,Y reduces to X,Y
     */
    @Test
    public void violateRule_withViolationFilterOnly_returnsNonViolatedConstraints() {
        //Arrange
        inputConstraints.add(atomicConstraint1);
        inputConstraints.add(atomicConstraint2);

        Mockito.reset(mockFilter);
        when(mockFilter.canViolate(atomicConstraint1)).thenReturn(false);
        when(mockFilter.canViolate(atomicConstraint2)).thenReturn(false);

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        Rule expectedRule = new Rule(
            ruleInformation,
            Arrays.asList(
                atomicConstraint1,
                atomicConstraint2
            )
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }

    /**
     * Tests that the violate method with multiple atomic constraint and the first constraint filtered out returns a
     * rule including the non-violated constraint and the combinations of the remaining negated constraint.
     * VIOLATE(RULE(X,Y,Z)), ignore constraint X reduces to X,VIOLATE(Y),VIOLATE(Z)
     */
    @Test
    public void violateRule_withViolationFilterFirst_returnsViolatedAndNonViolatedConstraints() {
        //Arrange
        inputConstraints.add(atomicConstraint1);
        inputConstraints.add(atomicConstraint2);
        inputConstraints.add(atomicConstraint3);

        Mockito.reset(mockFilter);
        when(mockFilter.canViolate(atomicConstraint1)).thenReturn(false);
        when(mockFilter.canViolate(atomicConstraint2)).thenReturn(true);
        when(mockFilter.canViolate(atomicConstraint3)).thenReturn(true);

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        Rule expectedRule = new Rule(
            ruleInformation,
            Arrays.asList(
                atomicConstraint1,
                new OrConstraint(
                    new AndConstraint(
                        new ViolatedAtomicConstraint(atomicConstraint2.negate()),
                        atomicConstraint3
                    ),
                    new AndConstraint(
                        atomicConstraint2,
                        new ViolatedAtomicConstraint(atomicConstraint3.negate())
                    )
                )
            )
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }

    /**
     * Tests that the violate method with multiple atomic constraint and the last constraint filtered out returns a
     * rule including the non-violated constraint and the combinations of the remaining negated constraint.
     * VIOLATE(RULE(X,Y,Z)), ignore constraint Z reduces to Z,VIOLATE(X),VIOLATE(Y)
     */
    @Test
    public void violateRule_withViolationFilterLast_returnsViolatedAndNonViolatedConstraints() {
        //Arrange
        inputConstraints.add(atomicConstraint1);
        inputConstraints.add(atomicConstraint2);
        inputConstraints.add(atomicConstraint3);

        Mockito.reset(mockFilter);
        when(mockFilter.canViolate(atomicConstraint1)).thenReturn(true);
        when(mockFilter.canViolate(atomicConstraint2)).thenReturn(true);
        when(mockFilter.canViolate(atomicConstraint3)).thenReturn(false);

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        Rule expectedRule = new Rule(
            ruleInformation,
            Arrays.asList(
                atomicConstraint3,
                new OrConstraint(
                    new AndConstraint(
                        new ViolatedAtomicConstraint(atomicConstraint1.negate()),
                        atomicConstraint2
                    ),
                    new AndConstraint(
                        atomicConstraint1,
                        new ViolatedAtomicConstraint(atomicConstraint2.negate())
                    )
                )
            )
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }
}
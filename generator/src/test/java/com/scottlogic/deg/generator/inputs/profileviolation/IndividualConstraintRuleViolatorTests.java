package com.scottlogic.deg.generator.inputs.profileviolation;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.profile.RuleInformation;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsLessThanConstantConstraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.*;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static com.scottlogic.deg.generator.inputs.profileviolation.TypeEqualityHelper.assertRuleTypeEquality;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests the behaviour of the IndividualConstraintRuleViolator class.
 */
public class IndividualConstraintRuleViolatorTests {

    private IndividualConstraintRuleViolator target;

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

        target = new IndividualConstraintRuleViolator(inputFilters);

        atomicConstraint1 = new IsLessThanConstantConstraint(new Field("foo"), 10, null);
        atomicConstraint2 = new IsLessThanConstantConstraint(new Field("bar"), 20, null);
        atomicConstraint3 = new IsLessThanConstantConstraint(new Field("foobar"), 30, null);
        ruleInformation = new RuleInformation();
    }

    @Test
    public void violateRule_withSingleConstraint_returnsNegatedEquivalent() {
        //Arrange
        Constraint constraint = Mockito.mock(Constraint.class);
        Constraint negatedConstraint = Mockito.mock(Constraint.class);
        inputConstraints.add(constraint);
        Rule inputRule = Mockito.mock(Rule.class);
        Mockito.when(inputRule.getConstraints()).thenReturn(inputConstraints);
        Mockito.when(constraint.negate()).thenReturn(negatedConstraint);

        //Act
        Rule outputRule = target.violateRule(inputRule);

        //Assert
        assertEquals(1, outputRule.getConstraints().size(), "The violate method should have returned the correct shaped rule");
        assertEquals(negatedConstraint, outputRule.getConstraints().toArray()[0], "The violate method should have returned the correct shaped rule");
        assertEquals(inputRule.getRuleInformation(), outputRule.getRuleInformation());
    }

    /**
     * Tests that the violate method with multiple atomic constraint returns the correct combinations of negated AND
     * constraint.
     * VIOLATE(RULE(X,Y,Z)) reduces to NOT(AND(X, Y, Z))
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
                new AndConstraint(atomicConstraint1, atomicConstraint2, atomicConstraint3).negate()
            )
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }

    /**
     * Tests that the violate method with empty violation filter ignores missing filter.
     */
    @Test
    public void violateRule_withEmptyViolationFilter_doesNotThrow() {
        //Arrange
        target = new IndividualConstraintRuleViolator(new ArrayList<>());

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
     * VIOLATE(RULE(X,Y,Z)), ignore constraint X reduces to AND(X, NOT(AND(X, Y)))
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
                new AndConstraint(
                    atomicConstraint2,
                    atomicConstraint3
                ).negate()
            )
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }

    /**
     * Tests that the violate method with multiple atomic constraint and the last constraint filtered out returns a
     * rule including the non-violated constraint and the combinations of the remaining negated constraint.
     * VIOLATE(RULE(X,Y,Z)), ignore constraint Z reduces to AND(Z, NOT(AND(X, Y)))
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
                new AndConstraint(
                    atomicConstraint1,
                    atomicConstraint2
                ).negate()
            )
        );

        assertThat("The violate method should have returned the correct shaped rule", outputRule, sameBeanAs(expectedRule));
        assertRuleTypeEquality(expectedRule, outputRule);
    }
}
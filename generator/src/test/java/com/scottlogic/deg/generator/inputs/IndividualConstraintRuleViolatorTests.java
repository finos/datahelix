package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.UnviolatableConstraintException;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsLessThanConstantConstraint;
import com.scottlogic.deg.generator.constraints.atomic.NotConstraint;
import com.scottlogic.deg.generator.constraints.atomic.ViolatedAtomicConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;
import com.scottlogic.deg.schemas.v3.RuleDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
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

    @Before
    public void commonTestSetup() {
        MockitoAnnotations.initMocks(this);

        List<ViolationFilter> inputFilters = Collections.singletonList(mockFilter);
        when(mockFilter.canViolate(any(Constraint.class))).thenReturn(true);

        target = new IndividualConstraintRuleViolator(inputFilters);

        atomicConstraint1 = new IsLessThanConstantConstraint(new Field("foo"), 10, null);
        atomicConstraint2 = new IsLessThanConstantConstraint(new Field("bar"), 20, null);
        atomicConstraint3 = new IsLessThanConstantConstraint(new Field("foobar"), 30, null);
        ruleInformation = new RuleInformation(new RuleDTO());
    }

    /**
     * Tests that the violate method with an AND constraint returns the correct OR constraint.
     * VIOLATE(AND(X, Y, Z)) reduces to
     *         OR(
     *            AND(VIOLATE(X), Y, Z),
     *            AND(X, VIOLATE(Y), Z),
     *            AND(X, Y, VIOLATE(Z)))
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
     * Tests that the violate method with an OR constraint returns the correct AND constraint.
     * VIOLATE(OR(X, Y, Z)) reduces to AND(VIOLATE(X), VIOLATE(Y), VIOLATE(Z))
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
    @Test(expected = UnviolatableConstraintException.class)
    public void violateRule_withUnsupportedConstraints_throwsRuntimeException() {
        //Arrange
        Constraint mockConstraint = Mockito.mock(Constraint.class);
        inputConstraints.add(mockConstraint);

        Rule inputRule = new Rule(ruleInformation, inputConstraints);

        //Act
        target.violateRule(inputRule);

        //Assert
        Assert.fail("The violate method with unsupported constraint type should have thrown an " +
            "UnviolatableConstraintException exception");
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

    /**
     * Asserts all constraints in two rules have the same type and all sub-constraints of these constraints have the
     * same type.
     * @param expectedRule The expected rule.
     * @param actualRule The actual rule
     */
    private void assertRuleTypeEquality(Rule expectedRule, Rule actualRule) {
        ArrayList<Constraint> expectedConstraints = new ArrayList<>(expectedRule.constraints);
        ArrayList<Constraint> outputConstraints = new ArrayList<>(actualRule.constraints);
        for (int i = 0; i < expectedConstraints.size(); i++) {
            assertConstraintTypeEquality(expectedConstraints.get(i), outputConstraints.get(i));
        }
    }

    /**
     * Asserts deep type equality for constraints that can contain other constraints.
     * For example asserts that an AND(OR(X,Y),OR(Z,W)) is not the same as AND(OR(X,Y),AND(Z,W))
     * but cannot distinguish between constraints A:(foo less than 10) and B:(bar less than 50)
     * @param expectedConstraint The expected constraint.
     * @param actualConstraint The constraint under test.
     */
    private void assertConstraintTypeEquality(Constraint expectedConstraint, Constraint actualConstraint) {
        Assert.assertEquals("Class types do not match for constraints. Expected: "
                + expectedConstraint.getClass() + " but was: " + actualConstraint.getClass(),
            expectedConstraint.getClass(),
            actualConstraint.getClass());
        if (expectedConstraint instanceof NotConstraint){
            assertConstraintTypeEquality(
                ((NotConstraint)expectedConstraint).negatedConstraint,
                ((NotConstraint)actualConstraint).negatedConstraint);
        }
        else if (expectedConstraint instanceof AndConstraint) {
            ArrayList<Constraint> expectedConstraints = new ArrayList<>(((AndConstraint) expectedConstraint).subConstraints);
            ArrayList<Constraint> actualConstraints = new ArrayList<>(((AndConstraint) actualConstraint).subConstraints);
            for (int i = 0; i < ((AndConstraint) expectedConstraint).subConstraints.size(); i++) {
                assertConstraintTypeEquality(
                    expectedConstraints.get(i),
                    actualConstraints.get(i));
            }
        }
        else if (expectedConstraint instanceof OrConstraint) {
            ArrayList<Constraint> expectedConstraints = new ArrayList<>(((OrConstraint) expectedConstraint).subConstraints);
            ArrayList<Constraint> actualConstraints = new ArrayList<>(((OrConstraint) actualConstraint).subConstraints);
            for (int i = 0; i < ((OrConstraint) expectedConstraint).subConstraints.size(); i++) {
                assertConstraintTypeEquality(
                    expectedConstraints.get(i),
                    actualConstraints.get(i));
            }
        }
        else if (expectedConstraint instanceof ConditionalConstraint) {
            ConditionalConstraint expectedConditionalConstraint = (ConditionalConstraint) expectedConstraint;
            ConditionalConstraint actualConditionalConstraint = (ConditionalConstraint) actualConstraint;
            assertConstraintTypeEquality(
                expectedConditionalConstraint.condition,
                actualConditionalConstraint.condition);
            assertConstraintTypeEquality(
                expectedConditionalConstraint.whenConditionIsFalse,
                actualConditionalConstraint.whenConditionIsFalse);
            assertConstraintTypeEquality(
                expectedConditionalConstraint.whenConditionIsTrue,
                actualConditionalConstraint.whenConditionIsTrue);
        }
    }
}
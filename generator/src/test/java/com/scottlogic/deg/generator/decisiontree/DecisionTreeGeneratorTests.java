package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.*;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.scottlogic.deg.generator.AssertingMatcher.matchesAssertions;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.*;

class DecisionTreeGeneratorTests {
    private final IConstraint aIsNull = new IsNullConstraint(new Field("A"));
    private final IConstraint bEquals10 = new IsEqualToConstantConstraint(new Field("B"), 10);
    private final IConstraint cIsNumeric = new IsOfTypeConstraint(new Field("C"), IsOfTypeConstraint.Types.Numeric);
    private final IConstraint dGreaterThan10 = new IsGreaterThanConstantConstraint(new Field("D"), 10);
    private final IConstraint eIsString = new IsOfTypeConstraint(new Field("E"), IsOfTypeConstraint.Types.String);

    private Rule inputRule = null;
    private DecisionTree actualOutput = null;

    @BeforeEach
    private void beforeEach() {
        this.actualOutput = null;
        this.inputRule = null;
    }

    private void givenRule(IConstraint... constraints) {
        this.inputRule = new Rule("", Arrays.asList(constraints));
    }

    private DecisionTree getActualOutput() {
        if (this.actualOutput == null) {
            DecisionTreeGenerator testObject = new DecisionTreeGenerator();

            this.actualOutput = testObject.generateTreeFor(this.inputRule);
        }

        return this.actualOutput;
    }

    private void treeRootShouldMatch(ConstraintNode expected) {
        ConstraintNode actual = this.getActualOutput().getRootNode();

        Assert.assertThat(actual, isEquivalentTo(expected));
    }

    private Matcher<IConstraint> sameNegation(IConstraint expected) {
        return matchesAssertions(
            "Both constraints negate the same constraint instance",
            (actual, subAssert) -> {
                subAssert.assertThat(actual, instanceOf(NotConstraint.class));
                subAssert.assertThat(expected, instanceOf(NotConstraint.class));

                // this method doesn't stop when an assertion fails, so we need to do this check to stop invalid casts below
                if (!(actual instanceof NotConstraint && expected instanceof NotConstraint))
                    return;

                subAssert.assertThat(
                    ((NotConstraint) actual).negatedConstraint,
                    sameInstance(((NotConstraint)expected).negatedConstraint));
            });
    }

    private Matcher<ConstraintNode> isEquivalentTo(ConstraintNode expected) {
        return matchesAssertions(
            "matching option node",
            (actual, asserter) -> {
                asserter.assertThat( // Should have same number of atomic constraints
                    actual.getAtomicConstraints().size(),
                    equalTo(expected.getAtomicConstraints().size()));

                asserter.assertThat( // Should have same atomic constraints
                    actual.getAtomicConstraints(),
                    containsInAnyOrder(
                        expected.getAtomicConstraints().stream()
                            .map(c -> anyOf(sameInstance(c), sameNegation(c)))
                            .collect(Collectors.toList())));

                asserter.assertThat( // Should have same number of decisions
                    actual.getDecisions().size(),
                    equalTo(expected.getDecisions().size()));

                asserter.assertThat( // Should have same decisions
                    actual.getDecisions(),
                    containsInAnyOrder(
                        expected.getDecisions().stream()
                            .map(this::isEquivalentTo)
                            .collect(Collectors.toList())));
            });
    }

    private Matcher<DecisionNode> isEquivalentTo(DecisionNode expected) {
        return matchesAssertions(
            "matching decision node",
            (actual, asserter) -> {
                asserter.assertThat( // Should have same number of options
                    actual.getOptions().size(),
                    equalTo(expected.getOptions().size()));

                asserter.assertThat( // Should have same options
                    actual.getOptions(),
                    containsInAnyOrder(
                        expected.getOptions().stream()
                            .map(this::isEquivalentTo)
                            .collect(Collectors.toList())));
            });
    }



    // checks A, B, C
    @Test
    void withJustAtomicConstraints() {
        givenRule(aIsNull, bEquals10, dGreaterThan10);

        treeRootShouldMatch(
            new ConstraintNode(
                aIsNull,
                bEquals10,
                dGreaterThan10));
    }

    // checks ((A AND B) AND C)
    @Test
    void withAtomicConstraintsNestedInAnd() {
        givenRule(
            new AndConstraint(
                new AndConstraint(aIsNull, bEquals10),
                dGreaterThan10));

        treeRootShouldMatch(
            new ConstraintNode(
                aIsNull,
                bEquals10,
                dGreaterThan10));
    }

    // checks (A OR B), (C OR D)
    @Test
    void withMultipleOrConstraints() {
        givenRule(
            new OrConstraint(aIsNull, bEquals10),
            new OrConstraint(dGreaterThan10, cIsNumeric));

        treeRootShouldMatch(
            new ConstraintNode(
                new DecisionNode(
                    new ConstraintNode(aIsNull),
                    new ConstraintNode(bEquals10)),
                new DecisionNode(
                    new ConstraintNode(dGreaterThan10),
                    new ConstraintNode(cIsNumeric))));
    }

    // checks (A OR B) AND (C OR D)
    @Test
    void withOrConstraintsWrappedInAnd() {
        givenRule(
            new AndConstraint(
                new OrConstraint(aIsNull, bEquals10),
                new OrConstraint(dGreaterThan10, cIsNumeric)));

        treeRootShouldMatch(
            new ConstraintNode(
                new DecisionNode(
                    new ConstraintNode(aIsNull),
                    new ConstraintNode(bEquals10)),
                new DecisionNode(
                    new ConstraintNode(dGreaterThan10),
                    new ConstraintNode(cIsNumeric))));
    }

    // Checks (A OR (B AND C)) AND (D OR E)
    @Test
    void withComplexMixedNestingOfAndOr() {
        givenRule(
            new AndConstraint(
                new OrConstraint(
                    aIsNull,
                    new AndConstraint(
                        bEquals10,
                        cIsNumeric)),
                new OrConstraint(dGreaterThan10, eIsString)));

        treeRootShouldMatch(
            new ConstraintNode(
                new DecisionNode(
                    new ConstraintNode(aIsNull),
                    new ConstraintNode(bEquals10, cIsNumeric)),
                new DecisionNode(
                    new ConstraintNode(dGreaterThan10),
                    new ConstraintNode(eIsString))));
    }

    // Checks IF (A) THEN B ELSE C
    @Test
    void withIfConstraint() {
        givenRule(
            new ConditionalConstraint(
                aIsNull,
                cIsNumeric,
                eIsString));

        treeRootShouldMatch(
            new ConstraintNode(
                new DecisionNode(
                    new ConstraintNode(aIsNull, cIsNumeric),
                    new ConstraintNode(aIsNull.isFalse(), eIsString))));
    }

    // Checks IF (A OR B) THEN C
    @Test
    void withIfConstraintOverOrConstraint() {
        givenRule(
            new ConditionalConstraint(
                aIsNull.or(bEquals10),
                cIsNumeric));

        treeRootShouldMatch(
            new ConstraintNode(
                Collections.emptyList(),
                Arrays.asList(
                    new DecisionNode(
                        /* OPTION 1: AND(C, OR(A, B))  */
                        new ConstraintNode(
                            Arrays.asList(cIsNumeric),
                            Collections.singleton(
                                new DecisionNode(
                                    new ConstraintNode(aIsNull),
                                    new ConstraintNode(bEquals10)))),
                        /* OPTION 2: AND(¬A, ¬B)  */
                        new ConstraintNode(
                            new NotConstraint(aIsNull),
                            new NotConstraint(bEquals10))))));
    }

    // NOT (IF A THEN B ELSE C) - edge case
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedConditionalConstraintIsPresent() {
        givenRule(
            new NotConstraint(
                new ConditionalConstraint(
                    aIsNull,
                    cIsNumeric,
                    eIsString)));

        treeRootShouldMatch(
            new ConstraintNode(
                new DecisionNode(
                    new ConstraintNode(aIsNull, cIsNumeric.isFalse()),
                    new ConstraintNode(aIsNull.isFalse(), eIsString.isFalse()))));
    }

    // NOT (IF A THEN B) - other edge case
    @Test
    void withNegatedIf() {
        // ¬(A -> B)
        // is equivalent to
        // A ^ ¬B

        givenRule(
            new NotConstraint(
                new ConditionalConstraint(aIsNull, bEquals10)));

        treeRootShouldMatch(
            new ConstraintNode(
                aIsNull,
                bEquals10.isFalse()));
    }

    // NOT (NOT A)
    @Test
    void withDoubleNegatedAtomicConstraint() {
        givenRule(
            new NotConstraint(
                new NotConstraint(aIsNull)));

        treeRootShouldMatch(
            new ConstraintNode(aIsNull));
    }

    // NOT (A AND B)
    @Test
    void withNegatedAnd() {
        givenRule(
            new NotConstraint(
                new AndConstraint(
                    aIsNull,
                    bEquals10)));

        treeRootShouldMatch(
            new ConstraintNode(
                new DecisionNode(
                    new ConstraintNode(aIsNull.isFalse()),
                    new ConstraintNode(bEquals10.isFalse()))));
    }

    // (A OR B) OR C
    @Test
    void withNestedOrs() {
        givenRule(
            new OrConstraint(
                new OrConstraint(aIsNull, bEquals10),
                cIsNumeric));

        treeRootShouldMatch(
            new ConstraintNode(
                Collections.emptyList(),
                Arrays.asList(
                    new DecisionNode(
                        new ConstraintNode(aIsNull),
                        new ConstraintNode(bEquals10),
                        new ConstraintNode(cIsNumeric)))));
    }

    @Test
    void whenViolatingPositiveAtomic() {
        givenRule(
            new ViolateConstraint(
                aIsNull));

        treeRootShouldMatch(
            new ConstraintNode(
                new NotConstraint(
                    aIsNull)));
    }

    @Test
    void whenViolatingNegativeAtomic() {
        givenRule(
            new ViolateConstraint(
                new NotConstraint(
                    aIsNull)));

        treeRootShouldMatch(
            new ConstraintNode(
                aIsNull));
    }

    @Test
    void whenViolatingOrConstraint() {
        givenRule(
            new ViolateConstraint(
                new OrConstraint(aIsNull, cIsNumeric)));

        treeRootShouldMatch(
            new ConstraintNode(
                new NotConstraint(aIsNull),
                new NotConstraint(cIsNumeric)));
    }

    @Test
    void whenViolatingAndConstraint() {
        givenRule(
            new ViolateConstraint(
                new AndConstraint(aIsNull, cIsNumeric, eIsString)));

        treeRootShouldMatch(
            new ConstraintNode(
                new DecisionNode(
                    new ConstraintNode(
                        new NotConstraint(aIsNull),
                        cIsNumeric,
                        eIsString),
                    new ConstraintNode(
                        aIsNull,
                        new NotConstraint(cIsNumeric),
                        eIsString),
                    new ConstraintNode(
                        aIsNull,
                        cIsNumeric,
                        new NotConstraint(eIsString)))));
    }

    @Test
    void whenViolatingIfConstraint() {
        givenRule(
            new ViolateConstraint(
                new ConditionalConstraint(
                    aIsNull,
                    bEquals10,
                    eIsString)));

        treeRootShouldMatch(
            new ConstraintNode(
                new DecisionNode(
                    new ConstraintNode(
                        aIsNull,
                        bEquals10.isFalse()),
                    new ConstraintNode(
                        aIsNull.isFalse(),
                        eIsString.isFalse()))));
    }
}

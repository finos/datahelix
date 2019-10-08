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

package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.IsGreaterThanConstantConstraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.MatchesRegexConstraint;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.NegatedGrammaticalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.generator.decisiontree.testutils.*;
import com.scottlogic.deg.generator.profile.RuleInformation;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.fieldspecs.whitelist.WeightedElement;
import com.scottlogic.deg.generator.utils.SetUtils;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

class DecisionTreeFactoryTests {
    private final Field fieldA = createField("A");
    private final Field fieldB = createField("B");
    private final Field fieldC = createField("C");

    private final List<Rule> rules = new ArrayList<>();
    private DecisionTree actualOutput = null;

    @BeforeEach
    void beforeEach() {
        rules.clear();
        actualOutput = null;
    }

    private void givenRule(Constraint... constraints) {
        this.rules.add(new Rule(rule(""), Arrays.asList(constraints)));
    }

    private DecisionTree getActualOutput() {
        if (this.actualOutput == null) {
            Profile testInput = new Profile(
                new ProfileFields(
                    Arrays.asList(this.fieldA, this.fieldB, this.fieldC)),
                this.rules);

            DecisionTreeFactory testObject = new DecisionTreeFactory();

            this.actualOutput = testObject.analyse(testInput);
        }

        return this.actualOutput;
    }

    private ConstraintNode getResultingRootOption() {
        return this.getActualOutput().getRootNode();
    }

    @Test
    void shouldReturnAnalysedProfileWithNoAnalysedRules_IfProfileHasNoRules() {
        Profile testInput = new Profile(new ArrayList<>(), new ArrayList<>());
        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree testOutput = testObject.analyse(testInput);

        Assert.assertThat(testOutput, not(is(nullValue())));
        Assert.assertThat(testOutput.getFields().size(), is(0));
        Assert.assertThat(testOutput.getRootNode(), not(is(nullValue())));
        Assert.assertThat(testOutput.getRootNode().getAtomicConstraints(), is(empty()));
        Assert.assertThat(testOutput.getRootNode().getDecisions(), is(empty()));
    }

    @Test
    void shouldReturnAnalysedProfileWithCorrectFields() {
        List<Field> inputFieldList = Arrays.asList(createField("one"), createField("two"), createField("three"));
        Profile testInput = new Profile(inputFieldList, new ArrayList<>());
        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree testOutput = testObject.analyse(testInput);
        ProfileFields actualFields = testOutput.getFields();

        ProfileFields expected = new ProfileFields(inputFieldList);
        assertThat(actualFields, sameBeanAs(expected));
    }

    @Test
    void shouldReturnAnalysedRuleWithNoDecisions_IfProfileContainsOnlyAtomicConstraints() {
        List<Field> inputFieldList = Arrays.asList(createField("one"), createField("two"), createField("three"));
        IsInSetConstraint constraint0 = new IsInSetConstraint(
            inputFieldList.get(0),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), BigDecimal.valueOf(0));
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        Rule testRule = new Rule(rule("test"), Arrays.asList(constraint0, constraint1, constraint2));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));

        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root has non-null list of decisions",
            outputRule.getRootNode().getDecisions(), Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root has empty list of decisions",
            outputRule.getRootNode().getDecisions().size(), Is.is(0));
    }

    @Test
    void shouldReturnAnalysedRuleWithAllConstraintsInAtomicConstraintsCollection_IfProfileContainsOnlyAtomicConstraints() {
        List<Field> inputFieldList = Arrays.asList(createField("one"), createField("two"), createField("three"));
        IsInSetConstraint constraint0 = new IsInSetConstraint(
            inputFieldList.get(0),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), BigDecimal.valueOf(0));
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        List<Constraint> inputConstraints = Arrays.asList(constraint0, constraint1, constraint2);
        Rule testRule = new Rule(rule("test"), inputConstraints);
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("Decision tree root atomic constraint list is same size as original constraint list",
            outputRule.getRootNode().getAtomicConstraints().size(), Is.is(inputConstraints.size()));
        for (Constraint constraint : inputConstraints) {
            AtomicConstraint atomicConstraint = (AtomicConstraint) constraint;

            Assert.assertThat("Each input constraint is in the decision tree root node atomic constraint list",
                outputRule.getRootNode().getAtomicConstraints(), hasItem(atomicConstraint));
        }
    }

    @Test
    void shouldReturnAnalysedRuleWithNoDecisions_IfProfileContainsOnlyAtomicConstraintsAndAndConstraints() {
        List<Field> inputFieldList = Arrays.asList(createField("one"), createField("two"), createField("three"));
        IsInSetConstraint constraint0 = new IsInSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), BigDecimal.valueOf(0));
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraint0, constraint1));
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        Rule testRule = new Rule(rule("test"), Arrays.asList(andConstraint0, constraint2));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root has non-null list of decisions",
            outputRule.getRootNode().getDecisions(), Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root has empty list of decisions",
            outputRule.getRootNode().getDecisions().size(), Is.is(0));
    }

    @Test
    void shouldReturnAnalysedRuleWithAllAtomicConstraintsInAtomicConstraintsCollection_IfProfileContainsOnlyAtomicConstraintsAndAndConstraints() {
        List<Field> inputFieldList = Arrays.asList(createField("one"), createField("two"), createField("three"));
        IsInSetConstraint constraint0 = new IsInSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), BigDecimal.valueOf(0));
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraint0, constraint1));
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        Rule testRule = new Rule(rule("test"), Arrays.asList(andConstraint0, constraint2));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));

        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root contains correct number of atomic constraints",
            outputRule.getRootNode().getAtomicConstraints().size(), Is.is(3));
        Assert.assertThat("Decision tree root atomic constraints list contains constraint 0",
            outputRule.getRootNode().getAtomicConstraints().contains(constraint0), Is.is(true));
        Assert.assertThat("Decision tree root atomic constraints list contains constraint 1",
            outputRule.getRootNode().getAtomicConstraints().contains(constraint1), Is.is(true));
        Assert.assertThat("Decision tree root atomic constraints list contains constraint 2",
            outputRule.getRootNode().getAtomicConstraints().contains(constraint2), Is.is(true));
    }

    @Test
    void shouldReturnAnalysedRuleWithDecisionForEachOrConstraint() {
        List<Field> inputFieldList = Arrays.asList(createField("one"), createField("two"), createField("three"));
        IsInSetConstraint constraint0 = new IsInSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), BigDecimal.valueOf(0));
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraint0, constraint1));
        IsInSetConstraint constraint2 = new IsInSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("steam", 1.0F))));
        IsInSetConstraint constraint3 = new IsInSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("diesel", 1.0F))));
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraint2, constraint3));
        Rule testRule = new Rule(rule("test"), Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root contains correct number of decisions",
            outputRule.getRootNode().getDecisions().size(), Is.is(2));
    }

    // checks (A OR B) AND (C OR D)
    @Test
    void shouldReturnAnalysedRuleWithNoAtomicConstraints_IfAllAtomicConstraintsInProfileAreChildrenOfOrConstraints() {
        List<Field> inputFieldList = Arrays.asList(createField("one"), createField("two"), createField("three"));
        IsInSetConstraint constraintA = new IsInSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), BigDecimal.valueOf(0));
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
        IsInSetConstraint constraintC = new IsInSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("steam", 1.0F))));
        IsInSetConstraint constraintD = new IsInSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("diesel", 1.0F))));
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintC, constraintD));
        Rule testRule = new Rule(rule("test"), Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root contains no atomic constraints",
            outputRule.getRootNode().getAtomicConstraints().size(), Is.is(0));
    }

    // checks (A OR B) AND (C OR D)
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfAllAtomicConstraintsInProfileAreChildrenOfOrConstraints() {
        List<Field> inputFieldList = Arrays.asList(createField("one"), createField("two"), createField("three"));
        IsInSetConstraint constraintA = new IsInSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), BigDecimal.valueOf(0));
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
        IsInSetConstraint constraintC = new IsInSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("steam", 1.0F))));
        IsInSetConstraint constraintD = new IsInSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("diesel", 1.0F))));
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintC, constraintD));
        Rule testRule = new Rule(rule("test"), Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertTrue(isEquivalentTo(
            new ConstraintNodeBuilder().addAtomicConstraints(Collections.emptySet()).setDecisions(SetUtils.setOf(
                new DecisionNode(
                    new ConstraintNodeBuilder().addAtomicConstraints(constraintA).build(),
                    new ConstraintNodeBuilder().addAtomicConstraints(constraintB).build()
                ),
                new DecisionNode(
                    new ConstraintNodeBuilder().addAtomicConstraints(constraintC).build(),
                    new ConstraintNodeBuilder().addAtomicConstraints(constraintD).build()
                )
            )).build(),
            outputRule.getRootNode())
        );
    }

    // Checks (A OR (B AND C)) AND (D OR E)
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfAllAtomicConstraintsInProfileAreChildrenOfOrAndAndConstraints() {
        List<Field> inputFieldList = Arrays.asList(createField("one"), createField("two"), createField("three"));
        IsInSetConstraint constraintA = new IsInSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), BigDecimal.valueOf(0));
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(0), BigDecimal.valueOf(5));
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraintC, constraintB));
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, andConstraint0));
        IsInSetConstraint constraintD = new IsInSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("steam", 1.0F))));
        IsInSetConstraint constraintE = new IsInSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("diesel", 1.0F))));
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintD, constraintE));
        Rule testRule = new Rule(rule("test"), Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertTrue(isEquivalentTo(
            new ConstraintNodeBuilder().addAtomicConstraints(Collections.emptySet()).setDecisions(SetUtils.setOf(
                new DecisionNode(
                    new ConstraintNodeBuilder().addAtomicConstraints(constraintA).build(),
                    new ConstraintNodeBuilder().addAtomicConstraints(constraintC, constraintB).build()
                ),
                new DecisionNode(
                    new ConstraintNodeBuilder().addAtomicConstraints(constraintD).build(),
                    new ConstraintNodeBuilder().addAtomicConstraints(constraintE).build()
                )
            )).build(),
            outputRule.getRootNode())
        );
    }

    // Checks IF (A) THEN B ELSE C
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfConditionalConstraintIsPresent() {
        List<Field> inputFieldList = Arrays.asList(createField("one"), createField("two"), createField("three"));
        IsInSetConstraint constraintA = new IsInSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), BigDecimal.valueOf(10));
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), BigDecimal.valueOf(20));
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB, constraintC);
        Rule testRule = new Rule(rule("test"), Collections.singletonList(conditionalConstraint));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertTrue(isEquivalentTo(
            new ConstraintNodeBuilder().addAtomicConstraints(Collections.emptySet()).setDecisions(Collections.singleton(
                new DecisionNode(
                    new ConstraintNodeBuilder().addAtomicConstraints(SetUtils.setOf(
                        constraintA,
                        constraintB)).setDecisions(Collections.emptySet()).build(),
                    new ConstraintNodeBuilder().addAtomicConstraints(SetUtils.setOf(
                        constraintA.negate(),
                        constraintC
                    )).setDecisions(Collections.emptySet()).build()
                )
            )).build(),
            outputRule.getRootNode())
        );
    }

    // Checks IF (A OR B) THEN C
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfConditionalConstraintWithNestedOrIsPresent() {
        AtomicConstraint aEquals10 = new IsInSetConstraint(fieldA, new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        AtomicConstraint aGreaterThan10 = new IsGreaterThanConstantConstraint(fieldA, BigDecimal.valueOf(10));
        AtomicConstraint bGreaterThan20 = new IsGreaterThanConstantConstraint(fieldB, BigDecimal.valueOf(20));

        givenRule(
            new ConditionalConstraint(
                new OrConstraint(
                    aEquals10,
                    aGreaterThan10),
                bGreaterThan20));

        Assert.assertTrue(
            isEquivalentTo(
                getResultingRootOption(), new ConstraintNodeBuilder().addAtomicConstraints(Collections.emptySet()).setDecisions(Collections.singleton(
                    new DecisionNode(
                        /* OPTION 1: AND(C, OR(A, B))  */
                        new ConstraintNodeBuilder().addAtomicConstraints(Collections.singleton(bGreaterThan20)).setDecisions(Collections.singleton(
                            new DecisionNode(
                                new ConstraintNodeBuilder().addAtomicConstraints(aEquals10).build(),
                                new ConstraintNodeBuilder().addAtomicConstraints(aGreaterThan10).build()))).build(),
                        /* OPTION 2: AND(¬A, ¬B)  */
                        new ConstraintNodeBuilder().addAtomicConstraints(aEquals10.negate(), aGreaterThan10.negate()).build()
                    )
                )).build()
            )
        );
    }

    // NOT (IF A THEN B ELSE C) - edge case
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedConditionalConstraintIsPresent() {
        List<Field> inputFieldList = Arrays.asList(createField("one"), createField("two"), createField("three"));
        IsInSetConstraint constraintA = new IsInSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), BigDecimal.valueOf(20));
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), BigDecimal.valueOf(10));
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB, constraintC);
        Constraint notConstraint = conditionalConstraint.negate();
        Rule testRule = new Rule(rule("test"), Collections.singletonList(notConstraint));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertTrue(isEquivalentTo(
            new ConstraintNodeBuilder().addAtomicConstraints(Collections.emptySet()).setDecisions(Collections.singleton(
                new DecisionNode(
                    new ConstraintNodeBuilder().addAtomicConstraints(SetUtils.setOf(
                        constraintA,
                        constraintB.negate()
                    )).setDecisions(Collections.emptySet()).build(),
                    new ConstraintNodeBuilder().addAtomicConstraints(SetUtils.setOf(
                        constraintA.negate(),
                        constraintC.negate()
                    )).setDecisions(Collections.emptySet()).build()
                )
            )).build(),
            outputRule.getRootNode())
        );
    }

    // NOT (IF A THEN B) - other edge case
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedConditionalConstraintWithoutElseClauseIsPresent() {
        // ¬(A -> B)
        // is equivalent to
        // A ^ ¬B

        AtomicConstraint aEqualTo10 = new IsInSetConstraint(fieldA, new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        AtomicConstraint bGreaterThan20 = new IsGreaterThanConstantConstraint(fieldB, BigDecimal.valueOf(20));

        Constraint inputRule = new ConditionalConstraint(aEqualTo10, bGreaterThan20).negate();

        ConstraintNode expectedOutput = new ConstraintNodeBuilder().addAtomicConstraints(aEqualTo10, bGreaterThan20.negate()).build();

        givenRule(inputRule);

        Assert.assertTrue(
            isEquivalentTo(
                getResultingRootOption(), expectedOutput
            )
        );
    }

    // NOT (NOT A)
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfDoubleNegationIsPresent() {
        List<Field> inputFieldList = Arrays.asList(createField("one"), createField("two"), createField("three"));
        IsInSetConstraint constraintA = new IsInSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        Constraint notConstraint0 = constraintA.negate();
        Constraint notConstraint1 = notConstraint0.negate();
        Rule testRule = new Rule(rule("test"), Collections.singletonList(notConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        // Result should just be A.
        Assert.assertThat("Decision tree root contains one atomic constraint",
            outputRule.getRootNode().getAtomicConstraints().size(), Is.is(1));
        Assert.assertThat("Decision tree root contains no decisions",
            outputRule.getRootNode().getDecisions().size(), Is.is(0));
        Assert.assertThat("Atomic constraint of decision tree root is constraint A",
            outputRule.getRootNode().getAtomicConstraints().contains(constraintA), Is.is(true));
    }

    // NOT (A AND B)
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedAndIsPresent() {
        List<Field> inputFieldList = Arrays.asList(createField("one"), createField("two"), createField("three"));
        IsInSetConstraint constraintA = new IsInSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), BigDecimal.valueOf(5));
        NegatedGrammaticalConstraint notConstraint = (NegatedGrammaticalConstraint) new AndConstraint(Arrays.asList(constraintA, constraintB)).negate();
        Rule testRule = new Rule(rule("test"), Collections.singletonList(notConstraint));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        // Result should be (NOT A) OR (NOT B)
        Assert.assertTrue(isEquivalentTo(
            new ConstraintNodeBuilder().addAtomicConstraints(Collections.emptySet()).setDecisions(Collections.singleton(
                new DecisionNode(
                    new ConstraintNodeBuilder().addAtomicConstraints(Collections.singleton(constraintA.negate())).setDecisions(Collections.emptySet()).build(),
                    new ConstraintNodeBuilder().addAtomicConstraints(Collections.singleton(constraintB.negate())).setDecisions(Collections.emptySet()).build()
                )
            )).build(),
            outputRule.getRootNode())
        );
    }

    // (A OR B) OR C
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNestedOrsArePresent() {
        AtomicConstraint constraintA = new IsInSetConstraint(fieldA, new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        AtomicConstraint constraintB = new IsGreaterThanConstantConstraint(fieldB, BigDecimal.valueOf(20));
        AtomicConstraint constraintC = new IsGreaterThanConstantConstraint(fieldB, BigDecimal.valueOf(10));

        givenRule(
            new OrConstraint(
                new OrConstraint(constraintA, constraintB),
                constraintC));

        Assert.assertTrue(
            isEquivalentTo(
                getResultingRootOption(),
                new ConstraintNodeBuilder().addAtomicConstraints(Collections.emptySet()).setDecisions(Collections.singleton(
                    new DecisionNode(
                        new ConstraintNodeBuilder().addAtomicConstraints(constraintA).build(),
                        new ConstraintNodeBuilder().addAtomicConstraints(constraintB).build(),
                        new ConstraintNodeBuilder().addAtomicConstraints(constraintC).build())
                )).build()
            )
        );
    }

    private boolean isEquivalentTo(ConstraintNode expected, ConstraintNode actual) {
        TreeComparisonContext context = new TreeComparisonContext();
        AnyOrderCollectionEqualityComparer defaultAnyOrderCollectionEqualityComparer = new AnyOrderCollectionEqualityComparer();
        ConstraintNodeComparer constraintNodeComparer = new ConstraintNodeComparer(
            context,
            defaultAnyOrderCollectionEqualityComparer,
            new DecisionComparer(),
            defaultAnyOrderCollectionEqualityComparer,
            new AnyOrderCollectionEqualityComparer(new DecisionComparer()));

        boolean match = constraintNodeComparer.equals(expected, actual);
        if (!match){
            new TreeComparisonReporter().reportMessages(context);
        }

        return match;
    }

    private static RuleInformation rule(String description){
        return new RuleInformation(description);
    }
}

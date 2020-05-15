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

package com.scottlogic.datahelix.generator.core.decisiontree;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.common.util.NumberUtils;
import com.scottlogic.datahelix.generator.common.whitelist.DistributedList;
import com.scottlogic.datahelix.generator.common.whitelist.WeightedElement;
import com.scottlogic.datahelix.generator.core.decisiontree.testutils.*;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.GreaterThanConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.InSetConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.MatchesRegexConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.grammatical.NegatedGrammaticalConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.datahelix.generator.common.SetUtils;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createField;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;

class DecisionTreeFactoryTests {
    private final Field fieldA = createField("A");
    private final Field fieldB = createField("B");
    private final Field fieldC = createField("C");

    private final List<Constraint> constraints = new ArrayList<>();
    private DecisionTree actualOutput = null;

    @BeforeEach
    void beforeEach() {
        constraints.clear();
        actualOutput = null;
    }

    private void givenConstraints(Constraint... constraints) {
        this.constraints.addAll(Arrays.stream(constraints).collect(Collectors.toList()));
    }

    private DecisionTree getActualOutput() {
        if (this.actualOutput == null) {
            Profile testInput = new Profile(
                new Fields(
                    Arrays.asList(this.fieldA, this.fieldB, this.fieldC)),
                this.constraints,
                Collections.emptyList());

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
        Profile testInput = new Profile(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
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
        Profile testInput = new Profile(inputFieldList, new ArrayList<>(), new ArrayList<>());
        DecisionTreeFactory testObject = new DecisionTreeFactory();

        DecisionTree testOutput = testObject.analyse(testInput);
        Fields actualFields = testOutput.getFields();

        Fields expected = new Fields(inputFieldList);
        assertThat(actualFields, sameBeanAs(expected));
    }

    @Test
    void shouldReturnAnalysedRuleWithNoDecisions_IfProfileContainsOnlyAtomicConstraints() {
        List<Field> inputFieldList = Arrays.asList(createField("one"), createField("two"), createField("three"));
        InSetConstraint constraint0 = new InSetConstraint(
            inputFieldList.get(0),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        GreaterThanConstraint constraint1 = new GreaterThanConstraint(inputFieldList.get(0), NumberUtils.coerceToBigDecimal(0));
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        Profile testInput = new Profile(inputFieldList, Arrays.asList(constraint0, constraint1, constraint2), new ArrayList<>());

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
        InSetConstraint constraint0 = new InSetConstraint(
            inputFieldList.get(0),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        GreaterThanConstraint constraint1 = new GreaterThanConstraint(inputFieldList.get(0), NumberUtils.coerceToBigDecimal(0));
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        List<Constraint> inputConstraints = Arrays.asList(constraint0, constraint1, constraint2);
        Profile testInput = new Profile(inputFieldList, inputConstraints, new ArrayList<>());
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
        InSetConstraint constraint0 = new InSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        GreaterThanConstraint constraint1 = new GreaterThanConstraint(inputFieldList.get(0), NumberUtils.coerceToBigDecimal(0));
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraint0, constraint1));
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        Profile testInput = new Profile(inputFieldList, Arrays.asList(andConstraint0, constraint2), new ArrayList<>());
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
        InSetConstraint constraint0 = new InSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        GreaterThanConstraint constraint1 = new GreaterThanConstraint(inputFieldList.get(0), NumberUtils.coerceToBigDecimal(0));
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraint0, constraint1));
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        Profile testInput = new Profile(inputFieldList, Arrays.asList(andConstraint0, constraint2), new ArrayList<>());

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
        InSetConstraint constraint0 = new InSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        GreaterThanConstraint constraint1 = new GreaterThanConstraint(inputFieldList.get(0), NumberUtils.coerceToBigDecimal(0));
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraint0, constraint1));
        InSetConstraint constraint2 = new InSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("steam", 1.0F))));
        InSetConstraint constraint3 = new InSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("diesel", 1.0F))));
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraint2, constraint3));
        Profile testInput = new Profile(inputFieldList, Arrays.asList(orConstraint0, orConstraint1), new ArrayList<>());
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
        InSetConstraint constraintA = new InSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        GreaterThanConstraint constraintB = new GreaterThanConstraint(inputFieldList.get(0), NumberUtils.coerceToBigDecimal(0));
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
        InSetConstraint constraintC = new InSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("steam", 1.0F))));
        InSetConstraint constraintD = new InSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("diesel", 1.0F))));
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintC, constraintD));
        Profile testInput = new Profile(inputFieldList, Arrays.asList(orConstraint0, orConstraint1), new ArrayList<>());
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
        InSetConstraint constraintA = new InSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        GreaterThanConstraint constraintB = new GreaterThanConstraint(inputFieldList.get(0), NumberUtils.coerceToBigDecimal(0));
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
        InSetConstraint constraintC = new InSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("steam", 1.0F))));
        InSetConstraint constraintD = new InSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("diesel", 1.0F))));
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintC, constraintD));
        Profile testInput = new Profile(inputFieldList, Arrays.asList(orConstraint0, orConstraint1), new ArrayList<>());
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
        InSetConstraint constraintA = new InSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        GreaterThanConstraint constraintB = new GreaterThanConstraint(inputFieldList.get(0), NumberUtils.coerceToBigDecimal(0));
        GreaterThanConstraint constraintC = new GreaterThanConstraint(inputFieldList.get(0), NumberUtils.coerceToBigDecimal(5));
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraintC, constraintB));
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, andConstraint0));
        InSetConstraint constraintD = new InSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("steam", 1.0F))));
        InSetConstraint constraintE = new InSetConstraint(
            inputFieldList.get(1),
            new DistributedList<>(Collections.singletonList(new WeightedElement<>("diesel", 1.0F))));
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintD, constraintE));
        Profile testInput = new Profile(inputFieldList, Arrays.asList(orConstraint0, orConstraint1), new ArrayList<>());
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
        InSetConstraint constraintA = new InSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        GreaterThanConstraint constraintB = new GreaterThanConstraint(inputFieldList.get(1), NumberUtils.coerceToBigDecimal(10));
        GreaterThanConstraint constraintC = new GreaterThanConstraint(inputFieldList.get(1), NumberUtils.coerceToBigDecimal(20));
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB, constraintC);
        Profile testInput = new Profile(inputFieldList,  Collections.singletonList(conditionalConstraint), new ArrayList<>());
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
        AtomicConstraint aEquals10 = new InSetConstraint(fieldA, new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        AtomicConstraint aGreaterThan10 = new GreaterThanConstraint(fieldA, NumberUtils.coerceToBigDecimal(10));
        AtomicConstraint bGreaterThan20 = new GreaterThanConstraint(fieldB, NumberUtils.coerceToBigDecimal(20));

        givenConstraints(
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
        InSetConstraint constraintA = new InSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        GreaterThanConstraint constraintB = new GreaterThanConstraint(inputFieldList.get(1), NumberUtils.coerceToBigDecimal(20));
        GreaterThanConstraint constraintC = new GreaterThanConstraint(inputFieldList.get(1), NumberUtils.coerceToBigDecimal(10));
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB, constraintC);
        Constraint notConstraint = conditionalConstraint.negate();
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(notConstraint), new ArrayList<>());
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

        AtomicConstraint aEqualTo10 = new InSetConstraint(fieldA, new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        AtomicConstraint bGreaterThan20 = new GreaterThanConstraint(fieldB, NumberUtils.coerceToBigDecimal(20));

        Constraint inputRule = new ConditionalConstraint(aEqualTo10, bGreaterThan20).negate();

        ConstraintNode expectedOutput = new ConstraintNodeBuilder().addAtomicConstraints(aEqualTo10, bGreaterThan20.negate()).build();

        givenConstraints(inputRule);

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
        InSetConstraint constraintA = new InSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        Constraint notConstraint0 = constraintA.negate();
        Constraint notConstraint1 = notConstraint0.negate();
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(notConstraint1), new ArrayList<>());
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
        InSetConstraint constraintA = new InSetConstraint(inputFieldList.get(0), new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        GreaterThanConstraint constraintB = new GreaterThanConstraint(inputFieldList.get(1), NumberUtils.coerceToBigDecimal(5));
        NegatedGrammaticalConstraint notConstraint = (NegatedGrammaticalConstraint) new AndConstraint(Arrays.asList(constraintA, constraintB)).negate();
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(notConstraint), new ArrayList<>());
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
        AtomicConstraint constraintA = new InSetConstraint(fieldA, new DistributedList<>(Collections.singletonList(new WeightedElement<>(10, 1.0F))));
        AtomicConstraint constraintB = new GreaterThanConstraint(fieldB, NumberUtils.coerceToBigDecimal(20));
        AtomicConstraint constraintC = new GreaterThanConstraint(fieldB, NumberUtils.coerceToBigDecimal(10));

        givenConstraints(
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
}

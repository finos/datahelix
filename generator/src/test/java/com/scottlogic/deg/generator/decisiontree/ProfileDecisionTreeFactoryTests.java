package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.constraint.atomic.AtomicConstraint;
import com.scottlogic.deg.common.constraint.atomic.IsGreaterThanConstantConstraint;
import com.scottlogic.deg.common.constraint.atomic.IsInSetConstraint;
import com.scottlogic.deg.common.constraint.atomic.MatchesRegexConstraint;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.constraint.Constraint;
import com.scottlogic.deg.common.constraint.grammatical.AndConstraint;
import com.scottlogic.deg.common.constraint.grammatical.ConditionalConstraint;
import com.scottlogic.deg.common.constraint.grammatical.NegatedGrammaticalConstraint;
import com.scottlogic.deg.common.constraint.grammatical.OrConstraint;
import com.scottlogic.deg.generator.decisiontree.testutils.*;
import com.scottlogic.deg.common.profile.RuleInformation;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Pattern;

import static com.scottlogic.deg.generator.AssertUtils.pairwiseAssert;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;

class ProfileDecisionTreeFactoryTests {
    private final Field fieldA = new Field("A");
    private final Field fieldB = new Field("B");
    private final Field fieldC = new Field("C");

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

            ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

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
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTree testOutput = testObject.analyse(testInput);

        Assert.assertThat(testOutput, not(is(nullValue())));
        Assert.assertThat(testOutput.getFields().size(), is(0));
        Assert.assertThat(testOutput.getRootNode(), not(is(nullValue())));
        Assert.assertThat(testOutput.getRootNode().getAtomicConstraints(), is(empty()));
        Assert.assertThat(testOutput.getRootNode().getDecisions(), is(empty()));
    }

    @Test
    void shouldReturnAnalysedProfileWithCorrectFields() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        Profile testInput = new Profile(inputFieldList, new ArrayList<>());
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTree testOutput = testObject.analyse(testInput);
        ProfileFields actualFields = testOutput.getFields();

        pairwiseAssert(
            actualFields,
            inputFieldList,
            (actual, expected) -> Assert.assertThat(actual, sameInstance(expected)));
    }

    @Test
    void shouldReturnAnalysedRuleWithNoDecisions_IfProfileContainsOnlyAtomicConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsInSetConstraint constraint0 = new IsInSetConstraint(inputFieldList.get(0), Collections.singleton(10), rules());
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rules());
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"), rules());
        Rule testRule = new Rule(rule("test"), Arrays.asList(constraint0, constraint1, constraint2));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));

        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root has non-null list of decisions",
            outputRule.getRootNode().getDecisions(), Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root has empty list of decisions",
            outputRule.getRootNode().getDecisions().size(), Is.is(0));
    }

    @Test
    void shouldReturnAnalysedRuleWithAllConstraintsInAtomicConstraintsCollection_IfProfileContainsOnlyAtomicConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsInSetConstraint constraint0 = new IsInSetConstraint(inputFieldList.get(0), Collections.singleton(10), rules());
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rules());
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"), rules());
        List<Constraint> inputConstraints = Arrays.asList(constraint0, constraint1, constraint2);
        Rule testRule = new Rule(rule("test"), inputConstraints);
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

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
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsInSetConstraint constraint0 = new IsInSetConstraint(inputFieldList.get(0), Collections.singleton(10), rules());
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rules());
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraint0, constraint1));
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"), rules());
        Rule testRule = new Rule(rule("test"), Arrays.asList(andConstraint0, constraint2));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root has non-null list of decisions",
            outputRule.getRootNode().getDecisions(), Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root has empty list of decisions",
            outputRule.getRootNode().getDecisions().size(), Is.is(0));
    }

    @Test
    void shouldReturnAnalysedRuleWithAllAtomicConstraintsInAtomicConstraintsCollection_IfProfileContainsOnlyAtomicConstraintsAndAndConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsInSetConstraint constraint0 = new IsInSetConstraint(inputFieldList.get(0), Collections.singleton(10), rules());
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rules());
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraint0, constraint1));
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"), rules());
        Rule testRule = new Rule(rule("test"), Arrays.asList(andConstraint0, constraint2));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));

        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

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
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsInSetConstraint constraint0 = new IsInSetConstraint(inputFieldList.get(0), Collections.singleton(10), rules());
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rules());
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraint0, constraint1));
        IsInSetConstraint constraint2 = new IsInSetConstraint(inputFieldList.get(1), Collections.singleton("steam"), rules());
        IsInSetConstraint constraint3 = new IsInSetConstraint(inputFieldList.get(1), Collections.singleton("diesel"), rules());
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraint2, constraint3));
        Rule testRule = new Rule(rule("test"), Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root contains correct number of decisions",
            outputRule.getRootNode().getDecisions().size(), Is.is(2));
    }

    // checks (A OR B) AND (C OR D)
    @Test
    void shouldReturnAnalysedRuleWithNoAtomicConstraints_IfAllAtomicConstraintsInProfileAreChildrenOfOrConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsInSetConstraint constraintA = new IsInSetConstraint(inputFieldList.get(0), Collections.singleton(10), rules());
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rules());
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
        IsInSetConstraint constraintC = new IsInSetConstraint(inputFieldList.get(1), Collections.singleton("steam"), rules());
        IsInSetConstraint constraintD = new IsInSetConstraint(inputFieldList.get(1), Collections.singleton("diesel"), rules());
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintC, constraintD));
        Rule testRule = new Rule(rule("test"), Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root contains no atomic constraints",
            outputRule.getRootNode().getAtomicConstraints().size(), Is.is(0));
    }

    // checks (A OR B) AND (C OR D)
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfAllAtomicConstraintsInProfileAreChildrenOfOrConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsInSetConstraint constraintA = new IsInSetConstraint(inputFieldList.get(0), Collections.singleton(10), rules());
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rules());
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
        IsInSetConstraint constraintC = new IsInSetConstraint(inputFieldList.get(1), Collections.singleton("steam"), rules());
        IsInSetConstraint constraintD = new IsInSetConstraint(inputFieldList.get(1), Collections.singleton("diesel"), rules());
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintC, constraintD));
        Rule testRule = new Rule(rule("test"), Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertTrue(isEquivalentTo(
            new TreeConstraintNode(
                Collections.emptySet(),
                Arrays.asList(
                    new TreeDecisionNode(
                        new TreeConstraintNode(constraintA),
                        new TreeConstraintNode(constraintB)
                    ),
                    new TreeDecisionNode(
                        new TreeConstraintNode(constraintC),
                        new TreeConstraintNode(constraintD)
                    )
                )
            ),
            outputRule.getRootNode())
        );
    }

    // Checks (A OR (B AND C)) AND (D OR E)
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfAllAtomicConstraintsInProfileAreChildrenOfOrAndAndConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsInSetConstraint constraintA = new IsInSetConstraint(inputFieldList.get(0), Collections.singleton(10), rules());
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rules());
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 5, rules());
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraintC, constraintB));
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, andConstraint0));
        IsInSetConstraint constraintD = new IsInSetConstraint(inputFieldList.get(1), Collections.singleton("steam"), rules());
        IsInSetConstraint constraintE = new IsInSetConstraint(inputFieldList.get(1), Collections.singleton("diesel"), rules());
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintD, constraintE));
        Rule testRule = new Rule(rule("test"), Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertTrue(isEquivalentTo(
            new TreeConstraintNode(
                Collections.emptySet(),
                Arrays.asList(
                    new TreeDecisionNode(
                        new TreeConstraintNode(
                            constraintA
                        ),
                        new TreeConstraintNode(
                            constraintC,
                            constraintB
                        )
                    ),
                    new TreeDecisionNode(
                        new TreeConstraintNode(constraintD),
                        new TreeConstraintNode(constraintE)
                    )
                )
            ),
            outputRule.getRootNode())
        );
    }

    // Checks IF (A) THEN B ELSE C
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfConditionalConstraintIsPresent() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsInSetConstraint constraintA = new IsInSetConstraint(inputFieldList.get(0), Collections.singleton(10), rules());
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 10, rules());
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20, rules());
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB, constraintC);
        Rule testRule = new Rule(rule("test"), Collections.singletonList(conditionalConstraint));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertTrue(isEquivalentTo(
            new TreeConstraintNode(
                Collections.emptySet(),
                Collections.singletonList(
                    new TreeDecisionNode(
                        new TreeConstraintNode(
                            Arrays.asList(
                                constraintA,
                                constraintB),
                            Collections.emptySet()
                        ),
                        new TreeConstraintNode(
                            Arrays.asList(
                                constraintA.negate(),
                                constraintC
                            ),
                            Collections.emptySet()
                        )
                    )
                )
            ),
            outputRule.getRootNode())
        );
    }

    // Checks IF (A OR B) THEN C
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfConditionalConstraintWithNestedOrIsPresent() {
        AtomicConstraint aEquals10 = new IsInSetConstraint(fieldA, Collections.singleton(10), rules());
        AtomicConstraint aGreaterThan10 = new IsGreaterThanConstantConstraint(fieldA, 10, rules());
        AtomicConstraint bGreaterThan20 = new IsGreaterThanConstantConstraint(fieldB, 20, rules());

        givenRule(
            new ConditionalConstraint(
                aEquals10.or(aGreaterThan10),
                bGreaterThan20));

        Assert.assertTrue(
            isEquivalentTo(
                getResultingRootOption(), new TreeConstraintNode(
                    Collections.emptyList(),
                    Collections.singletonList(
                        new TreeDecisionNode(
                            /* OPTION 1: AND(C, OR(A, B))  */
                            new TreeConstraintNode(
                                Collections.singletonList(bGreaterThan20),
                                Collections.singleton(
                                    new TreeDecisionNode(
                                        new TreeConstraintNode(aEquals10),
                                        new TreeConstraintNode(aGreaterThan10)))),
                            /* OPTION 2: AND(¬A, ¬B)  */
                            new TreeConstraintNode(
                                aEquals10.negate(),
                                aGreaterThan10.negate()
                            )
                        )
                    )
                )
            )
        );
    }

    // NOT (IF A THEN B ELSE C) - edge case
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedConditionalConstraintIsPresent() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsInSetConstraint constraintA = new IsInSetConstraint(inputFieldList.get(0), Collections.singleton(10), rules());
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20, rules());
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 10, rules());
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB, constraintC);
        Constraint notConstraint = conditionalConstraint.negate();
        Rule testRule = new Rule(rule("test"), Collections.singletonList(notConstraint));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        Assert.assertTrue(isEquivalentTo(
            new TreeConstraintNode(
                Collections.emptySet(),
                Collections.singletonList(
                    new TreeDecisionNode(
                        new TreeConstraintNode(
                            Arrays.asList(
                                constraintA,
                                constraintB.negate()
                            ),
                            Collections.emptySet()
                        ),
                        new TreeConstraintNode(
                            Arrays.asList(
                                constraintA.negate(),
                                constraintC.negate()
                            ),
                            Collections.emptySet()
                        )
                    )
                )
            ),
            outputRule.getRootNode())
        );
    }

    // NOT (IF A THEN B) - other edge case
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedConditionalConstraintWithoutElseClauseIsPresent() {
        // ¬(A -> B)
        // is equivalent to
        // A ^ ¬B

        AtomicConstraint aEqualTo10 = new IsInSetConstraint(fieldA, Collections.singleton(10), rules());
        AtomicConstraint bGreaterThan20 = new IsGreaterThanConstantConstraint(fieldB, 20, rules());

        Constraint inputRule = new ConditionalConstraint(aEqualTo10, bGreaterThan20).negate();

        ConstraintNode expectedOutput = new TreeConstraintNode(
            aEqualTo10,
            bGreaterThan20.negate());

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
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsInSetConstraint constraintA = new IsInSetConstraint(inputFieldList.get(0), Collections.singleton(10), rules());
        Constraint notConstraint0 = constraintA.negate();
        Constraint notConstraint1 = notConstraint0.negate();
        Rule testRule = new Rule(rule("test"), Collections.singletonList(notConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

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
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsInSetConstraint constraintA = new IsInSetConstraint(inputFieldList.get(0), Collections.singleton(10), rules());
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 5, rules());
        NegatedGrammaticalConstraint notConstraint = (NegatedGrammaticalConstraint) new AndConstraint(Arrays.asList(constraintA, constraintB)).negate();
        Rule testRule = new Rule(rule("test"), Collections.singletonList(notConstraint));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTree outputRule = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", outputRule, Is.is(IsNull.notNullValue()));
        // Result should be (NOT A) OR (NOT B)
        Assert.assertTrue(isEquivalentTo(
            new TreeConstraintNode(
                Collections.emptySet(),
                Collections.singletonList(
                    new TreeDecisionNode(
                        new TreeConstraintNode(
                            Collections.singletonList(constraintA.negate()),
                            Collections.emptySet()
                        ),
                        new TreeConstraintNode(
                            Collections.singletonList(constraintB.negate()),
                            Collections.emptySet()
                        )
                    )
                )
            ),
            outputRule.getRootNode())
        );
    }

    // (A OR B) OR C
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNestedOrsArePresent() {
        AtomicConstraint constraintA = new IsInSetConstraint(fieldA, Collections.singleton(10), rules());
        AtomicConstraint constraintB = new IsGreaterThanConstantConstraint(fieldB, 20, rules());
        AtomicConstraint constraintC = new IsGreaterThanConstantConstraint(fieldB, 10, rules());

        givenRule(
            new OrConstraint(
                new OrConstraint(constraintA, constraintB),
                constraintC));

        Assert.assertTrue(
            isEquivalentTo(
                getResultingRootOption(),
                new TreeConstraintNode(
                    Collections.emptyList(),
                    Collections.singletonList(
                        new TreeDecisionNode(
                            new TreeConstraintNode(constraintA),
                            new TreeConstraintNode(constraintB),
                            new TreeConstraintNode(constraintC))
                    )
                )
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

    private static Set<RuleInformation> rules(){
        return Collections.singleton(rule("rules"));
    }

    private static RuleInformation rule(String description){
        return new RuleInformation(description);
    }
}

package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.*;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.constraints.grammatical.*;
import com.scottlogic.deg.generator.decisiontree.test_utils.*;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.schemas.v3.RuleDTO;
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

import static com.scottlogic.deg.generator.AssertUtils.pairwiseAssert;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.*;

class ProfileDecisionTreeFactoryTests {
    private final Field fieldA = new Field("A");
    private final Field fieldB = new Field("B");
    private final Field fieldC = new Field("C");

    private final List<Rule> rules = new ArrayList<>();
    private DecisionTreeCollection actualOutput = null;

    @BeforeEach
    void beforeEach() {
        rules.clear();
        actualOutput = null;
    }

    private void givenRule(Constraint... constraints) {
        this.rules.add(new Rule(rule(""), Arrays.asList(constraints)));
    }

    private DecisionTreeCollection getActualOutput() {
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
        return this.getActualOutput().getDecisionTrees().iterator().next()
            .getRootNode();
    }

    @Test
    void shouldReturnAnalysedProfileWithNoAnalysedRules_IfProfileHasNoRules() {
        Profile testInput = new Profile(new ArrayList<>(), new ArrayList<>());
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTreeCollection testOutput = testObject.analyse(testInput);

        Assert.assertEquals(0, testOutput.getDecisionTrees().size());
    }

    @Test
    void shouldReturnAnalysedProfileWithCorrectFields() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        Profile testInput = new Profile(inputFieldList, new ArrayList<>());
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTreeCollection testOutput = testObject.analyse(testInput);
        ProfileFields actualFields = testOutput.getFields();

        pairwiseAssert(
            actualFields,
            inputFieldList,
            (actual, expected) -> Assert.assertThat(actual, sameInstance(expected)));
    }

    @Test
    void shouldReturnAnalysedRuleWithNoDecisions_IfProfileContainsOnlyAtomicConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10, rule());
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rule());
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"), rule());
        Rule testRule = new Rule(rule("test"), Arrays.asList(constraint0, constraint1, constraint2));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));

        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTreeCollection testOutput = testObject.analyse(testInput);

        Assert.assertThat("analyse() output is not null", testOutput, Is.is(IsNull.notNullValue()));
        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertThat("Decision tree root has non-null list of decisions",
            outputRule.getRootNode().getDecisions(), Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root has empty list of decisions",
            outputRule.getRootNode().getDecisions().size(), Is.is(0));
    }

    @Test
    void shouldReturnAnalysedRuleWithAllConstraintsInAtomicConstraintsCollection_IfProfileContainsOnlyAtomicConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10, rule());
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rule());
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"), rule());
        List<Constraint> inputConstraints = Arrays.asList(constraint0, constraint1, constraint2);
        Rule testRule = new Rule(rule("test"), inputConstraints);
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTreeCollection testOutput = testObject.analyse(testInput);

        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertThat("Decision tree root atomic constraint list is same size as original constraint list",
            outputRule.getRootNode().getAtomicConstraints().size(), Is.is(inputConstraints.size()));
        for (Constraint constraint : inputConstraints) {
            Assert.assertThat("Each input constraint is in the decision tree root node atomic constraint list",
                outputRule.getRootNode().getAtomicConstraints().contains(constraint), Is.is(true));
        }
    }

    @Test
    void shouldReturnAnalysedRuleWithNoDecisions_IfProfileContainsOnlyAtomicConstraintsAndAndConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10, rule());
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rule());
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraint0, constraint1));
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"), rule());
        Rule testRule = new Rule(rule("test"), Arrays.asList(andConstraint0, constraint2));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTreeCollection testOutput = testObject.analyse(testInput);

        Assert.assertThat("analyse() output contain decision tree list", testOutput.getDecisionTrees(),
            Is.is(IsNull.notNullValue()));
        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertThat("Decision tree root has non-null list of decisions",
            outputRule.getRootNode().getDecisions(), Is.is(IsNull.notNullValue()));
        Assert.assertThat("Decision tree root has empty list of decisions",
            outputRule.getRootNode().getDecisions().size(), Is.is(0));
    }

    @Test
    void shouldReturnAnalysedRuleWithAllAtomicConstraintsInAtomicConstraintsCollection_IfProfileContainsOnlyAtomicConstraintsAndAndConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10, rule());
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rule());
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraint0, constraint1));
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"), rule());
        Rule testRule = new Rule(rule("test"), Arrays.asList(andConstraint0, constraint2));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));

        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTreeCollection testOutput = testObject.analyse(testInput);

        Assert.assertThat("analyse() output contain decision tree list", testOutput.getDecisionTrees(),
            Is.is(IsNull.notNullValue()));
        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
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
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10, rule());
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rule());
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraint0, constraint1));
        IsEqualToConstantConstraint constraint2 = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam", rule());
        IsEqualToConstantConstraint constraint3 = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel", rule());
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraint2, constraint3));
        Rule testRule = new Rule(rule("test"), Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTreeCollection testOutput = testObject.analyse(testInput);

        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertThat("Decision tree root contains correct number of decisions",
            outputRule.getRootNode().getDecisions().size(), Is.is(2));
    }

    // checks (A OR B) AND (C OR D)
    @Test
    void shouldReturnAnalysedRuleWithNoAtomicConstraints_IfAllAtomicConstraintsInProfileAreChildrenOfOrConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10, rule());
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rule());
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
        IsEqualToConstantConstraint constraintC = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam", rule());
        IsEqualToConstantConstraint constraintD = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel", rule());
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintC, constraintD));
        Rule testRule = new Rule(rule("test"), Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTreeCollection testOutput = testObject.analyse(testInput);

        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertThat("Decision tree root contains no atomic constraints",
            outputRule.getRootNode().getAtomicConstraints().size(), Is.is(0));
    }

    // checks (A OR B) AND (C OR D)
    @Test
    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfAllAtomicConstraintsInProfileAreChildrenOfOrConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10, rule());
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rule());
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
        IsEqualToConstantConstraint constraintC = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam", rule());
        IsEqualToConstantConstraint constraintD = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel", rule());
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintC, constraintD));
        Rule testRule = new Rule(rule("test"), Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTreeCollection testOutput = testObject.analyse(testInput);

        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
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
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10, rule());
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0, rule());
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 5, rule());
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraintC, constraintB));
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, andConstraint0));
        IsEqualToConstantConstraint constraintD = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam", rule());
        IsEqualToConstantConstraint constraintE = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel", rule());
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintD, constraintE));
        Rule testRule = new Rule(rule("test"), Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTreeCollection testOutput = testObject.analyse(testInput);

        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
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
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10, rule());
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 10, rule());
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20, rule());
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB, constraintC);
        Rule testRule = new Rule(rule("test"), Collections.singletonList(conditionalConstraint));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTreeCollection testOutput = testObject.analyse(testInput);

        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertTrue(isEquivalentTo(
            new TreeConstraintNode(
                Collections.emptySet(),
                Arrays.asList(
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
        AtomicConstraint aEquals10 = new IsEqualToConstantConstraint(fieldA, 10, rule());
        AtomicConstraint aGreaterThan10 = new IsGreaterThanConstantConstraint(fieldA, 10, rule());
        AtomicConstraint bGreaterThan20 = new IsGreaterThanConstantConstraint(fieldB, 20, rule());

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
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10, rule());
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20, rule());
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 10, rule());
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB, constraintC);
        Constraint notConstraint = conditionalConstraint.negate();
        Rule testRule = new Rule(rule("test"), Collections.singletonList(notConstraint));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTreeCollection testOutput = testObject.analyse(testInput);

        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();

        Assert.assertTrue(isEquivalentTo(
            new TreeConstraintNode(
                Collections.emptySet(),
                Arrays.asList(
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

        AtomicConstraint aEqualTo10 = new IsEqualToConstantConstraint(fieldA, 10, rule());
        AtomicConstraint bGreaterThan20 = new IsGreaterThanConstantConstraint(fieldB, 20, rule());

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
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10, rule());
        Constraint notConstraint0 = constraintA.negate();
        Constraint notConstraint1 = notConstraint0.negate();
        Rule testRule = new Rule(rule("test"), Collections.singletonList(notConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTreeCollection testOutput = testObject.analyse(testInput);

        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
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
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10, rule());
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 5, rule());
        NegatedGrammaticalConstraint notConstraint = (NegatedGrammaticalConstraint) new AndConstraint(Arrays.asList(constraintA, constraintB)).negate();
        Rule testRule = new Rule(rule("test"), Collections.singletonList(notConstraint));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        ProfileDecisionTreeFactory testObject = new ProfileDecisionTreeFactory();

        DecisionTreeCollection testOutput = testObject.analyse(testInput);

        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        // Result should be (NOT A) OR (NOT B)
        Assert.assertTrue(isEquivalentTo(
            new TreeConstraintNode(
                Collections.emptySet(),
                Arrays.asList(
                    new TreeDecisionNode(
                        new TreeConstraintNode(
                            Arrays.asList(constraintA.negate()),
                            Collections.emptySet()
                        ),
                        new TreeConstraintNode(
                            Arrays.asList(constraintB.negate()),
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
        AtomicConstraint constraintA = new IsEqualToConstantConstraint(fieldA, 10, rule());
        AtomicConstraint constraintB = new IsGreaterThanConstantConstraint(fieldB, 20, rule());
        AtomicConstraint constraintC = new IsGreaterThanConstantConstraint(fieldB, 10, rule());

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

    private void treeRootShouldMatch(ConstraintNode expected) {
        ConstraintNode actual = this.getActualOutput().getMergedTree().getRootNode();

        Assert.assertTrue(
            isEquivalentTo(actual, expected)
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

    private final AtomicConstraint aIsNull = new IsNullConstraint(new Field("A"), rule());
    private final AtomicConstraint bEquals10 = new IsEqualToConstantConstraint(new Field("B"), 10, rule());
    private final AtomicConstraint cIsNumeric = new IsOfTypeConstraint(new Field("C"), IsOfTypeConstraint.Types.NUMERIC, rule());
    private final AtomicConstraint eIsString = new IsOfTypeConstraint(new Field("E"), IsOfTypeConstraint.Types.STRING, rule());

    @Test
    void whenViolatingPositiveAtomic() {
        givenRule(
            new ViolateConstraint(
                aIsNull));

        treeRootShouldMatch(
            new TreeConstraintNode(
                aIsNull.negate()));
    }

    @Test
    void whenViolatingNegativeAtomic() {
        givenRule(
            new ViolateConstraint(
                aIsNull.negate()));

        treeRootShouldMatch(
            new TreeConstraintNode(
                aIsNull));
    }

    @Test
    void whenViolatingOrConstraint() {
        givenRule(
            new ViolateConstraint(
                new OrConstraint(aIsNull, cIsNumeric)));

        treeRootShouldMatch(
            new TreeConstraintNode(
                aIsNull.negate(),
                cIsNumeric.negate()));
    }

    @Test
    void whenViolatingAndConstraint() {
        givenRule(
            new ViolateConstraint(
                new AndConstraint(aIsNull, cIsNumeric, eIsString)));

        treeRootShouldMatch(
            new TreeConstraintNode(
                new TreeDecisionNode(
                    new TreeConstraintNode(
                        aIsNull.negate(),
                        cIsNumeric,
                        eIsString),
                    new TreeConstraintNode(
                        aIsNull,
                        cIsNumeric.negate(),
                        eIsString),
                    new TreeConstraintNode(
                        aIsNull,
                        cIsNumeric,
                        eIsString.negate()))));
    }

    @Test
    void whenViolatingIfConstraint() {
        givenRule(
            new ViolateConstraint(
                new ConditionalConstraint(
                    aIsNull,
                    bEquals10,
                    eIsString)));


        new TreeConstraintNode(
            new TreeDecisionNode(
                new TreeConstraintNode(
                    aIsNull,
                    bEquals10.negate()),
                new TreeConstraintNode(
                    aIsNull.negate(),
                    eIsString.negate())));
    }

    private void assertOptionContainsSingleConstraint(ConstraintNode option, Constraint constraint) {
        Assert.assertThat("Option contains no decisions", option.getDecisions().size(), Is.is(0));
        Assert.assertThat("Option contains one atomic constraint", option.getAtomicConstraints().size(),
            Is.is(1));
        Assert.assertThat("Option's atomic constraint is the given constraint",
            option.getAtomicConstraints().contains(constraint), Is.is(true));
        Assert.assertThat(option.getAtomicConstraints(), contains(constraint));
    }

    private static RuleInformation rule(){
        return rule("rule");
    }

    private static RuleInformation rule(String description){
        RuleDTO rule = new RuleDTO();
        rule.rule = description;
        return new RuleInformation(rule);
    }
}

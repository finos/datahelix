//package com.scottlogic.deg.generator.decisiontree;
//
//import com.scottlogic.deg.generator.Field;
//import com.scottlogic.deg.generator.Rule;
//import com.scottlogic.deg.generator.constraints.*;
//import org.hamcrest.Matcher;
//import org.junit.Assert;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.stream.Collectors;
//
//import static com.scottlogic.deg.generator.AssertingMatcher.matchesAssertions;
//import static org.hamcrest.CoreMatchers.sameInstance;
//import static org.hamcrest.Matchers.*;
//
//class DecisionTreeGeneratorTests {
//    private final IConstraint aIsNull = new IsNullConstraint(new Field("A"));
//    private final IConstraint bEquals10 = new IsEqualToConstantConstraint(new Field("B"), 10);
//    private final IConstraint cIsNumeric = new IsOfTypeConstraint(new Field("C"), IsOfTypeConstraint.Types.Numeric);
//    private final IConstraint dGreaterThan10 = new IsGreaterThanConstantConstraint(new Field("D"), 10);
//    private final IConstraint eIsString = new IsOfTypeConstraint(new Field("E"), IsOfTypeConstraint.Types.String);
//
//<<<<<<< HEAD
//    private Rule inputRule = null;
//    private DecisionTree actualOutput = null;
//=======
//    private final List<Rule> rules = new ArrayList<>();
//    private DecisionTreeCollection actualOutput = null;
//>>>>>>> master
//
//    @BeforeEach
//    private void beforeEach() {
//        this.actualOutput = null;
//        this.inputRule = null;
//    }
//
//    private void givenRule(IConstraint... constraints) {
//        this.inputRule = new Rule("", Arrays.asList(constraints));
//    }
//
//<<<<<<< HEAD
//    private DecisionTree getActualOutput() {
//=======
//    private DecisionTreeCollection getActualOutput() {
//>>>>>>> master
//        if (this.actualOutput == null) {
//            DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//            this.actualOutput = testObject.generateTreeFor(this.inputRule);
//        }
//
//        return this.actualOutput;
//    }
//
//    private void treeRootShouldMatch(ConstraintNode expected) {
//        ConstraintNode actual = this.getActualOutput().getRootNode();
//
//        Assert.assertThat(actual, isEquivalentTo(expected));
//    }
//
//    private Matcher<IConstraint> sameNegation(IConstraint expected) {
//        return matchesAssertions(
//            "Both constraints negate the same constraint instance",
//            (actual, subAssert) -> {
//                subAssert.assertThat(actual, instanceOf(NotConstraint.class));
//                subAssert.assertThat(expected, instanceOf(NotConstraint.class));
//
//                // this method doesn't stop when an assertion fails, so we need to do this check to stop invalid casts below
//                if (!(actual instanceof NotConstraint && expected instanceof NotConstraint))
//                    return;
//
//                subAssert.assertThat(
//                    ((NotConstraint) actual).negatedConstraint,
//                    sameInstance(((NotConstraint)expected).negatedConstraint));
//            });
//    }
//
//    private Matcher<ConstraintNode> isEquivalentTo(ConstraintNode expected) {
//        return matchesAssertions(
//            "matching option node",
//            (actual, asserter) -> {
//                asserter.assertThat( // Should have same number of atomic constraints
//                    actual.getAtomicConstraints().size(),
//                    equalTo(expected.getAtomicConstraints().size()));
//
//                asserter.assertThat( // Should have same atomic constraints
//                    actual.getAtomicConstraints(),
//                    containsInAnyOrder(
//                        expected.getAtomicConstraints().stream()
//                            .map(c -> anyOf(sameInstance(c), sameNegation(c)))
//                            .collect(Collectors.toList())));
//
//                asserter.assertThat( // Should have same number of decisions
//                    actual.getDecisions().size(),
//                    equalTo(expected.getDecisions().size()));
//
//                asserter.assertThat( // Should have same decisions
//                    actual.getDecisions(),
//                    containsInAnyOrder(
//                        expected.getDecisions().stream()
//                            .map(this::isEquivalentTo)
//                            .collect(Collectors.toList())));
//            });
//    }
//
//    private Matcher<DecisionNode> isEquivalentTo(DecisionNode expected) {
//        return matchesAssertions(
//            "matching decision node",
//            (actual, asserter) -> {
//                asserter.assertThat( // Should have same number of options
//                    actual.getOptions().size(),
//                    equalTo(expected.getOptions().size()));
//
//                asserter.assertThat( // Should have same options
//                    actual.getOptions(),
//                    containsInAnyOrder(
//                        expected.getOptions().stream()
//                            .map(this::isEquivalentTo)
//                            .collect(Collectors.toList())));
//            });
//    }
//
//
//<<<<<<< HEAD
//=======
//    @Test
//    void shouldReturnAnalysedProfileWithNoAnalysedRules_IfProfileHasNoRules() {
//        Profile testInput = new Profile(new ArrayList<>(), new ArrayList<>());
//        DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//        DecisionTreeCollection testOutput = testObject.analyse(testInput);
//
//        Assert.assertEquals(0, testOutput.getDecisionTrees().size());
//    }
//
//    @Test
//    void shouldReturnAnalysedProfileWithCorrectFields() {
//        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
//        Profile testInput = new Profile(inputFieldList, new ArrayList<>());
//        DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//        DecisionTreeCollection testOutput = testObject.analyse(testInput);
//        ProfileFields actualFields = testOutput.getFields();
//
//        pairwiseAssert(
//            actualFields,
//            inputFieldList,
//            (actual, expected) -> Assert.assertThat(actual, sameInstance(expected)));
//    }
//
//    @Test
//    void shouldReturnAnalysedRuleWithNoDecisions_IfProfileContainsOnlyAtomicConstraints() {
//        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
//        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
//        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
//        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
//        Rule testRule = new Rule("test", Arrays.asList(constraint0, constraint1, constraint2));
//        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
//
//        DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//        DecisionTreeCollection testOutput = testObject.analyse(testInput);
//
//        Assert.assertThat("analyse() output is not null", testOutput, Is.is(IsNull.notNullValue()));
//        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
//        Assert.assertThat("Decision tree root has non-null list of decisions",
//                outputRule.getRootNode().getDecisions(), Is.is(IsNull.notNullValue()));
//        Assert.assertThat("Decision tree root has empty list of decisions",
//                outputRule.getRootNode().getDecisions().size(), Is.is(0));
//    }
//
//    @Test
//    void shouldReturnAnalysedRuleWithAllConstraintsInAtomicConstraintsCollection_IfProfileContainsOnlyAtomicConstraints() {
//        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
//        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
//        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
//        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
//        List<IConstraint> inputConstraints = Arrays.asList(constraint0, constraint1, constraint2);
//        Rule testRule = new Rule("test", inputConstraints);
//        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
//        DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//        DecisionTreeCollection testOutput = testObject.analyse(testInput);
//
//        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
//        Assert.assertThat("Decision tree root atomic constraint list is same size as original constraint list",
//                outputRule.getRootNode().getAtomicConstraints().size(), Is.is(inputConstraints.size()));
//        for (IConstraint constraint : inputConstraints) {
//            Assert.assertThat("Each input constraint is in the decision tree root node atomic constraint list",
//                    outputRule.getRootNode().getAtomicConstraints().contains(constraint), Is.is(true));
//        }
//    }
//
//    @Test
//    void shouldReturnAnalysedRuleWithNoDecisions_IfProfileContainsOnlyAtomicConstraintsAndAndConstraints() {
//        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
//        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
//        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
//        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraint0, constraint1));
//        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
//        Rule testRule = new Rule("test", Arrays.asList(andConstraint0, constraint2));
//        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
//        DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//        DecisionTreeCollection testOutput = testObject.analyse(testInput);
//
//        Assert.assertThat("analyse() output contain decision tree list", testOutput.getDecisionTrees(),
//                Is.is(IsNull.notNullValue()));
//        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
//        Assert.assertThat("Decision tree root has non-null list of decisions",
//                outputRule.getRootNode().getDecisions(), Is.is(IsNull.notNullValue()));
//        Assert.assertThat("Decision tree root has empty list of decisions",
//                outputRule.getRootNode().getDecisions().size(), Is.is(0));
//    }
//>>>>>>> master
//
//    // checks A, B, C
//    @Test
//<<<<<<< HEAD
//    void withJustAtomicConstraints() {
//        givenRule(aIsNull, bEquals10, dGreaterThan10);
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                aIsNull,
//                bEquals10,
//                dGreaterThan10));
//=======
//    void shouldReturnAnalysedRuleWithAllAtomicConstraintsInAtomicConstraintsCollection_IfProfileContainsOnlyAtomicConstraintsAndAndConstraints() {
//        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
//        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
//        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
//        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraint0, constraint1));
//        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
//        Rule testRule = new Rule("test", Arrays.asList(andConstraint0, constraint2));
//        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
//
//        DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//        DecisionTreeCollection testOutput = testObject.analyse(testInput);
//
//        Assert.assertThat("analyse() output contain decision tree list", testOutput.getDecisionTrees(),
//                Is.is(IsNull.notNullValue()));
//        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
//        Assert.assertThat("Decision tree root contains correct number of atomic constraints",
//                outputRule.getRootNode().getAtomicConstraints().size(), Is.is(3));
//        Assert.assertThat("Decision tree root atomic constraints list contains constraint 0",
//                outputRule.getRootNode().getAtomicConstraints().contains(constraint0), Is.is(true));
//        Assert.assertThat("Decision tree root atomic constraints list contains constraint 1",
//                outputRule.getRootNode().getAtomicConstraints().contains(constraint1), Is.is(true));
//        Assert.assertThat("Decision tree root atomic constraints list contains constraint 2",
//                outputRule.getRootNode().getAtomicConstraints().contains(constraint2), Is.is(true));
//>>>>>>> master
//    }
//
//    // checks ((A AND B) AND C)
//    @Test
//<<<<<<< HEAD
//    void withAtomicConstraintsNestedInAnd() {
//        givenRule(
//            new AndConstraint(
//                new AndConstraint(aIsNull, bEquals10),
//                dGreaterThan10));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                aIsNull,
//                bEquals10,
//                dGreaterThan10));
//=======
//    void shouldReturnAnalysedRuleWithDecisionForEachOrConstraint() {
//        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
//        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
//        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
//        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraint0, constraint1));
//        IsEqualToConstantConstraint constraint2 = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam");
//        IsEqualToConstantConstraint constraint3 = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel");
//        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraint2, constraint3));
//        Rule testRule = new Rule("test", Arrays.asList(orConstraint0, orConstraint1));
//        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
//        DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//        DecisionTreeCollection testOutput = testObject.analyse(testInput);
//
//        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
//        Assert.assertThat("Decision tree root contains correct number of decisions",
//                outputRule.getRootNode().getDecisions().size(), Is.is(2));
//>>>>>>> master
//    }
//
//    // checks (A OR B), (C OR D)
//    @Test
//<<<<<<< HEAD
//    void withMultipleOrConstraints() {
//        givenRule(
//            new OrConstraint(aIsNull, bEquals10),
//            new OrConstraint(dGreaterThan10, cIsNumeric));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                new DecisionNode(
//                    new ConstraintNode(aIsNull),
//                    new ConstraintNode(bEquals10)),
//                new DecisionNode(
//                    new ConstraintNode(dGreaterThan10),
//                    new ConstraintNode(cIsNumeric))));
//=======
//    void shouldReturnAnalysedRuleWithNoAtomicConstraints_IfAllAtomicConstraintsInProfileAreChildrenOfOrConstraints() {
//        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
//        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
//        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
//        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
//        IsEqualToConstantConstraint constraintC = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam");
//        IsEqualToConstantConstraint constraintD = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel");
//        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintC, constraintD));
//        Rule testRule = new Rule("test", Arrays.asList(orConstraint0, orConstraint1));
//        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
//        DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//        DecisionTreeCollection testOutput = testObject.analyse(testInput);
//
//        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
//        Assert.assertThat("Decision tree root contains no atomic constraints",
//                outputRule.getRootNode().getAtomicConstraints().size(), Is.is(0));
//>>>>>>> master
//    }
//
//    // checks (A OR B) AND (C OR D)
//    @Test
//<<<<<<< HEAD
//    void withOrConstraintsWrappedInAnd() {
//        givenRule(
//            new AndConstraint(
//                new OrConstraint(aIsNull, bEquals10),
//                new OrConstraint(dGreaterThan10, cIsNumeric)));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                new DecisionNode(
//                    new ConstraintNode(aIsNull),
//                    new ConstraintNode(bEquals10)),
//                new DecisionNode(
//                    new ConstraintNode(dGreaterThan10),
//                    new ConstraintNode(cIsNumeric))));
//=======
//    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfAllAtomicConstraintsInProfileAreChildrenOfOrConstraints() {
//        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
//        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
//        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
//        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
//        IsEqualToConstantConstraint constraintC = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam");
//        IsEqualToConstantConstraint constraintD = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel");
//        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintC, constraintD));
//        Rule testRule = new Rule("test", Arrays.asList(orConstraint0, orConstraint1));
//        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
//        DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//        DecisionTreeCollection testOutput = testObject.analyse(testInput);
//
//        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
//        List<DecisionNode> decisions = new ArrayList<>(outputRule.getRootNode().getDecisions());
//        Assert.assertThat("First decision has correct number of options", decisions.get(0).getOptions().size(),
//                Is.is(2));
//        List<ConstraintNode> options = new ArrayList<>(decisions.get(0).getOptions());
//        assertOptionContainsSingleConstraint(options.get(0), constraintA);
//        assertOptionContainsSingleConstraint(options.get(1), constraintB);
//        Assert.assertThat("Second decision has correct number of options", decisions.get(1).getOptions().size(),
//                Is.is(2));
//        options = new ArrayList<>(decisions.get(1).getOptions());
//        assertOptionContainsSingleConstraint(options.get(0), constraintC);
//        assertOptionContainsSingleConstraint(options.get(1), constraintD);
//>>>>>>> master
//    }
//
//    // Checks (A OR (B AND C)) AND (D OR E)
//    @Test
//<<<<<<< HEAD
//    void withComplexMixedNestingOfAndOr() {
//        givenRule(
//            new AndConstraint(
//                new OrConstraint(
//                    aIsNull,
//                    new AndConstraint(
//                        bEquals10,
//                        cIsNumeric)),
//                new OrConstraint(dGreaterThan10, eIsString)));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                new DecisionNode(
//                    new ConstraintNode(aIsNull),
//                    new ConstraintNode(bEquals10, cIsNumeric)),
//                new DecisionNode(
//                    new ConstraintNode(dGreaterThan10),
//                    new ConstraintNode(eIsString))));
//=======
//    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfAllAtomicConstraintsInProfileAreChildrenOfOrAndAndConstraints() {
//        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
//        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
//        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
//        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 5);
//        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraintC, constraintB));
//        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, andConstraint0));
//        IsEqualToConstantConstraint constraintD = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam");
//        IsEqualToConstantConstraint constraintE = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel");
//        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintD, constraintE));
//        Rule testRule = new Rule("test", Arrays.asList(orConstraint0, orConstraint1));
//        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
//        DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//        DecisionTreeCollection testOutput = testObject.analyse(testInput);
//
//        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
//        List<DecisionNode> decisions = new ArrayList<>(outputRule.getRootNode().getDecisions());
//        Assert.assertThat("First decision has two options", decisions.get(0).getOptions().size(), Is.is(2));
//        List<ConstraintNode> options = new ArrayList<>(decisions.get(0).getOptions());
//        assertOptionContainsSingleConstraint(options.get(0), constraintA);
//        Assert.assertThat("Second option of first decision has no further decisions",
//                options.get(1).getDecisions().size(), Is.is(0));
//        Assert.assertThat("Second option of first decision has two atomic constraints",
//                options.get(1).getAtomicConstraints().size(), Is.is(2));
//        Assert.assertThat("Second option of first decision contains constraint C",
//                options.get(1).getAtomicConstraints().contains(constraintC), Is.is(true));
//        Assert.assertThat("Second option of first decision contains constraint B",
//                options.get(1).getAtomicConstraints().contains(constraintB), Is.is(true));
//        Assert.assertThat("Second decision has two options", decisions.get(1).getOptions().size(), Is.is(2));
//        Assert.assertEquals(2, decisions.get(1).getOptions().size());
//        options = new ArrayList<>(decisions.get(1).getOptions());
//        assertOptionContainsSingleConstraint(options.get(0), constraintD);
//        assertOptionContainsSingleConstraint(options.get(1), constraintE);
//>>>>>>> master
//    }
//
//    // Checks IF (A) THEN B ELSE C
//    @Test
//<<<<<<< HEAD
//    void withIfConstraint() {
//        givenRule(
//            new ConditionalConstraint(
//                aIsNull,
//                cIsNumeric,
//                eIsString));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                new DecisionNode(
//                    new ConstraintNode(aIsNull, cIsNumeric),
//                    new ConstraintNode(aIsNull.isFalse(), eIsString))));
//=======
//    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfConditionalConstraintIsPresent() {
//        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
//        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
//        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 10);
//        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20);
//        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB, constraintC);
//        Rule testRule = new Rule("test", Collections.singletonList(conditionalConstraint));
//        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
//        DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//        DecisionTreeCollection testOutput = testObject.analyse(testInput);
//
//        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
//        Assert.assertThat("Decision tree root contains no atomic constraints",
//                outputRule.getRootNode().getAtomicConstraints().size(), Is.is(0));
//        Assert.assertThat("Decision tree root contains one decision",
//                outputRule.getRootNode().getDecisions().size(), Is.is(1));
//        DecisionNode decision = outputRule.getRootNode().getDecisions().iterator().next();
//        Assert.assertThat("Decision has two options", decision.getOptions().size(), Is.is(2));
//        List<ConstraintNode> options = new ArrayList<>(decision.getOptions());
//        Assert.assertThat("First option contains atomic constraint A",
//                options.get(0).getAtomicConstraints().contains(constraintA), Is.is(true));
//        Assert.assertThat("First option contains atomic constraint B",
//                options.get(0).getAtomicConstraints().contains(constraintB), Is.is(true));
//        Assert.assertThat("Second option contains atomic constraint C",
//                options.get(1).getAtomicConstraints().contains(constraintC), Is.is(true));
//        IConstraint constraint = options.get(1).getAtomicConstraints().iterator().next();
//        Assert.assertThat("First atomic constraint of second option is a NotConstraint",
//                constraint instanceof NotConstraint, Is.is(true));
//        Assert.assertThat("First atomic constraint of second option is a negated version of constraint A",
//                ((NotConstraint)constraint).negatedConstraint, Is.is(constraintA));
//>>>>>>> master
//    }
//
//    // Checks IF (A OR B) THEN C
//    @Test
//    void withIfConstraintOverOrConstraint() {
//        givenRule(
//            new ConditionalConstraint(
//                aIsNull.or(bEquals10),
//                cIsNumeric));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                Collections.emptyList(),
//                Arrays.asList(
//                    new DecisionNode(
//                        /* OPTION 1: AND(C, OR(A, B))  */
//                        new ConstraintNode(
//                            Arrays.asList(cIsNumeric),
//                            Collections.singleton(
//                                new DecisionNode(
//                                    new ConstraintNode(aIsNull),
//                                    new ConstraintNode(bEquals10)))),
//                        /* OPTION 2: AND(¬A, ¬B)  */
//                        new ConstraintNode(
//                            new NotConstraint(aIsNull),
//                            new NotConstraint(bEquals10))))));
//    }
//
//    // NOT (IF A THEN B ELSE C) - edge case
//    @Test
//    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedConditionalConstraintIsPresent() {
//<<<<<<< HEAD
//        givenRule(
//            new NotConstraint(
//                new ConditionalConstraint(
//                    aIsNull,
//                    cIsNumeric,
//                    eIsString)));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                new DecisionNode(
//                    new ConstraintNode(aIsNull, cIsNumeric.isFalse()),
//                    new ConstraintNode(aIsNull.isFalse(), eIsString.isFalse()))));
//=======
//        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
//        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
//        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20);
//        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 10);
//        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB, constraintC);
//        NotConstraint notConstraint = new NotConstraint(conditionalConstraint);
//        Rule testRule = new Rule("test", Collections.singletonList(notConstraint));
//        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
//        DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//        DecisionTreeCollection testOutput = testObject.analyse(testInput);
//
//        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
//        Assert.assertThat("Decision tree root contains no atomic constraints",
//                outputRule.getRootNode().getAtomicConstraints().size(), Is.is(0));
//        Assert.assertThat("Decision tree root contains one decision",
//                outputRule.getRootNode().getDecisions().size(), Is.is(1));
//        DecisionNode decision = outputRule.getRootNode().getDecisions().iterator().next();
//        Assert.assertThat("First decision has two options", decision.getOptions().size(), Is.is(2));
//        List<ConstraintNode> options = new ArrayList<>(decision.getOptions());
//        // First option should be A AND (NOT B)
//        Assert.assertThat("First option has two atomic constraints", options.get(0).getAtomicConstraints().size(),
//                Is.is(2));
//        List<IConstraint> constraints = new ArrayList<>(options.get(0).getAtomicConstraints());
//        Assert.assertThat("First atomic constraint of first option is constraint A", constraints.get(0),
//                Is.is(constraintA));
//        Assert.assertThat("Second atomic constraint of first option is a NOT constraint",
//                constraints.get(1) instanceof NotConstraint, Is.is(true));
//        Assert.assertThat("Second atomic constraint of first option is NOT(constraint B)",
//                ((NotConstraint)constraints.get(1)).negatedConstraint, Is.is(constraintB));
//        // Second option should be (NOT A) AND (NOT C)
//        Assert.assertThat("Second option has two atomic constraints", options.get(1).getAtomicConstraints().size(),
//                Is.is(2));
//        Assert.assertThat("Second option has no subdecisions", options.get(1).getDecisions().size(), Is.is(0));
//        constraints = new ArrayList<>(options.get(1).getAtomicConstraints());
//        Assert.assertThat("First atomic constraint of second option is a NOT constraint",
//                constraints.get(0) instanceof NotConstraint, Is.is(true));
//        Assert.assertThat("Second atomic constraint of second option is a NOT constraint",
//                constraints.get(1) instanceof NotConstraint, Is.is(true));
//        Assert.assertThat("First atomic constraint of second option is negated constraint A",
//                ((NotConstraint)constraints.get(0)).negatedConstraint, Is.is(constraintA));
//        Assert.assertThat("Second atomic constraint of second option is negated constraint C",
//                ((NotConstraint)constraints.get(1)).negatedConstraint, Is.is(constraintC));
//>>>>>>> master
//    }
//
//    // NOT (IF A THEN B) - other edge case
//    @Test
//    void withNegatedIf() {
//        // ¬(A -> B)
//        // is equivalent to
//        // A ^ ¬B
//
//        givenRule(
//            new NotConstraint(
//                new ConditionalConstraint(aIsNull, bEquals10)));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                aIsNull,
//                bEquals10.isFalse()));
//    }
//
//    // NOT (NOT A)
//    @Test
//<<<<<<< HEAD
//    void withDoubleNegatedAtomicConstraint() {
//        givenRule(
//            new NotConstraint(
//                new NotConstraint(aIsNull)));
//
//        treeRootShouldMatch(
//            new ConstraintNode(aIsNull));
//=======
//    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfDoubleNegationIsPresent() {
//        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
//        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
//        NotConstraint notConstraint0 = new NotConstraint(constraintA);
//        NotConstraint notConstraint1 = new NotConstraint(notConstraint0);
//        Rule testRule = new Rule("test", Collections.singletonList(notConstraint1));
//        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
//        DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//        DecisionTreeCollection testOutput = testObject.analyse(testInput);
//
//        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
//        // Result should just be A.
//        Assert.assertThat("Decision tree root contains one atomic constraint",
//                outputRule.getRootNode().getAtomicConstraints().size(), Is.is(1));
//        Assert.assertThat("Decision tree root contains no decisions",
//                outputRule.getRootNode().getDecisions().size(), Is.is(0));
//        Assert.assertThat("Atomic constraint of decision tree root is constraint A",
//                outputRule.getRootNode().getAtomicConstraints().contains(constraintA), Is.is(true));
//>>>>>>> master
//    }
//
//    // NOT (A AND B)
//    @Test
//<<<<<<< HEAD
//    void withNegatedAnd() {
//        givenRule(
//            new NotConstraint(
//                new AndConstraint(
//                    aIsNull,
//                    bEquals10)));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                new DecisionNode(
//                    new ConstraintNode(aIsNull.isFalse()),
//                    new ConstraintNode(bEquals10.isFalse()))));
//=======
//    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedAndIsPresent() {
//        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
//        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
//        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 5);
//        NotConstraint notConstraint = new NotConstraint(new AndConstraint(Arrays.asList(constraintA, constraintB)));
//        Rule testRule = new Rule("test", Collections.singletonList(notConstraint));
//        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
//        DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//        DecisionTreeCollection testOutput = testObject.analyse(testInput);
//
//        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
//        // Result should be (NOT A) OR (NOT B)
//        Assert.assertThat("Decision tree root contains no atomic constraints",
//                outputRule.getRootNode().getAtomicConstraints().size(), Is.is(0));
//        Assert.assertThat("Decision tree root contains one decision",
//                outputRule.getRootNode().getDecisions().size(), Is.is(1));
//        DecisionNode decision = outputRule.getRootNode().getDecisions().iterator().next();
//        Assert.assertThat("Decision has two options", decision.getOptions().size(), Is.is(2));
//        List<ConstraintNode> options = new ArrayList<>(decision.getOptions());
//        Assert.assertThat("First option has one atomic constraint", options.get(0).getAtomicConstraints().size(),
//                Is.is(1));
//        Assert.assertThat("First option has no subdecisions", options.get(0).getDecisions().size(), Is.is(0));
//        IConstraint testConstraint = options.get(0).getAtomicConstraints().iterator().next();
//        Assert.assertThat("Atomic constraint of first option is a NOT constraint",
//                testConstraint instanceof NotConstraint, Is.is(true));
//        Assert.assertThat("Atomic constraint of first option is NOT(Constraint A)",
//                ((NotConstraint)testConstraint).negatedConstraint, Is.is(constraintA));
//        Assert.assertThat("Second option has one atomic constraint", options.get(1).getAtomicConstraints().size(),
//                Is.is(1));
//        Assert.assertThat("Second option has no subdecisions", options.get(1).getDecisions().size(), Is.is(0));
//        testConstraint = options.get(1).getAtomicConstraints().iterator().next();
//        Assert.assertThat("Atomic constraint of second option is a NOT constraint",
//                testConstraint instanceof NotConstraint, Is.is(true));
//        Assert.assertThat("Atomic constraint of second option is NOT(Constraint B)",
//                ((NotConstraint)testConstraint).negatedConstraint, Is.is(constraintB));
//>>>>>>> master
//    }
//
//    // (A OR B) OR C
//    @Test
//    void withNestedOrs() {
//        givenRule(
//            new OrConstraint(
//                new OrConstraint(aIsNull, bEquals10),
//                cIsNumeric));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                Collections.emptyList(),
//                Arrays.asList(
//                    new DecisionNode(
//                        new ConstraintNode(aIsNull),
//                        new ConstraintNode(bEquals10),
//                        new ConstraintNode(cIsNumeric)))));
//    }
//
//    @Test
//    void whenViolatingPositiveAtomic() {
//        givenRule(
//            new ViolateConstraint(
//                aIsNull));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                new NotConstraint(
//                    aIsNull)));
//    }
//
//    @Test
//    void whenViolatingNegativeAtomic() {
//        givenRule(
//            new ViolateConstraint(
//                new NotConstraint(
//                    aIsNull)));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                aIsNull));
//    }
//
//    @Test
//    void whenViolatingOrConstraint() {
//        givenRule(
//            new ViolateConstraint(
//                new OrConstraint(aIsNull, cIsNumeric)));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                new NotConstraint(aIsNull),
//                new NotConstraint(cIsNumeric)));
//    }
//
//    @Test
//    void whenViolatingAndConstraint() {
//        givenRule(
//            new ViolateConstraint(
//                new AndConstraint(aIsNull, cIsNumeric, eIsString)));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                new DecisionNode(
//                    new ConstraintNode(
//                        new NotConstraint(aIsNull),
//                        cIsNumeric,
//                        eIsString),
//                    new ConstraintNode(
//                        aIsNull,
//                        new NotConstraint(cIsNumeric),
//                        eIsString),
//                    new ConstraintNode(
//                        aIsNull,
//                        cIsNumeric,
//                        new NotConstraint(eIsString)))));
//    }
//
//    @Test
//    void whenViolatingIfConstraint() {
//        givenRule(
//            new ViolateConstraint(
//                new ConditionalConstraint(
//                    aIsNull,
//                    bEquals10,
//                    eIsString)));
//
//        treeRootShouldMatch(
//            new ConstraintNode(
//                new DecisionNode(
//                    new ConstraintNode(
//                        aIsNull,
//                        bEquals10.isFalse()),
//                    new ConstraintNode(
//                        aIsNull.isFalse(),
//                        eIsString.isFalse()))));
//    }
//}

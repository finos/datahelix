//package com.scottlogic.deg.generator.decisiontree;
//
//import com.scottlogic.deg.generator.Field;
//import com.scottlogic.deg.generator.Profile;
//import com.scottlogic.deg.generator.ProfileFields;
//import com.scottlogic.deg.generator.Rule;
//import com.scottlogic.deg.generator.constraints.*;
//import org.hamcrest.core.Is;
//import org.hamcrest.core.IsNull;
//import org.junit.Assert;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.regex.Pattern;
//
//import static com.scottlogic.deg.generator.AssertUtils.pairwiseAssert;
//import static com.scottlogic.deg.generator.decisiontree.DecisionTreeMatchers.isEquivalentTo;
//import static org.hamcrest.CoreMatchers.sameInstance;
//import static org.hamcrest.Matchers.*;
//
//class DecisionTreeGeneratorTests {
//    private final Field fieldA = new Field("A");
//    private final Field fieldB = new Field("B");
//    private final Field fieldC = new Field("C");
//
//    private final List<Rule> rules = new ArrayList<>();
//    private DecisionTreeCollection actualOutput = null;
//
//    private void beforeEach() {
//        rules.clear();
//        actualOutput = null;
//    }
//
//    private void givenRule(IConstraint... constraints) {
//        this.rules.add(new Rule("", Arrays.asList(constraints)));
//    }
//
//    private DecisionTreeCollection getActualOutput() {
//        if (this.actualOutput == null) {
//            Profile testInput = new Profile(
//                new ProfileFields(
//                    Arrays.asList(this.fieldA, this.fieldB, this.fieldC)),
//                this.rules);
//
//            DecisionTreeGenerator testObject = new DecisionTreeGenerator();
//
//            this.actualOutput = testObject.analyse(testInput);
//        }
//
//        return this.actualOutput;
//    }
//
//    private ConstraintNode getResultingRootOption() {
//        return this.getActualOutput().getDecisionTrees().iterator().next()
//            .getRootNode();
//    }
//
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
//            outputRule.getRootNode().getDecisions(), Is.is(IsNull.notNullValue()));
//        Assert.assertThat("Decision tree root has empty list of decisions",
//            outputRule.getRootNode().getDecisions().size(), Is.is(0));
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
//            outputRule.getRootNode().getAtomicConstraints().size(), Is.is(inputConstraints.size()));
//        for (IConstraint constraint : inputConstraints) {
//            Assert.assertThat("Each input constraint is in the decision tree root node atomic constraint list",
//                outputRule.getRootNode().getAtomicConstraints().contains(constraint), Is.is(true));
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
//            Is.is(IsNull.notNullValue()));
//        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
//        Assert.assertThat("Decision tree root has non-null list of decisions",
//            outputRule.getRootNode().getDecisions(), Is.is(IsNull.notNullValue()));
//        Assert.assertThat("Decision tree root has empty list of decisions",
//            outputRule.getRootNode().getDecisions().size(), Is.is(0));
//    }
//
//    @Test
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
//            Is.is(IsNull.notNullValue()));
//        DecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
//        Assert.assertThat("Decision tree root contains correct number of atomic constraints",
//            outputRule.getRootNode().getAtomicConstraints().size(), Is.is(3));
//        Assert.assertThat("Decision tree root atomic constraints list contains constraint 0",
//            outputRule.getRootNode().getAtomicConstraints().contains(constraint0), Is.is(true));
//        Assert.assertThat("Decision tree root atomic constraints list contains constraint 1",
//            outputRule.getRootNode().getAtomicConstraints().contains(constraint1), Is.is(true));
//        Assert.assertThat("Decision tree root atomic constraints list contains constraint 2",
//            outputRule.getRootNode().getAtomicConstraints().contains(constraint2), Is.is(true));
//    }
//
//    @Test
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
//            outputRule.getRootNode().getDecisions().size(), Is.is(2));
//    }
//
//    // checks (A OR B) AND (C OR D)
//    @Test
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
//            outputRule.getRootNode().getAtomicConstraints().size(), Is.is(0));
//    }
//
//    // checks (A OR B) AND (C OR D)
//    @Test
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
//            Is.is(2));
//        List<ConstraintNode> options = new ArrayList<>(decisions.get(0).getOptions());
//        assertOptionContainsSingleConstraint(options.get(0), constraintA);
//        assertOptionContainsSingleConstraint(options.get(1), constraintB);
//        Assert.assertThat("Second decision has correct number of options", decisions.get(1).getOptions().size(),
//            Is.is(2));
//        options = new ArrayList<>(decisions.get(1).getOptions());
//        assertOptionContainsSingleConstraint(options.get(0), constraintC);
//        assertOptionContainsSingleConstraint(options.get(1), constraintD);
//    }
//
//    // Checks (A OR (B AND C)) AND (D OR E)
//    @Test
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
//            options.get(1).getDecisions().size(), Is.is(0));
//        Assert.assertThat("Second option of first decision has two atomic constraints",
//            options.get(1).getAtomicConstraints().size(), Is.is(2));
//        Assert.assertThat("Second option of first decision contains constraint C",
//            options.get(1).getAtomicConstraints().contains(constraintC), Is.is(true));
//        Assert.assertThat("Second option of first decision contains constraint B",
//            options.get(1).getAtomicConstraints().contains(constraintB), Is.is(true));
//        Assert.assertThat("Second decision has two options", decisions.get(1).getOptions().size(), Is.is(2));
//        Assert.assertEquals(2, decisions.get(1).getOptions().size());
//        options = new ArrayList<>(decisions.get(1).getOptions());
//        assertOptionContainsSingleConstraint(options.get(0), constraintD);
//        assertOptionContainsSingleConstraint(options.get(1), constraintE);
//    }
//
//    // Checks IF (A) THEN B ELSE C
//    @Test
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
//            outputRule.getRootNode().getAtomicConstraints().size(), Is.is(0));
//        Assert.assertThat("Decision tree root contains one decision",
//            outputRule.getRootNode().getDecisions().size(), Is.is(1));
//        DecisionNode decision = outputRule.getRootNode().getDecisions().iterator().next();
//        Assert.assertThat("Decision has two options", decision.getOptions().size(), Is.is(2));
//        List<ConstraintNode> options = new ArrayList<>(decision.getOptions());
//        Assert.assertThat("First option contains atomic constraint A",
//            options.get(0).getAtomicConstraints().contains(constraintA), Is.is(true));
//        Assert.assertThat("First option contains atomic constraint B",
//            options.get(0).getAtomicConstraints().contains(constraintB), Is.is(true));
//        Assert.assertThat("Second option contains atomic constraint C",
//            options.get(1).getAtomicConstraints().contains(constraintC), Is.is(true));
//        IConstraint constraint = options.get(1).getAtomicConstraints().iterator().next();
//        Assert.assertThat("First atomic constraint of second option is a NotConstraint",
//            constraint instanceof NotConstraint, Is.is(true));
//        Assert.assertThat("First atomic constraint of second option is a negated version of constraint A",
//            ((NotConstraint) constraint).negatedConstraint, Is.is(constraintA));
//    }
//
//    // Checks IF (A OR B) THEN C
//    @Test
//    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfConditionalConstraintWithNestedOrIsPresent() {
//        IConstraint aEquals10 = new IsEqualToConstantConstraint(fieldA, 10);
//        IConstraint aGreaterThan10 = new IsGreaterThanConstantConstraint(fieldA, 10);
//        IConstraint bGreaterThan20 = new IsGreaterThanConstantConstraint(fieldB, 20);
//
//        givenRule(
//            new ConditionalConstraint(
//                aEquals10.or(aGreaterThan10),
//                bGreaterThan20));
//
//        Assert.assertThat(
//            getResultingRootOption(),
//            isEquivalentTo(
//                new ConstraintNode(
//                    Collections.emptyList(),
//                    Arrays.asList(
//                        new DecisionNode(
//                            /* OPTION 1: AND(C, OR(A, B))  */
//                            new ConstraintNode(
//                                Arrays.asList(bGreaterThan20),
//                                Collections.singleton(
//                                    new DecisionNode(
//                                        new ConstraintNode(aEquals10),
//                                        new ConstraintNode(aGreaterThan10)))),
//                            /* OPTION 2: AND(¬A, ¬B)  */
//                            new ConstraintNode(
//                                new NotConstraint(aEquals10),
//                                new NotConstraint(aGreaterThan10)))))));
//    }
//
//    // NOT (IF A THEN B ELSE C) - edge case
//    @Test
//    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedConditionalConstraintIsPresent() {
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
//            outputRule.getRootNode().getAtomicConstraints().size(), Is.is(0));
//        Assert.assertThat("Decision tree root contains one decision",
//            outputRule.getRootNode().getDecisions().size(), Is.is(1));
//        DecisionNode decision = outputRule.getRootNode().getDecisions().iterator().next();
//        Assert.assertThat("First decision has two options", decision.getOptions().size(), Is.is(2));
//        List<ConstraintNode> options = new ArrayList<>(decision.getOptions());
//        // First option should be A AND (NOT B)
//        Assert.assertThat("First option has two atomic constraints", options.get(0).getAtomicConstraints().size(),
//            Is.is(2));
//        List<IConstraint> constraints = new ArrayList<>(options.get(0).getAtomicConstraints());
//        Assert.assertThat("First atomic constraint of first option is constraint A", constraints.get(0),
//            Is.is(constraintA));
//        Assert.assertThat("Second atomic constraint of first option is a NOT constraint",
//            constraints.get(1) instanceof NotConstraint, Is.is(true));
//        Assert.assertThat("Second atomic constraint of first option is NOT(constraint B)",
//            ((NotConstraint) constraints.get(1)).negatedConstraint, Is.is(constraintB));
//        // Second option should be (NOT A) AND (NOT C)
//        Assert.assertThat("Second option has two atomic constraints", options.get(1).getAtomicConstraints().size(),
//            Is.is(2));
//        Assert.assertThat("Second option has no subdecisions", options.get(1).getDecisions().size(), Is.is(0));
//        constraints = new ArrayList<>(options.get(1).getAtomicConstraints());
//        Assert.assertThat("First atomic constraint of second option is a NOT constraint",
//            constraints.get(0) instanceof NotConstraint, Is.is(true));
//        Assert.assertThat("Second atomic constraint of second option is a NOT constraint",
//            constraints.get(1) instanceof NotConstraint, Is.is(true));
//        Assert.assertThat("First atomic constraint of second option is negated constraint A",
//            ((NotConstraint) constraints.get(0)).negatedConstraint, Is.is(constraintA));
//        Assert.assertThat("Second atomic constraint of second option is negated constraint C",
//            ((NotConstraint) constraints.get(1)).negatedConstraint, Is.is(constraintC));
//    }
//
//    // NOT (IF A THEN B) - other edge case
//    @Test
//    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedConditionalConstraintWithoutElseClauseIsPresent() {
//        // ¬(A -> B)
//        // is equivalent to
//        // A ^ ¬B
//
//        IConstraint aEqualTo10 = new IsEqualToConstantConstraint(fieldA, 10);
//        IConstraint bGreaterThan20 = new IsGreaterThanConstantConstraint(fieldB, 20);
//
//        IConstraint inputRule = new NotConstraint(new ConditionalConstraint(aEqualTo10, bGreaterThan20));
//
//        ConstraintNode expectedOutput = new ConstraintNode(
//            aEqualTo10,
//            bGreaterThan20.isFalse());
//
//        givenRule(inputRule);
//
//        Assert.assertThat(getResultingRootOption(), isEquivalentTo(expectedOutput));
//    }
//
//    // NOT (NOT A)
//    @Test
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
//            outputRule.getRootNode().getAtomicConstraints().size(), Is.is(1));
//        Assert.assertThat("Decision tree root contains no decisions",
//            outputRule.getRootNode().getDecisions().size(), Is.is(0));
//        Assert.assertThat("Atomic constraint of decision tree root is constraint A",
//            outputRule.getRootNode().getAtomicConstraints().contains(constraintA), Is.is(true));
//    }
//
//    // NOT (A AND B)
//    @Test
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
//            outputRule.getRootNode().getAtomicConstraints().size(), Is.is(0));
//        Assert.assertThat("Decision tree root contains one decision",
//            outputRule.getRootNode().getDecisions().size(), Is.is(1));
//        DecisionNode decision = outputRule.getRootNode().getDecisions().iterator().next();
//        Assert.assertThat("Decision has two options", decision.getOptions().size(), Is.is(2));
//        List<ConstraintNode> options = new ArrayList<>(decision.getOptions());
//        Assert.assertThat("First option has one atomic constraint", options.get(0).getAtomicConstraints().size(),
//            Is.is(1));
//        Assert.assertThat("First option has no subdecisions", options.get(0).getDecisions().size(), Is.is(0));
//        IConstraint testConstraint = options.get(0).getAtomicConstraints().iterator().next();
//        Assert.assertThat("Atomic constraint of first option is a NOT constraint",
//            testConstraint instanceof NotConstraint, Is.is(true));
//        Assert.assertThat("Atomic constraint of first option is NOT(Constraint A)",
//            ((NotConstraint) testConstraint).negatedConstraint, Is.is(constraintA));
//        Assert.assertThat("Second option has one atomic constraint", options.get(1).getAtomicConstraints().size(),
//            Is.is(1));
//        Assert.assertThat("Second option has no subdecisions", options.get(1).getDecisions().size(), Is.is(0));
//        testConstraint = options.get(1).getAtomicConstraints().iterator().next();
//        Assert.assertThat("Atomic constraint of second option is a NOT constraint",
//            testConstraint instanceof NotConstraint, Is.is(true));
//        Assert.assertThat("Atomic constraint of second option is NOT(Constraint B)",
//            ((NotConstraint) testConstraint).negatedConstraint, Is.is(constraintB));
//    }
//
//    // (A OR B) OR C
//    @Test
//    void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNestedOrsArePresent() {
//        IConstraint constraintA = new IsEqualToConstantConstraint(fieldA, 10);
//        IConstraint constraintB = new IsGreaterThanConstantConstraint(fieldB, 20);
//        IConstraint constraintC = new IsGreaterThanConstantConstraint(fieldB, 10);
//
//        givenRule(
//            new OrConstraint(
//                new OrConstraint(constraintA, constraintB),
//                constraintC));
//
//        Assert.assertThat(
//            getResultingRootOption(),
//            isEquivalentTo(
//                new ConstraintNode(
//                    Collections.emptyList(),
//                    Arrays.asList(
//                        new DecisionNode(
//                            new ConstraintNode(constraintA),
//                            new ConstraintNode(constraintB),
//                            new ConstraintNode(constraintC))))));
//    }
//
//    private void treeRootShouldMatch(ConstraintNode expected) {
//        ConstraintNode actual = this.getActualOutput().getMergedTree().getRootNode();
//
//        Assert.assertThat(actual, isEquivalentTo(expected));
//    }
//
//    private final IConstraint aIsNull = new IsNullConstraint(new Field("A"));
//    private final IConstraint bEquals10 = new IsEqualToConstantConstraint(new Field("B"), 10);
//    private final IConstraint cIsNumeric = new IsOfTypeConstraint(new Field("C"), IsOfTypeConstraint.Types.Numeric);
//    private final IConstraint dGreaterThan10 = new IsGreaterThanConstantConstraint(new Field("D"), 10);
//    private final IConstraint eIsString = new IsOfTypeConstraint(new Field("E"), IsOfTypeConstraint.Types.String);
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
//
//        new ConstraintNode(
//            new DecisionNode(
//                new ConstraintNode(
//                    aIsNull,
//                    bEquals10.isFalse()),
//                new ConstraintNode(
//                    aIsNull.isFalse(),
//                    eIsString.isFalse())));
//    }
//
//    private void assertOptionContainsSingleConstraint(ConstraintNode option, IConstraint constraint) {
//        Assert.assertThat("Option contains no decisions", option.getDecisions().size(), Is.is(0));
//        Assert.assertThat("Option contains one atomic constraint", option.getAtomicConstraints().size(),
//            Is.is(1));
//        Assert.assertThat("Option's atomic constraint is the given constraint",
//            option.getAtomicConstraints().contains(constraint), Is.is(true));
//        Assert.assertThat(option.getAtomicConstraints(), contains(constraint));
//    }
//
//
//}

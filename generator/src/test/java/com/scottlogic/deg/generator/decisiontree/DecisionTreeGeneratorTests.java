package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Pattern;

import static com.scottlogic.deg.generator.AssertUtils.pairwiseAssert;
import static org.hamcrest.CoreMatchers.sameInstance;

public class DecisionTreeGeneratorTests {
    @Test
    public void shouldReturnAnalysedProfileWithNoAnalysedRules_IfProfileHasNoRules() {
        Profile testInput = new Profile(new ArrayList<>(), new ArrayList<>());
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        Assert.assertEquals(0, testOutput.getDecisionTrees().size());
    }

    @Test
    public void shouldReturnAnalysedProfileWithCorrectFields() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        Profile testInput = new Profile(inputFieldList, new ArrayList<>());
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);
        ProfileFields actualFields = testOutput.getFields();

        pairwiseAssert(
            actualFields,
            inputFieldList,
            (actual, expected) -> Assert.assertThat(actual, sameInstance(expected)));
    }

    @Test
    public void shouldReturnAnalysedRuleWithCorrectDescription() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        Rule testRule = new Rule("test", Arrays.asList(constraint0, constraint1, constraint2));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));

        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);
        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertEquals(testRule.description, outputRule.getDescription());
    }

    @Test
    public void shouldReturnAnalysedRuleWithNoDecisions_IfProfileContainsOnlyAtomicConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        Rule testRule = new Rule("test", Arrays.asList(constraint0, constraint1, constraint2));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));

        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        Assert.assertNotNull(testOutput.getDecisionTrees());
        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertNotNull(outputRule.getRootOption().getDecisions());
        Assert.assertEquals(0, outputRule.getRootOption().getDecisions().size());
    }

    @Test
    public void shouldReturnAnalysedRuleWithAllConstraintsInAtomicConstraintsCollection_IfProfileContainsOnlyAtomicConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        List<IConstraint> inputConstraints = Arrays.asList(constraint0, constraint1, constraint2);
        Rule testRule = new Rule("test", inputConstraints);
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));

        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);
        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertEquals(inputConstraints.size(), outputRule.getRootOption().getAtomicConstraints().size());
        for (IConstraint constraint : inputConstraints) {
            Assert.assertTrue(outputRule.getRootOption().getAtomicConstraints().contains(constraint));
        }
    }

    @Test
    public void shouldReturnAnalysedRuleWithNoDecisions_IfProfileContainsOnlyAtomicConstraintsAndAndConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraint0, constraint1));
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        Rule testRule = new Rule("test", Arrays.asList(andConstraint0, constraint2));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));

        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        Assert.assertNotNull(testOutput.getDecisionTrees());
        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertNotNull(outputRule.getRootOption().getDecisions());
        Assert.assertEquals(0, outputRule.getRootOption().getDecisions().size());
    }

    @Test
    public void shouldReturnAnalysedRuleWithAllAtomicConstraintsInAtomicConstraintsCollection_IfProfileContainsOnlyAtomicConstraintsAndAndConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraint0, constraint1));
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        Rule testRule = new Rule("test", Arrays.asList(andConstraint0, constraint2));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));

        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        Assert.assertNotNull(testOutput.getDecisionTrees());
        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertEquals(3, outputRule.getRootOption().getAtomicConstraints().size());
        Assert.assertTrue(outputRule.getRootOption().getAtomicConstraints().contains(constraint0));
        Assert.assertTrue(outputRule.getRootOption().getAtomicConstraints().contains(constraint1));
        Assert.assertTrue(outputRule.getRootOption().getAtomicConstraints().contains(constraint2));
    }

    @Test
    public void shouldReturnAnalysedRuleWithDecisionForEachOrConstraint() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraint0, constraint1));
        IsEqualToConstantConstraint constraint2 = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam");
        IsEqualToConstantConstraint constraint3 = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel");
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraint2, constraint3));
        Rule testRule = new Rule("test", Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertEquals(2, outputRule.getRootOption().getDecisions().size());
    }

    // checks (A OR B) AND (C OR D)
    @Test
    public void shouldReturnAnalysedRuleWithNoAtomicConstraints_IfAllAtomicConstraintsInProfileAreChildrenOfOrConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
        IsEqualToConstantConstraint constraintC = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam");
        IsEqualToConstantConstraint constraintD = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel");
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintC, constraintD));
        Rule testRule = new Rule("test", Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertEquals(0, outputRule.getRootOption().getAtomicConstraints().size());
    }

    // checks (A OR B) AND (C OR D)
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfAllAtomicConstraintsInProfileAreChildrenOfOrConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
        IsEqualToConstantConstraint constraintC = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam");
        IsEqualToConstantConstraint constraintD = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel");
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintC, constraintD));
        Rule testRule = new Rule("test", Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        List<IRuleDecision> decisions = new ArrayList<>(outputRule.getRootOption().getDecisions());
        Assert.assertEquals(2, decisions.get(0).getOptions().size());
        List<IRuleOption> options = new ArrayList<>(decisions.get(0).getOptions());
        assertOptionContainsSingleConstraint(options.get(0), constraintA);
        assertOptionContainsSingleConstraint(options.get(1), constraintB);
        Assert.assertEquals(2, decisions.get(1).getOptions().size());
        options = new ArrayList<>(decisions.get(1).getOptions());
        assertOptionContainsSingleConstraint(options.get(0), constraintC);
        assertOptionContainsSingleConstraint(options.get(1), constraintD);
    }

    // Checks (A OR (B AND C)) AND (D OR E)
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfAllAtomicConstraintsInProfileAreChildrenOfOrAndAndConstraints() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 5);
        AndConstraint andConstraint0 = new AndConstraint(Arrays.asList(constraintC, constraintB));
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, andConstraint0));
        IsEqualToConstantConstraint constraintD = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam");
        IsEqualToConstantConstraint constraintE = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel");
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(constraintD, constraintE));
        Rule testRule = new Rule("test", Arrays.asList(orConstraint0, orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        List<IRuleDecision> decisions = new ArrayList<>(outputRule.getRootOption().getDecisions());
        Assert.assertEquals(2, decisions.get(0).getOptions().size());
        List<IRuleOption> options = new ArrayList<>(decisions.get(0).getOptions());
        assertOptionContainsSingleConstraint(options.get(0), constraintA);
        Assert.assertEquals(0, options.get(1).getDecisions().size());
        Assert.assertEquals(2, options.get(1).getAtomicConstraints().size());
        Assert.assertTrue(options.get(1).getAtomicConstraints().contains(constraintC));
        Assert.assertTrue(options.get(1).getAtomicConstraints().contains(constraintB));
        Assert.assertEquals(2, decisions.get(1).getOptions().size());
        options = new ArrayList<>(decisions.get(1).getOptions());
        assertOptionContainsSingleConstraint(options.get(0), constraintD);
        assertOptionContainsSingleConstraint(options.get(1), constraintE);
    }

    // Checks IF (A) THEN B ELSE C
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfConditionalConstraintIsPresent() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 10);
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20);
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB, constraintC);
        Rule testRule = new Rule("test", Collections.singletonList(conditionalConstraint));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertEquals(0, outputRule.getRootOption().getAtomicConstraints().size());
        Assert.assertEquals(1, outputRule.getRootOption().getDecisions().size());
        IRuleDecision decision = outputRule.getRootOption().getDecisions().iterator().next();
        Assert.assertEquals(2, decision.getOptions().size());
        List<IRuleOption> options = new ArrayList<>(decision.getOptions());
        Assert.assertTrue(options.get(0).getAtomicConstraints().contains(constraintA));
        Assert.assertTrue(options.get(0).getAtomicConstraints().contains(constraintB));
        Assert.assertTrue(options.get(1).getAtomicConstraints().contains(constraintC));
        IConstraint constraint = options.get(1).getAtomicConstraints().iterator().next();
        Assert.assertTrue(constraint instanceof NotConstraint);
        Assert.assertEquals(constraintA, ((NotConstraint)constraint).negatedConstraint);
    }

    // Checks IF (A OR B) THEN C
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfConditionalConstraintWithNestedOrIsPresent() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20);
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(orConstraint0, constraintC);
        Rule testRule = new Rule("test", Collections.singletonList(conditionalConstraint));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertEquals(0, outputRule.getRootOption().getAtomicConstraints().size());
        Assert.assertEquals(1, outputRule.getRootOption().getDecisions().size());
        // First decision level
        IRuleDecision decision = outputRule.getRootOption().getDecisions().iterator().next();
        Assert.assertEquals(2, decision.getOptions().size());
        List<IRuleOption> options = new ArrayList<>(decision.getOptions());
        // First option: C AND (A OR B)
        Assert.assertEquals(1, options.get(0).getAtomicConstraints().size());
        Assert.assertTrue(options.get(0).getAtomicConstraints().contains(constraintC));
        Assert.assertEquals(1, options.get(0).getDecisions().size());
        IRuleDecision decision2 = options.get(0).getDecisions().iterator().next();
        Assert.assertEquals(2, decision2.getOptions().size());
        List<IRuleOption> optionsNested = new ArrayList<>(decision2.getOptions());
        Assert.assertEquals(1, optionsNested.get(0).getAtomicConstraints().size());
        Assert.assertTrue(optionsNested.get(0).getAtomicConstraints().contains(constraintA));
        Assert.assertEquals(0, optionsNested.get(0).getDecisions().size());
        Assert.assertEquals(1, optionsNested.get(1).getAtomicConstraints().size());
        Assert.assertTrue(optionsNested.get(1).getAtomicConstraints().contains(constraintB));
        Assert.assertEquals(0, optionsNested.get(1).getDecisions().size());
        // Second option: ¬(A OR B) = ¬A AND ¬B
        Assert.assertEquals(2, options.get(1).getAtomicConstraints().size());
        List<IConstraint> constraints = new ArrayList<>(options.get(1).getAtomicConstraints());
        Assert.assertTrue(constraints.get(0) instanceof NotConstraint);
        Assert.assertEquals(constraintA, ((NotConstraint)constraints.get(0)).negatedConstraint);
        Assert.assertTrue(constraints.get(1) instanceof NotConstraint);
        Assert.assertEquals(constraintB, ((NotConstraint)constraints.get(1)).negatedConstraint);
    }

    // NOT (IF A THEN B ELSE C) - edge case
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedConditionalConstraintIsPresent() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20);
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 10);
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB, constraintC);
        NotConstraint notConstraint = new NotConstraint(conditionalConstraint);
        Rule testRule = new Rule("test", Collections.singletonList(notConstraint));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();

        Assert.assertEquals(0, outputRule.getRootOption().getAtomicConstraints().size());
        Assert.assertEquals(1, outputRule.getRootOption().getDecisions().size());
        IRuleDecision decision = outputRule.getRootOption().getDecisions().iterator().next();
        Assert.assertEquals(2, decision.getOptions().size());
        List<IRuleOption> options = new ArrayList<>(decision.getOptions());
        // First option should be A AND (NOT B)
        Assert.assertEquals(2, options.get(0).getAtomicConstraints().size());
        List<IConstraint> constraints = new ArrayList<>(options.get(0).getAtomicConstraints());
        Assert.assertEquals(constraintA, constraints.get(0));
        Assert.assertTrue(constraints.get(1) instanceof NotConstraint);
        Assert.assertEquals(constraintB, ((NotConstraint)constraints.get(1)).negatedConstraint);
        // Second option should be (NOT A) AND (NOT C)
        Assert.assertEquals(2, options.get(1).getAtomicConstraints().size());
        Assert.assertEquals(0, options.get(1).getDecisions().size());
        constraints = new ArrayList<>(options.get(1).getAtomicConstraints());
        Assert.assertTrue(constraints.get(0) instanceof NotConstraint);
        Assert.assertEquals(constraintA, ((NotConstraint)constraints.get(0)).negatedConstraint);
        Assert.assertTrue(constraints.get(1) instanceof NotConstraint);
        Assert.assertEquals(constraintC, ((NotConstraint)constraints.get(1)).negatedConstraint);
    }

    // NOT (IF A THEN B) - other edge case
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedConditionalConstraintWithoutElseClauseIsPresent() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20);
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB);
        NotConstraint notConstraint = new NotConstraint(conditionalConstraint);
        Rule testRule = new Rule("test", Collections.singletonList(notConstraint));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        // No decisions; only A AND (NOT B) constraints.
        Assert.assertEquals(2, outputRule.getRootOption().getAtomicConstraints().size());
        Assert.assertEquals(0, outputRule.getRootOption().getDecisions().size());
        Iterator<IConstraint> constraintIterator = outputRule.getRootOption().getAtomicConstraints().iterator();
        IConstraint testConstraint = constraintIterator.next();
        Assert.assertEquals(constraintA, testConstraint);
        testConstraint = constraintIterator.next();
        Assert.assertTrue(testConstraint instanceof NotConstraint);
        Assert.assertEquals(constraintB, ((NotConstraint)testConstraint).negatedConstraint);
    }

    // NOT (NOT A)
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfDoubleNegationIsPresent() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        NotConstraint notConstraint0 = new NotConstraint(constraintA);
        NotConstraint notConstraint1 = new NotConstraint(notConstraint0);
        Rule testRule = new Rule("test", Collections.singletonList(notConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        // Result should just be A.
        Assert.assertEquals(1, outputRule.getRootOption().getAtomicConstraints().size());
        Assert.assertEquals(0, outputRule.getRootOption().getDecisions().size());
        Assert.assertTrue(outputRule.getRootOption().getAtomicConstraints().contains(constraintA));
    }

    // NOT (A AND B)
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedAndIsPresent() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 5);
        NotConstraint notConstraint = new NotConstraint(new AndConstraint(Arrays.asList(constraintA, constraintB)));
        Rule testRule = new Rule("test", Collections.singletonList(notConstraint));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        // Result should be (NOT A) OR (NOT B)
        Assert.assertEquals(0, outputRule.getRootOption().getAtomicConstraints().size());
        Assert.assertEquals(1, outputRule.getRootOption().getDecisions().size());
        IRuleDecision decision = outputRule.getRootOption().getDecisions().iterator().next();
        Assert.assertEquals(2, decision.getOptions().size());
        List<IRuleOption> options = new ArrayList<>(decision.getOptions());
        Assert.assertEquals(1, options.get(0).getAtomicConstraints().size());
        Assert.assertEquals(0, options.get(0).getDecisions().size());
        IConstraint testConstraint = options.get(0).getAtomicConstraints().iterator().next();
        Assert.assertTrue(testConstraint instanceof NotConstraint);
        Assert.assertEquals(constraintA, ((NotConstraint)testConstraint).negatedConstraint);
        Assert.assertEquals(1, options.get(1).getAtomicConstraints().size());
        Assert.assertEquals(0, options.get(1).getDecisions().size());
        testConstraint = options.get(1).getAtomicConstraints().iterator().next();
        Assert.assertTrue(testConstraint instanceof NotConstraint);
        Assert.assertEquals(constraintB, ((NotConstraint)testConstraint).negatedConstraint);
    }

    // (A OR B) OR C
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNestedOrsArePresent() {
        List<Field> inputFieldList = Arrays.asList(new Field("one"), new Field("two"), new Field("three"));
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20);
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 10);
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(orConstraint0, constraintC));
        Rule testRule = new Rule("test", Collections.singletonList(orConstraint1));
        Profile testInput = new Profile(inputFieldList, Collections.singletonList(testRule));
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        // Result should be 1 decision with 3 options
        Assert.assertEquals(0, outputRule.getRootOption().getAtomicConstraints().size());
        Assert.assertEquals(1, outputRule.getRootOption().getDecisions().size());
        IRuleDecision decision = outputRule.getRootOption().getDecisions().iterator().next();
        Assert.assertEquals(3, decision.getOptions().size());
        List<IRuleOption> options = new ArrayList<>(decision.getOptions());
        Assert.assertEquals(1, options.get(0).getAtomicConstraints().size());
        Assert.assertEquals(0, options.get(0).getDecisions().size());
        Assert.assertTrue(options.get(0).getAtomicConstraints().contains(constraintA));
        Assert.assertEquals(1, options.get(1).getAtomicConstraints().size());
        Assert.assertEquals(0, options.get(1).getDecisions().size());
        Assert.assertTrue(options.get(1).getAtomicConstraints().contains(constraintB));
        Assert.assertEquals(1, options.get(2).getAtomicConstraints().size());
        Assert.assertEquals(0, options.get(2).getDecisions().size());
        Assert.assertTrue(options.get(2).getAtomicConstraints().contains(constraintC));
    }

    private void assertOptionContainsSingleConstraint(IRuleOption option, IConstraint constraint) {
        Assert.assertEquals(0, option.getDecisions().size());
        Assert.assertEquals(1, option.getAtomicConstraints().size());
        Assert.assertTrue(option.getAtomicConstraints().contains(constraint));
    }
}
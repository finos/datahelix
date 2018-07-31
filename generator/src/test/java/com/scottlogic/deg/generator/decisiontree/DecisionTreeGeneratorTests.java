package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

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
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        Profile testInput = new Profile(inputFieldList, new ArrayList<>());
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        Assert.assertEquals(inputFieldList.size(), testOutput.getFields().size());
        ArrayList<Field> fields = new ArrayList<>(testOutput.getFields());
        for (int i = 0; i < inputFieldList.size(); ++i) {
            Assert.assertEquals(inputFieldList.get(i).name, fields.get(i).name);
        }
    }

    @Test
    public void shouldReturnAnalysedRuleWithCorrectDescription() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        inputConstraints.add(constraint0);
        inputConstraints.add(constraint1);
        inputConstraints.add(constraint2);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);

        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);
        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertEquals(testRule.description, outputRule.getDescription());
    }

    @Test
    public void shouldReturnAnalysedRuleWithNoDecisions_IfProfileContainsOnlyAtomicConstraints() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        inputConstraints.add(constraint0);
        inputConstraints.add(constraint1);
        inputConstraints.add(constraint2);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);

        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        Assert.assertNotNull(testOutput.getDecisionTrees());
        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertNotNull(outputRule.getRootOption().getDecisions());
        Assert.assertEquals(0, outputRule.getRootOption().getDecisions().size());
    }

    @Test
    public void shouldReturnAnalysedRuleWithAllConstraintsInAtomicConstraintsCollection_IfProfileContainsOnlyAtomicConstraints() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        inputConstraints.add(constraint0);
        inputConstraints.add(constraint1);
        inputConstraints.add(constraint2);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);

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
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        ArrayList<IConstraint> subConstraints = new ArrayList<>();
        subConstraints.add(constraint0);
        subConstraints.add(constraint1);
        AndConstraint andConstraint0 = new AndConstraint(subConstraints);
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        inputConstraints.add(andConstraint0);
        inputConstraints.add(constraint2);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);

        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        Assert.assertNotNull(testOutput.getDecisionTrees());
        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertNotNull(outputRule.getRootOption().getDecisions());
        Assert.assertEquals(0, outputRule.getRootOption().getDecisions().size());
    }

    @Test
    public void shouldReturnAnalysedRuleWithAllAtomicConstraintsInAtomicConstraintsCollection_IfProfileContainsOnlyAtomicConstraintsAndAndConstraints() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        ArrayList<IConstraint> subConstraints = new ArrayList<>();
        subConstraints.add(constraint0);
        subConstraints.add(constraint1);
        AndConstraint andConstraint0 = new AndConstraint(subConstraints);
        MatchesRegexConstraint constraint2 = new MatchesRegexConstraint(inputFieldList.get(1), Pattern.compile("start.*end"));
        inputConstraints.add(andConstraint0);
        inputConstraints.add(constraint2);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);

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
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        ArrayList<IConstraint> subConstraints = new ArrayList<>();
        subConstraints.add(constraint0);
        subConstraints.add(constraint1);
        OrConstraint orConstraint0 = new OrConstraint(subConstraints);
        IsEqualToConstantConstraint constraint2 = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam");
        IsEqualToConstantConstraint constraint3 = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel");
        subConstraints = new ArrayList<>();
        subConstraints.add(constraint2);
        subConstraints.add(constraint3);
        OrConstraint orConstraint1 = new OrConstraint(subConstraints);
        inputConstraints.add(orConstraint0);
        inputConstraints.add(orConstraint1);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertEquals(inputConstraints.size(), outputRule.getRootOption().getDecisions().size());
    }

    // checks (A OR B) AND (C OR D)
    @Test
    public void shouldReturnAnalysedRuleWithNoAtomicConstraints_IfAllAtomicConstraintsInProfileAreChildrenOfOrConstraints() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        ArrayList<IConstraint> subConstraints = new ArrayList<>();
        subConstraints.add(constraintA);
        subConstraints.add(constraintB);
        OrConstraint orConstraint0 = new OrConstraint(subConstraints);
        IsEqualToConstantConstraint constraintC = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam");
        IsEqualToConstantConstraint constraintD = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel");
        subConstraints = new ArrayList<>();
        subConstraints.add(constraintC);
        subConstraints.add(constraintD);
        OrConstraint orConstraint1 = new OrConstraint(subConstraints);
        inputConstraints.add(orConstraint0);
        inputConstraints.add(orConstraint1);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertEquals(0, outputRule.getRootOption().getAtomicConstraints().size());
    }

    // checks (A OR B) AND (C OR D)
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfAllAtomicConstraintsInProfileAreChildrenOfOrConstraints() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        ArrayList<IConstraint> subConstraints = new ArrayList<>();
        subConstraints.add(constraintA);
        subConstraints.add(constraintB);
        OrConstraint orConstraint0 = new OrConstraint(subConstraints);
        IsEqualToConstantConstraint constraintC = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam");
        IsEqualToConstantConstraint constraintD = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel");
        subConstraints = new ArrayList<>();
        subConstraints.add(constraintC);
        subConstraints.add(constraintD);
        OrConstraint orConstraint1 = new OrConstraint(subConstraints);
        inputConstraints.add(orConstraint0);
        inputConstraints.add(orConstraint1);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Iterator<IRuleDecision> decisionIterator = outputRule.getRootOption().getDecisions().iterator();
        IRuleDecision decision = decisionIterator.next();
        Assert.assertEquals(2, decision.getOptions().size());
        Iterator<IRuleOption> optionIterator = decision.getOptions().iterator();
        IRuleOption option = optionIterator.next();
        assertOptionContainsSingleConstraint(option, constraintA);
        option = optionIterator.next();
        assertOptionContainsSingleConstraint(option, constraintB);
        decision = decisionIterator.next();
        Assert.assertEquals(2, decision.getOptions().size());
        optionIterator = decision.getOptions().iterator();
        option = optionIterator.next();
        assertOptionContainsSingleConstraint(option, constraintC);
        option = optionIterator.next();
        assertOptionContainsSingleConstraint(option, constraintD);
    }

    // Checks (A OR (B AND C)) AND (D OR E)
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfAllAtomicConstraintsInProfileAreChildrenOfOrAndAndConstraints() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 5);
        ArrayList<IConstraint> subConstraints = new ArrayList<>();
        subConstraints.add(constraintC);
        subConstraints.add(constraintB);
        AndConstraint andConstraint0 = new AndConstraint(subConstraints);
        subConstraints = new ArrayList<>();
        subConstraints.add(constraintA);
        subConstraints.add(andConstraint0);
        OrConstraint orConstraint0 = new OrConstraint(subConstraints);
        IsEqualToConstantConstraint constraintD = new IsEqualToConstantConstraint(inputFieldList.get(1), "steam");
        IsEqualToConstantConstraint constraintE = new IsEqualToConstantConstraint(inputFieldList.get(1), "diesel");
        subConstraints = new ArrayList<>();
        subConstraints.add(constraintD);
        subConstraints.add(constraintE);
        OrConstraint orConstraint1 = new OrConstraint(subConstraints);
        inputConstraints.add(orConstraint0);
        inputConstraints.add(orConstraint1);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Iterator<IRuleDecision> decisionIterator = outputRule.getRootOption().getDecisions().iterator();
        IRuleDecision decision = decisionIterator.next();
        Assert.assertEquals(2, decision.getOptions().size());
        Iterator<IRuleOption> optionIterator = decision.getOptions().iterator();
        IRuleOption option = optionIterator.next();
        assertOptionContainsSingleConstraint(option, constraintA);
        option = optionIterator.next();
        Assert.assertEquals(0, option.getDecisions().size());
        Assert.assertEquals(2, option.getAtomicConstraints().size());
        Assert.assertTrue(option.getAtomicConstraints().contains(constraintC));
        Assert.assertTrue(option.getAtomicConstraints().contains(constraintB));
        decision = decisionIterator.next();
        Assert.assertEquals(2, decision.getOptions().size());
        optionIterator = decision.getOptions().iterator();
        option = optionIterator.next();
        assertOptionContainsSingleConstraint(option, constraintD);
        option = optionIterator.next();
        assertOptionContainsSingleConstraint(option, constraintE);
    }

    // Checks IF (A) THEN B ELSE C
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfConditionalConstraintIsPresent() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 10);
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20);
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB, constraintC);
        inputConstraints.add(conditionalConstraint);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertEquals(0, outputRule.getRootOption().getAtomicConstraints().size());
        Assert.assertEquals(1, outputRule.getRootOption().getDecisions().size());
        IRuleDecision decision = outputRule.getRootOption().getDecisions().iterator().next();
        Assert.assertEquals(2, decision.getOptions().size());
        Iterator<IRuleOption> iterator = decision.getOptions().iterator();
        IRuleOption option = iterator.next();
        Assert.assertTrue(option.getAtomicConstraints().contains(constraintA));
        Assert.assertTrue(option.getAtomicConstraints().contains(constraintB));
        option = iterator.next();
        Assert.assertTrue(option.getAtomicConstraints().contains(constraintC));
        IConstraint constraint = option.getAtomicConstraints().iterator().next();
        Assert.assertTrue(constraint instanceof NotConstraint);
        Assert.assertEquals(constraintA, ((NotConstraint)constraint).negatedConstraint);
    }

    // Checks IF (A OR B) THEN C
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfConditionalConstraintWithNestedOrIsPresent() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20);
        List<IConstraint> subConstraints = new ArrayList<>();
        subConstraints.add(constraintA);
        subConstraints.add(constraintB);
        OrConstraint orConstraint0 = new OrConstraint(subConstraints);
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(orConstraint0, constraintC);
        inputConstraints.add(conditionalConstraint);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        Assert.assertEquals(0, outputRule.getRootOption().getAtomicConstraints().size());
        Assert.assertEquals(1, outputRule.getRootOption().getDecisions().size());
        // First decision level
        IRuleDecision decision = outputRule.getRootOption().getDecisions().iterator().next();
        Assert.assertEquals(2, decision.getOptions().size());
        Iterator<IRuleOption> iterator = decision.getOptions().iterator();
        // First option: C AND (A OR B)
        IRuleOption option = iterator.next();
        Assert.assertEquals(1, option.getAtomicConstraints().size());
        Assert.assertTrue(option.getAtomicConstraints().contains(constraintC));
        Assert.assertEquals(1, option.getDecisions().size());
        IRuleDecision decision2 = option.getDecisions().iterator().next();
        Assert.assertEquals(2, decision2.getOptions().size());
        Iterator<IRuleOption> iterator2 = decision2.getOptions().iterator();
        IRuleOption option2 = iterator2.next();
        Assert.assertEquals(1, option2.getAtomicConstraints().size());
        Assert.assertTrue(option2.getAtomicConstraints().contains(constraintA));
        Assert.assertEquals(0, option2.getDecisions().size());
        option2 = iterator2.next();
        Assert.assertEquals(1, option2.getAtomicConstraints().size());
        Assert.assertTrue(option2.getAtomicConstraints().contains(constraintB));
        Assert.assertEquals(0, option2.getDecisions().size());
        // Second option: ¬(A OR B) = ¬A AND ¬B
        option = iterator.next();
        Assert.assertEquals(2, option.getAtomicConstraints().size());
        Iterator<IConstraint> constraintIterator = option.getAtomicConstraints().iterator();
        IConstraint constraint = constraintIterator.next();
        Assert.assertTrue(constraint instanceof NotConstraint);
        Assert.assertEquals(constraintA, ((NotConstraint)constraint).negatedConstraint);
        constraint = constraintIterator.next();
        Assert.assertTrue(constraint instanceof NotConstraint);
        Assert.assertEquals(constraintB, ((NotConstraint)constraint).negatedConstraint);
    }

    // NOT (IF A THEN B ELSE C) - edge case
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedConditionalConstraintIsPresent() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20);
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 10);
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB, constraintC);
        NotConstraint notConstraint = new NotConstraint(conditionalConstraint);
        inputConstraints.add(notConstraint);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();

        Assert.assertEquals(0, outputRule.getRootOption().getAtomicConstraints().size());
        Assert.assertEquals(1, outputRule.getRootOption().getDecisions().size());
        IRuleDecision decision = outputRule.getRootOption().getDecisions().iterator().next();
        Assert.assertEquals(2, decision.getOptions().size());
        Iterator<IRuleOption> iterator = decision.getOptions().iterator();
        // First option should be A AND (NOT B)
        IRuleOption option = iterator.next();
        Assert.assertEquals(2, option.getAtomicConstraints().size());
        Iterator<IConstraint> constraintIterator = option.getAtomicConstraints().iterator();
        IConstraint testConstraint = constraintIterator.next();
        Assert.assertEquals(constraintA, testConstraint);
        testConstraint = constraintIterator.next();
        Assert.assertTrue(testConstraint instanceof NotConstraint);
        Assert.assertEquals(constraintB, ((NotConstraint)testConstraint).negatedConstraint);
        // Second option should be (NOT A) AND (NOT C)
        option = iterator.next();
        Assert.assertEquals(2, option.getAtomicConstraints().size());
        Assert.assertEquals(0, option.getDecisions().size());
        constraintIterator = option.getAtomicConstraints().iterator();
        testConstraint = constraintIterator.next();
        Assert.assertTrue(testConstraint instanceof NotConstraint);
        Assert.assertEquals(constraintA, ((NotConstraint)testConstraint).negatedConstraint);
        testConstraint = constraintIterator.next();
        Assert.assertTrue(testConstraint instanceof NotConstraint);
        Assert.assertEquals(constraintC, ((NotConstraint)testConstraint).negatedConstraint);
    }

    // NOT (IF A THEN B) - other edge case
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNegatedConditionalConstraintWithoutElseClauseIsPresent() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20);
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraintA, constraintB);
        NotConstraint notConstraint = new NotConstraint(conditionalConstraint);
        inputConstraints.add(notConstraint);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);
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
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        NotConstraint notConstraint0 = new NotConstraint(constraintA);
        NotConstraint notConstraint1 = new NotConstraint(notConstraint0);
        inputConstraints.add(notConstraint1);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);
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
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 5);
        NotConstraint notConstraint = new NotConstraint(new AndConstraint(Arrays.asList(constraintA, constraintB)));
        inputConstraints.add(notConstraint);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        // Result should be (NOT A) OR (NOT B)
        Assert.assertEquals(0, outputRule.getRootOption().getAtomicConstraints().size());
        Assert.assertEquals(1, outputRule.getRootOption().getDecisions().size());
        IRuleDecision decision = outputRule.getRootOption().getDecisions().iterator().next();
        Assert.assertEquals(2, decision.getOptions().size());
        Iterator<IRuleOption> iterator = decision.getOptions().iterator();
        IRuleOption option = iterator.next();
        Assert.assertEquals(1, option.getAtomicConstraints().size());
        Assert.assertEquals(0, option.getDecisions().size());
        IConstraint testConstraint = option.getAtomicConstraints().iterator().next();
        Assert.assertTrue(testConstraint instanceof NotConstraint);
        Assert.assertEquals(constraintA, ((NotConstraint)testConstraint).negatedConstraint);
        option = iterator.next();
        Assert.assertEquals(1, option.getAtomicConstraints().size());
        Assert.assertEquals(0, option.getDecisions().size());
        testConstraint = option.getAtomicConstraints().iterator().next();
        Assert.assertTrue(testConstraint instanceof NotConstraint);
        Assert.assertEquals(constraintB, ((NotConstraint)testConstraint).negatedConstraint);
    }

    // (A OR B) OR C
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfNestedOrsArePresent() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraintA = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraintB = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20);
        IsGreaterThanConstantConstraint constraintC = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 10);
        OrConstraint orConstraint0 = new OrConstraint(Arrays.asList(constraintA, constraintB));
        OrConstraint orConstraint1 = new OrConstraint(Arrays.asList(orConstraint0, constraintC));
        inputConstraints.add(orConstraint1);
        Rule testRule = new Rule("test", inputConstraints);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(testRule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        DecisionTreeGenerator testObject = new DecisionTreeGenerator();

        IDecisionTreeProfile testOutput = testObject.analyse(testInput);

        IRuleDecisionTree outputRule = testOutput.getDecisionTrees().iterator().next();
        // Result should be 1 decision with 3 options
        Assert.assertEquals(0, outputRule.getRootOption().getAtomicConstraints().size());
        Assert.assertEquals(1, outputRule.getRootOption().getDecisions().size());
        IRuleDecision decision = outputRule.getRootOption().getDecisions().iterator().next();
        Assert.assertEquals(3, decision.getOptions().size());
        Iterator<IRuleOption> iterator = decision.getOptions().iterator();
        IRuleOption option = iterator.next();
        Assert.assertEquals(1, option.getAtomicConstraints().size());
        Assert.assertEquals(0, option.getDecisions().size());
        Assert.assertTrue(option.getAtomicConstraints().contains(constraintA));
        option = iterator.next();
        Assert.assertEquals(1, option.getAtomicConstraints().size());
        Assert.assertEquals(0, option.getDecisions().size());
        Assert.assertTrue(option.getAtomicConstraints().contains(constraintB));
        option = iterator.next();
        Assert.assertEquals(1, option.getAtomicConstraints().size());
        Assert.assertEquals(0, option.getDecisions().size());
        Assert.assertTrue(option.getAtomicConstraints().contains(constraintC));
    }

    private void assertOptionContainsSingleConstraint(IRuleOption option, IConstraint constraint) {
        Assert.assertEquals(0, option.getDecisions().size());
        Assert.assertEquals(1, option.getAtomicConstraints().size());
        Assert.assertTrue(option.getAtomicConstraints().contains(constraint));
    }
}
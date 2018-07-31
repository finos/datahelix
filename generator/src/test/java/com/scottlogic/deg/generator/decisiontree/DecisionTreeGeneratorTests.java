package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        Iterator<IRuleDecision> decisionIterator = outputRule.getRootOption().getDecisions().iterator();
        IRuleDecision decision = decisionIterator.next();
        Assert.assertEquals(2, decision.getOptions().size());
        Iterator<IRuleOption> optionIterator = decision.getOptions().iterator();
        IRuleOption option = optionIterator.next();
        assertOptionContainsSingleConstraint(option, constraint0);
        option = optionIterator.next();
        assertOptionContainsSingleConstraint(option, constraint1);
        decision = decisionIterator.next();
        Assert.assertEquals(2, decision.getOptions().size());
        optionIterator = decision.getOptions().iterator();
        option = optionIterator.next();
        assertOptionContainsSingleConstraint(option, constraint2);
        option = optionIterator.next();
        assertOptionContainsSingleConstraint(option, constraint3);
    }

    // Checks (A OR (B AND C)) AND (D OR E)
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfAllAtomicConstraintsInProfileAreChildrenOfOrAndAndConstraints() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 0);
        IsGreaterThanConstantConstraint constraint4 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 5);
        ArrayList<IConstraint> subConstraints = new ArrayList<>();
        subConstraints.add(constraint4);
        subConstraints.add(constraint1);
        AndConstraint andConstraint0 = new AndConstraint(subConstraints);
        subConstraints = new ArrayList<>();
        subConstraints.add(constraint0);
        subConstraints.add(andConstraint0);
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
        Iterator<IRuleDecision> decisionIterator = outputRule.getRootOption().getDecisions().iterator();
        IRuleDecision decision = decisionIterator.next();
        Assert.assertEquals(2, decision.getOptions().size());
        Iterator<IRuleOption> optionIterator = decision.getOptions().iterator();
        IRuleOption option = optionIterator.next();
        assertOptionContainsSingleConstraint(option, constraint0);
        option = optionIterator.next();
        Assert.assertEquals(0, option.getDecisions().size());
        Assert.assertEquals(2, option.getAtomicConstraints().size());
        Assert.assertTrue(option.getAtomicConstraints().contains(constraint4));
        Assert.assertTrue(option.getAtomicConstraints().contains(constraint1));
        decision = decisionIterator.next();
        Assert.assertEquals(2, decision.getOptions().size());
        optionIterator = decision.getOptions().iterator();
        option = optionIterator.next();
        assertOptionContainsSingleConstraint(option, constraint2);
        option = optionIterator.next();
        assertOptionContainsSingleConstraint(option, constraint3);
    }

    // Checks IF (A) THEN B ELSE C
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfConditionalConstraintIsPresent() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 10);
        IsGreaterThanConstantConstraint constraint2 = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20);
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(constraint0, constraint1, constraint2);
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
        Assert.assertTrue(option.getAtomicConstraints().contains(constraint0));
        Assert.assertTrue(option.getAtomicConstraints().contains(constraint1));
        option = iterator.next();
        Assert.assertTrue(option.getAtomicConstraints().contains(constraint2));
        IConstraint constraint = option.getAtomicConstraints().iterator().next();
        Assert.assertTrue(constraint instanceof NotConstraint);
        Assert.assertEquals(constraint0, ((NotConstraint)constraint).negatedConstraint);
    }

    // Checks IF (A OR B) THEN C
    @Test
    public void shouldReturnAnalysedRuleWithCorrectDecisionStructure_IfConditionalConstraintWithNestedOrIsPresent() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        ArrayList<IConstraint> inputConstraints = new ArrayList<>();
        IsEqualToConstantConstraint constraint0 = new IsEqualToConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(inputFieldList.get(0), 10);
        IsGreaterThanConstantConstraint constraint2 = new IsGreaterThanConstantConstraint(inputFieldList.get(1), 20);
        List<IConstraint> subConstraints = new ArrayList<>();
        subConstraints.add(constraint0);
        subConstraints.add(constraint1);
        OrConstraint orConstraint0 = new OrConstraint(subConstraints);
        ConditionalConstraint conditionalConstraint = new ConditionalConstraint(orConstraint0, constraint2);
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
        Assert.assertTrue(option.getAtomicConstraints().contains(constraint2));
        Assert.assertEquals(1, option.getDecisions().size());
        IRuleDecision decision2 = option.getDecisions().iterator().next();
        Assert.assertEquals(2, decision2.getOptions().size());
        Iterator<IRuleOption> iterator2 = decision2.getOptions().iterator();
        IRuleOption option2 = iterator2.next();
        Assert.assertEquals(1, option2.getAtomicConstraints().size());
        Assert.assertTrue(option2.getAtomicConstraints().contains(constraint0));
        Assert.assertEquals(0, option2.getDecisions().size());
        option2 = iterator2.next();
        Assert.assertEquals(1, option2.getAtomicConstraints().size());
        Assert.assertTrue(option2.getAtomicConstraints().contains(constraint1));
        Assert.assertEquals(0, option2.getDecisions().size());
        // Second option: ¬(A OR B) = ¬A AND ¬B
        option = iterator.next();
        Assert.assertEquals(2, option.getAtomicConstraints().size());
        Iterator<IConstraint> constraintIterator = option.getAtomicConstraints().iterator();
        IConstraint constraint = constraintIterator.next();
        Assert.assertTrue(constraint instanceof NotConstraint);
        Assert.assertEquals(constraint0, ((NotConstraint)constraint).negatedConstraint);
        constraint = constraintIterator.next();
        Assert.assertTrue(constraint instanceof NotConstraint);
        Assert.assertEquals(constraint1, ((NotConstraint)constraint).negatedConstraint);
    }

    private void assertOptionContainsSingleConstraint(IRuleOption option, IConstraint constraint) {
        Assert.assertEquals(0, option.getDecisions().size());
        Assert.assertEquals(1, option.getAtomicConstraints().size());
        Assert.assertTrue(option.getAtomicConstraints().contains(constraint));
    }
}
package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.*;
import org.hamcrest.collection.IsIn;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ProfileAnalyserTests {
    @Test
    public void shouldReturnAnalysedProfileWithNoAnalysedRules_IfProfileHasNoRules() {
        Profile testInput = new Profile(new ArrayList<>(), new ArrayList<>());
        ProfileAnalyser testObject = new ProfileAnalyser();

        IAnalysedProfile testOutput = testObject.analyse(testInput);

        Assert.assertEquals(0, testOutput.getAnalysedRules().size());
    }

    @Test
    public void shouldReturnAnalysedProfileWithCorrectFields() {
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(new Field("one"));
        inputFieldList.add(new Field("two"));
        inputFieldList.add(new Field("three"));
        Profile testInput = new Profile(inputFieldList, new ArrayList<>());
        ProfileAnalyser testObject = new ProfileAnalyser();

        IAnalysedProfile testOutput = testObject.analyse(testInput);

        Assert.assertEquals(inputFieldList.size(), testOutput.getFields().size());
        ArrayList<Field> fields = new ArrayList<>(testOutput.getFields());
        for (int i = 0; i < inputFieldList.size(); ++i) {
            Assert.assertEquals(inputFieldList.get(i).name, fields.get(i).name);
        }
    }

    @Test
    public void shouldReturnTreeWithRootNodeOnly_IfProfileContainsOneAtomicConstraint() {
        Field field = new Field("test");
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(field);
        IsEqualToConstantConstraint constraint = new IsEqualToConstantConstraint(field, "0");
        ArrayList<IConstraint> constraintList = new ArrayList<>();
        constraintList.add(constraint);
        Rule rule = new Rule("test", constraintList);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(rule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        ProfileAnalyser testObject = new ProfileAnalyser();

        IAnalysedProfile testOutput = testObject.analyse(testInput);

        AnalysedRule outputRule = testOutput.getAnalysedRules().iterator().next();
        Assert.assertNotNull(outputRule.constraintRoot);
        Assert.assertNotNull(outputRule.constraintRoot.getChildNodes());
        Assert.assertEquals(0, outputRule.constraintRoot.getChildNodes().size());
    }

    @Test
    public void shouldReturnTreeWithIsEqualToConstraintInRootNodeAtomicConstraintsCollection() {
        Field field = new Field("test");
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(field);
        IsEqualToConstantConstraint constraint = new IsEqualToConstantConstraint(field, "0");
        ArrayList<IConstraint> constraintList = new ArrayList<>();
        constraintList.add(constraint);
        Rule rule = new Rule("test", constraintList);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(rule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        ProfileAnalyser testObject = new ProfileAnalyser();

        IAnalysedProfile testOutput = testObject.analyse(testInput);

        AnalysedRule outputRule = testOutput.getAnalysedRules().iterator().next();
        Assert.assertNotNull(outputRule.constraintRoot.getAtomicConstraints());
        Assert.assertEquals(1, outputRule.constraintRoot.getAtomicConstraints().size());
        Assert.assertEquals(constraint, outputRule.constraintRoot.getAtomicConstraints().iterator().next());
    }

    @Test
    public void shouldReturnTreeWithIsGreaterThanConstraintInRootNodeAtomicConstraintsCollection() {
        Field field = new Field("test");
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(field);
        IsGreaterThanConstantConstraint constraint = new IsGreaterThanConstantConstraint(field, 0);
        ArrayList<IConstraint> constraintList = new ArrayList<>();
        constraintList.add(constraint);
        Rule rule = new Rule("test", constraintList);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(rule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        ProfileAnalyser testObject = new ProfileAnalyser();

        IAnalysedProfile testOutput = testObject.analyse(testInput);

        AnalysedRule outputRule = testOutput.getAnalysedRules().iterator().next();
        Assert.assertNotNull(outputRule.constraintRoot.getAtomicConstraints());
        Assert.assertEquals(1, outputRule.constraintRoot.getAtomicConstraints().size());
        Assert.assertEquals(constraint, outputRule.constraintRoot.getAtomicConstraints().iterator().next());
    }

    @Test
    public void shouldReturnTreeWithRootNodeOnly_IfRuleConsistsSolelyOfAtomicConstraints() {
        Field field = new Field("test");
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(field);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(field, 0);
        IsGreaterThanConstantConstraint constraint2 = new IsGreaterThanConstantConstraint(field, 5);
        IsEqualToConstantConstraint constraint3 = new IsEqualToConstantConstraint(field, 8);
        ArrayList<IConstraint> constraintList = new ArrayList<>();
        constraintList.add(constraint1);
        constraintList.add(constraint2);
        constraintList.add(constraint3);

        Rule rule = new Rule("test", constraintList);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(rule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        ProfileAnalyser testObject = new ProfileAnalyser();

        IAnalysedProfile testOutput = testObject.analyse(testInput);

        AnalysedRule outputRule = testOutput.getAnalysedRules().iterator().next();
        Assert.assertNotNull(outputRule.constraintRoot);
        Assert.assertNotNull(outputRule.constraintRoot.getChildNodes());
        Assert.assertEquals(0, outputRule.constraintRoot.getChildNodes().size());
    }

    @Test
    public void shouldReturnTreeWithAtomicConstraintsInRootNode_IfRuleConsistsSolelyOfAtomicConstraints() {
        Field field = new Field("test");
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(field);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(field, 0);
        IsGreaterThanConstantConstraint constraint2 = new IsGreaterThanConstantConstraint(field, 5);
        IsEqualToConstantConstraint constraint3 = new IsEqualToConstantConstraint(field, 8);
        ArrayList<IConstraint> constraintList = new ArrayList<>();
        constraintList.add(constraint1);
        constraintList.add(constraint2);
        constraintList.add(constraint3);

        Rule rule = new Rule("test", constraintList);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(rule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        ProfileAnalyser testObject = new ProfileAnalyser();

        IAnalysedProfile testOutput = testObject.analyse(testInput);

        AnalysedRule outputRule = testOutput.getAnalysedRules().iterator().next();
        Assert.assertNotNull(outputRule.constraintRoot);
        Assert.assertNotNull(outputRule.constraintRoot.getAtomicConstraints());
        ArrayList<IConstraint> atomicConstraints = new ArrayList<>(outputRule.constraintRoot.getAtomicConstraints());
        Assert.assertEquals(3, atomicConstraints.size());
        Assert.assertTrue(atomicConstraints.contains(constraint1));
        Assert.assertTrue(atomicConstraints.contains(constraint2));
        Assert.assertTrue(atomicConstraints.contains(constraint3));
    }

    @Test
    public void shouldReturnTreeWithRootNodeOnly_IfRuleConsistsSolelyOfAtomicConstraintsAndAndConstraints() {
        Field field = new Field("test");
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(field);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(field, 0);
        IsGreaterThanConstantConstraint constraint2 = new IsGreaterThanConstantConstraint(field, 5);
        IsEqualToConstantConstraint constraint3 = new IsEqualToConstantConstraint(field, 8);
        ArrayList<IConstraint> subConstraintList = new ArrayList<>();
        subConstraintList.add(constraint2);
        subConstraintList.add(constraint3);
        AndConstraint andConstraint = new AndConstraint(subConstraintList);
        ArrayList<IConstraint> constraintList = new ArrayList<>();
        constraintList.add(constraint1);
        constraintList.add(andConstraint);

        Rule rule = new Rule("test", constraintList);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(rule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        ProfileAnalyser testObject = new ProfileAnalyser();

        IAnalysedProfile testOutput = testObject.analyse(testInput);

        AnalysedRule outputRule = testOutput.getAnalysedRules().iterator().next();
        Assert.assertNotNull(outputRule.constraintRoot);
        Assert.assertNotNull(outputRule.constraintRoot.getChildNodes());
        Assert.assertEquals(0, outputRule.constraintRoot.getChildNodes().size());
    }

    @Test
    public void shouldReturnTreeWithAtomicConstraintsInRootNode_IfRuleConsistsSolelyOfAtomicConstraintsAndAndConstraints() {
        Field field = new Field("test");
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(field);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(field, 0);
        IsGreaterThanConstantConstraint constraint2 = new IsGreaterThanConstantConstraint(field, 5);
        IsEqualToConstantConstraint constraint3 = new IsEqualToConstantConstraint(field, 8);
        ArrayList<IConstraint> subConstraintList = new ArrayList<>();
        subConstraintList.add(constraint2);
        subConstraintList.add(constraint3);
        AndConstraint andConstraint = new AndConstraint(subConstraintList);
        ArrayList<IConstraint> constraintList = new ArrayList<>();
        constraintList.add(constraint1);
        constraintList.add(andConstraint);

        Rule rule = new Rule("test", constraintList);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(rule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        ProfileAnalyser testObject = new ProfileAnalyser();

        IAnalysedProfile testOutput = testObject.analyse(testInput);

        AnalysedRule outputRule = testOutput.getAnalysedRules().iterator().next();
        Assert.assertNotNull(outputRule.constraintRoot);
        Assert.assertNotNull(outputRule.constraintRoot.getAtomicConstraints());
        ArrayList<IConstraint> atomicConstraints = new ArrayList<>(outputRule.constraintRoot.getAtomicConstraints());
        Assert.assertEquals(3, atomicConstraints.size());
        Assert.assertTrue(atomicConstraints.contains(constraint1));
        Assert.assertTrue(atomicConstraints.contains(constraint2));
        Assert.assertTrue(atomicConstraints.contains(constraint3));
    }

    @Test
    public void shouldReturnTreeWithRootNodeOnly_IfRuleConsistsSolelyOfAtomicConstraintsAndNestedAndConstraints() {
        Field field = new Field("test");
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(field);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(field, 0);
        IsGreaterThanConstantConstraint constraint2 = new IsGreaterThanConstantConstraint(field, 5);
        IsEqualToConstantConstraint constraint3 = new IsEqualToConstantConstraint(field, 8);
        IsGreaterThanConstantConstraint constraint4 = new IsGreaterThanConstantConstraint(field, 7);
        ArrayList<IConstraint> subConstraintList = new ArrayList<>();
        subConstraintList.add(constraint2);
        subConstraintList.add(constraint3);
        AndConstraint andConstraint1 = new AndConstraint(subConstraintList);
        subConstraintList = new ArrayList<>();
        subConstraintList.add(constraint1);
        subConstraintList.add(constraint4);
        AndConstraint andConstraint2 = new AndConstraint(subConstraintList);
        subConstraintList = new ArrayList<>();
        subConstraintList.add(andConstraint1);
        subConstraintList.add(andConstraint2);
        AndConstraint andConstraint0 = new AndConstraint(subConstraintList);
        ArrayList<IConstraint> constraintList = new ArrayList<>();
        constraintList.add(andConstraint0);
        Rule rule = new Rule("test", constraintList);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(rule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        ProfileAnalyser testObject = new ProfileAnalyser();

        IAnalysedProfile testOutput = testObject.analyse(testInput);

        AnalysedRule outputRule = testOutput.getAnalysedRules().iterator().next();
        Assert.assertNotNull(outputRule.constraintRoot);
        Assert.assertNotNull(outputRule.constraintRoot.getChildNodes());
        Assert.assertEquals(0, outputRule.constraintRoot.getChildNodes().size());
    }

    @Test
    public void shouldReturnTreeWitAtomicConstraintsInRootNode_IfRuleConsistsSolelyOfAtomicConstraintsAndNestedAndConstraints() {
        Field field = new Field("test");
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(field);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(field, 0);
        IsGreaterThanConstantConstraint constraint2 = new IsGreaterThanConstantConstraint(field, 5);
        IsEqualToConstantConstraint constraint3 = new IsEqualToConstantConstraint(field, 8);
        IsGreaterThanConstantConstraint constraint4 = new IsGreaterThanConstantConstraint(field, 7);
        ArrayList<IConstraint> subConstraintList = new ArrayList<>();
        subConstraintList.add(constraint2);
        subConstraintList.add(constraint3);
        AndConstraint andConstraint1 = new AndConstraint(subConstraintList);
        subConstraintList = new ArrayList<>();
        subConstraintList.add(constraint1);
        subConstraintList.add(constraint4);
        AndConstraint andConstraint2 = new AndConstraint(subConstraintList);
        subConstraintList = new ArrayList<>();
        subConstraintList.add(andConstraint1);
        subConstraintList.add(andConstraint2);
        AndConstraint andConstraint0 = new AndConstraint(subConstraintList);
        ArrayList<IConstraint> constraintList = new ArrayList<>();
        constraintList.add(andConstraint0);
        Rule rule = new Rule("test", constraintList);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(rule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        ProfileAnalyser testObject = new ProfileAnalyser();

        IAnalysedProfile testOutput = testObject.analyse(testInput);

        AnalysedRule outputRule = testOutput.getAnalysedRules().iterator().next();
        Assert.assertNotNull(outputRule.constraintRoot);
        Assert.assertNotNull(outputRule.constraintRoot.getAtomicConstraints());
        ArrayList<IConstraint> atomicConstraints = new ArrayList<>(outputRule.constraintRoot.getAtomicConstraints());
        Assert.assertEquals(4, atomicConstraints.size());
        Assert.assertTrue(atomicConstraints.contains(constraint1));
        Assert.assertTrue(atomicConstraints.contains(constraint2));
        Assert.assertTrue(atomicConstraints.contains(constraint3));
        Assert.assertTrue(atomicConstraints.contains(constraint4));
    }

    @Test
    public void shouldReturnTreeWithOneChildNode_IfRuleContainsOneOrConstraint() {
        Field field = new Field("test");
        ArrayList<Field> inputFieldList = new ArrayList<>();
        inputFieldList.add(field);
        IsGreaterThanConstantConstraint constraint1 = new IsGreaterThanConstantConstraint(field, 0);
        IsEqualToConstantConstraint constraint2 = new IsEqualToConstantConstraint(field, 5);
        IsEqualToConstantConstraint constraint3 = new IsEqualToConstantConstraint(field, 8);
        ArrayList<IConstraint> subConstraintList = new ArrayList<>();
        subConstraintList.add(constraint2);
        subConstraintList.add(constraint3);
        OrConstraint orConstraint1 = new OrConstraint(subConstraintList);
        ArrayList<IConstraint> constraintList = new ArrayList<>();
        constraintList.add(constraint1);
        constraintList.add(orConstraint1);
        Rule rule = new Rule("test", constraintList);
        ArrayList<Rule> ruleList = new ArrayList<>();
        ruleList.add(rule);
        Profile testInput = new Profile(inputFieldList, ruleList);
        ProfileAnalyser testObject = new ProfileAnalyser();

        IAnalysedProfile testOutput = testObject.analyse(testInput);

        AnalysedRule outputRule = testOutput.getAnalysedRules().iterator().next();
        Assert.assertNotNull(outputRule.constraintRoot.getChildNodes());
        Assert.assertEquals(2, outputRule.constraintRoot.getChildNodes().size());
        Assert.assertEquals(0, outputRule.constraintRoot.getChildNodes().iterator().next().getChildNodes().size());
    }
}
package com.scottlogic.deg.generator.inputs.profileviolation;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.NotConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.OrConstraint;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class TypeEqualityHelper {
    /**
     * Asserts all profiles in two lists have constraints and all sub-constraints of the same type.
     * @param expectedProfileList The expected profile list.
     * @param actualProfileList The actual profile list.
     */
    public static void assertListProfileTypeEquality(List<Profile> expectedProfileList, List<Profile> actualProfileList) {
        for (int i = 0; i < expectedProfileList.size(); i++) {
            assertProfileTypeEquality(expectedProfileList.get(i), actualProfileList.get(i));
        }
    }

    /**
     * Asserts all rules within two profiles have constraints and all sub-constraints of the same type.
     * @param expectedProfile The expected profile.
     * @param actualProfile The actual profile.
     */
    public static void assertProfileTypeEquality(Profile expectedProfile, Profile actualProfile) {
        ArrayList<Rule> expectedRules = new ArrayList<>(expectedProfile.rules);
        ArrayList<Rule> actualRules = new ArrayList<>(actualProfile.rules);
        for (int i = 0; i < expectedRules.size(); i++) {
            assertRuleTypeEquality(expectedRules.get(i), actualRules.get(i));
        }
    }

    /**
     * Asserts all constraints in two rules have the same type and all sub-constraints of these constraints have the
     * same type.
     * @param expectedRule The expected rule.
     * @param actualRule The actual rule
     */
    public static void assertRuleTypeEquality(Rule expectedRule, Rule actualRule) {
        ArrayList<Constraint> expectedConstraints = new ArrayList<>(expectedRule.constraints);
        ArrayList<Constraint> actualConstraints = new ArrayList<>(actualRule.constraints);
        for (int i = 0; i < expectedConstraints.size(); i++) {
            assertConstraintTypeEquality(expectedConstraints.get(i), actualConstraints.get(i));
        }
    }

    /**
     * Asserts deep type equality for constraints that can contain other constraints.
     * For example asserts that an AND(OR(X,Y),OR(Z,W)) is not the same as AND(OR(X,Y),AND(Z,W))
     * but cannot distinguish between constraints A:(foo less than 10) and B:(bar less than 50)
     * @param expectedConstraint The expected constraint.
     * @param actualConstraint The constraint under test.
     */
    public static void assertConstraintTypeEquality(Constraint expectedConstraint, Constraint actualConstraint) {
        if (expectedConstraint == null && actualConstraint == null) {
            return;
        }
        Assert.assertEquals("Class types do not match for constraints. Expected: "
                + expectedConstraint.getClass() + " but was: " + actualConstraint.getClass(),
            expectedConstraint.getClass(),
            actualConstraint.getClass());
        if (expectedConstraint instanceof NotConstraint){
            assertConstraintTypeEquality(
                ((NotConstraint)expectedConstraint).negatedConstraint,
                ((NotConstraint)actualConstraint).negatedConstraint);
        }
        else if (expectedConstraint instanceof AndConstraint) {
            ArrayList<Constraint> expectedConstraints = new ArrayList<>(((AndConstraint) expectedConstraint).subConstraints);
            ArrayList<Constraint> actualConstraints = new ArrayList<>(((AndConstraint) actualConstraint).subConstraints);
            for (int i = 0; i < ((AndConstraint) expectedConstraint).subConstraints.size(); i++) {
                assertConstraintTypeEquality(
                    expectedConstraints.get(i),
                    actualConstraints.get(i));
            }
        }
        else if (expectedConstraint instanceof OrConstraint) {
            ArrayList<Constraint> expectedConstraints = new ArrayList<>(((OrConstraint) expectedConstraint).subConstraints);
            ArrayList<Constraint> actualConstraints = new ArrayList<>(((OrConstraint) actualConstraint).subConstraints);
            for (int i = 0; i < ((OrConstraint) expectedConstraint).subConstraints.size(); i++) {
                assertConstraintTypeEquality(
                    expectedConstraints.get(i),
                    actualConstraints.get(i));
            }
        }
        else if (expectedConstraint instanceof ConditionalConstraint) {
            ConditionalConstraint expectedConditionalConstraint = (ConditionalConstraint) expectedConstraint;
            ConditionalConstraint actualConditionalConstraint = (ConditionalConstraint) actualConstraint;
            assertConstraintTypeEquality(
                expectedConditionalConstraint.condition,
                actualConditionalConstraint.condition);
            assertConstraintTypeEquality(
                expectedConditionalConstraint.whenConditionIsFalse,
                actualConditionalConstraint.whenConditionIsFalse);
            assertConstraintTypeEquality(
                expectedConditionalConstraint.whenConditionIsTrue,
                actualConditionalConstraint.whenConditionIsTrue);
        }
    }
}

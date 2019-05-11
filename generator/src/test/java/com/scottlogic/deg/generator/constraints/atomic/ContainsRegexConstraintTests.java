package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.equalTo;

public class ContainsRegexConstraintTests {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        ContainsRegexConstraint constraint1 = new ContainsRegexConstraint(field1, Pattern.compile("[abc]"), rules());
        ContainsRegexConstraint constraint2 = new ContainsRegexConstraint(field2, Pattern.compile("[abc]"), rules());
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    @Test
    public void testConstraintIsNotEqualDueToField() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField2");
        ContainsRegexConstraint constraint1 = new ContainsRegexConstraint(field1, Pattern.compile("[abc]"), rules());
        ContainsRegexConstraint constraint2 = new ContainsRegexConstraint(field2, Pattern.compile("[abc]"), rules());
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualDueToValue() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        ContainsRegexConstraint constraint1 = new ContainsRegexConstraint(field1, Pattern.compile("[abc]"), rules());
        ContainsRegexConstraint constraint2 = new ContainsRegexConstraint(field2, Pattern.compile("[abcd]"), rules());
        Assert.assertNotEquals(constraint1, constraint2);
    }

    private static Set<RuleInformation> rules(){
        return Collections.singleton(new RuleInformation("rules"));
    }
}

package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.hamcrest.Matchers.equalTo;

public class ContainsRegexConstraintTest {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        ContainsRegexConstraint constraint1 = new ContainsRegexConstraint(field1, Pattern.compile("[abc]"));
        ContainsRegexConstraint constraint2 = new ContainsRegexConstraint(field2, Pattern.compile("[abc]"));
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    @Test
    public void testConstraintIsNotEqualDueToField() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField2");
        ContainsRegexConstraint constraint1 = new ContainsRegexConstraint(field1, Pattern.compile("[abc]"));
        ContainsRegexConstraint constraint2 = new ContainsRegexConstraint(field2, Pattern.compile("[abc]"));
        Assert.assertNotEquals(constraint1, constraint2);
    }

    @Test
    public void testConstraintIsNotEqualDueToValue() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");
        ContainsRegexConstraint constraint1 = new ContainsRegexConstraint(field1, Pattern.compile("[abc]"));
        ContainsRegexConstraint constraint2 = new ContainsRegexConstraint(field2, Pattern.compile("[abcd]"));
        Assert.assertNotEquals(constraint1, constraint2);
    }
}

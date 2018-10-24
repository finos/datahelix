package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class AndConstraintTest {

    @Test
    public void testConstraintIsEqual() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");

        Field field3 = new Field("TestField");
        Field field4 = new Field("TestField");
        AndConstraint constraint1 = new AndConstraint(new IsNullConstraint(field1), new IsNullConstraint(field2));
        AndConstraint constraint2 = new AndConstraint(new IsNullConstraint(field3), new IsNullConstraint(field4));
        Assert.assertThat(constraint1, equalTo(constraint2));
    }

    @Test
    public void testConstraintIsEqualRecursively() {
        Field field1 = new Field("TestField");
        Field field2 = new Field("TestField");

        Field field3 = new Field("TestField");
        Field field4 = new Field("TestField");
        AndConstraint constraint1 = new AndConstraint(new AndConstraint(new IsNullConstraint(field1), new IsNullConstraint(field2)));
        AndConstraint constraint2 = new AndConstraint(new AndConstraint(new IsNullConstraint(field3), new IsNullConstraint(field4)));
        Assert.assertThat(constraint1, equalTo(constraint2));
    }
//
//    @Test
//    public void testConstraintIsEqualRecursivelySameLevel() {
//        Field field1 = new Field("TestField");
//        Field field2 = new Field("TestField");
//        NotConstraint constraint1 = new NotConstraint(new NotConstraint(new NotConstraint(new IsNullConstraint(field1))));
//        NotConstraint constraint2 = new NotConstraint(new NotConstraint(new NotConstraint(new IsNullConstraint(field2))));
//        Assert.assertThat(constraint1, equalTo(constraint2));
//    }
//
//    @Test
//    public void testConstraintIsNotEqualDueToField() {
//        Field field1 = new Field("TestField");
//        Field field2 = new Field("TestField2");
//        NotConstraint constraint1 = new NotConstraint(new IsNullConstraint(field1));
//        NotConstraint constraint2 = new NotConstraint(new IsNullConstraint(field2));
//        Assert.assertNotEquals(constraint1, constraint2);
//    }
//
//    @Test
//    public void testConstraintIsNotEqualDueToValue() {
//        Field field1 = new Field("TestField");
//        Field field2 = new Field("TestField");
//        NotConstraint constraint1 = new NotConstraint(new IsEqualToConstantConstraint(field1, "abc"));
//        NotConstraint constraint2 = new NotConstraint(new IsEqualToConstantConstraint(field2, "abcd"));
//        Assert.assertNotEquals(constraint1, constraint2);
//    }
//
//    @Test
//    public void testConstraintIsNotEqualRecursively() {
//        Field field1 = new Field("TestField");
//        Field field2 = new Field("TestField");
//        NotConstraint constraint1 = new NotConstraint(new IsNullConstraint(field1));
//        NotConstraint constraint2 = new NotConstraint(new NotConstraint(new IsNullConstraint(field2)));
//        Assert.assertNotEquals(constraint1, constraint2);
//    }

}

package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.schemas.v3.RuleDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

public class IsInSetConstraintTests {

    @Test
    public void testConstraintThrowsIfGivenEmptySet(){
        Field field1 = new Field("TestField");

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new IsInSetConstraint(field1, Collections.emptySet(), rules()));
    }

    @Test
    public void testConstraintThrowsIfGivenNullInASet(){
        Field field1 = new Field("TestField");

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new IsInSetConstraint(field1, Collections.singleton(null), rules()));
    }

    @Test
    public void testConstraintThrowsNothingIfGivenAValidSet(){
        Field field1 = new Field("TestField");
        Assertions.assertDoesNotThrow(
            () -> new IsInSetConstraint(field1, Collections.singleton("foo"), rules()));
    }

    private static Set<RuleInformation> rules(){
        RuleDTO rule = new RuleDTO();
        rule.rule = "rules";
        return Collections.singleton(new RuleInformation(rule));
    }
}

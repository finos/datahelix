package com.scottlogic.deg.generator.decisiontree.rule_strategy;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsEqualToConstantConstraint;

import java.util.HashMap;
import java.util.Map;

public class Combination {
    Map<Field, IsEqualToConstantConstraint> combinations = new HashMap<>();

    public  Map<Field, IsEqualToConstantConstraint> getCombinations() { return combinations; }

    public void add(Field field, Object o){
        combinations.put(field, new IsEqualToConstantConstraint(field, o));
    }

}

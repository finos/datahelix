package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsEqualToConstantConstraint;

import java.util.HashMap;
import java.util.Map;

public class Combination {
    private Map<Field, Object> combinations = new HashMap<>();

    public  Map<Field, Object> getCombinations() { return combinations; }

    public void add(Field field, Object o){
        combinations.put(field, o);
    }

}

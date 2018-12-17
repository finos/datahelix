package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.Field;

import java.util.HashMap;
import java.util.Map;

public class Combination {
    private Map<Field, DataValue> combinations = new HashMap<>();

    public  Map<Field, DataValue> getCombinations() { return combinations; }

    public void add(Field field, DataValue o){
        combinations.put(field, o);
    }

}

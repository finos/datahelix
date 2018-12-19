package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.Field;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Combination {
    private Map<Field, DataValue> combinations = new HashMap<>();

    public  Map<Field, DataValue> getCombinations() { return combinations; }

    public void add(Field field, DataValue o){
        combinations.put(field, o);
    }

    @Override
    public String toString(){
        return "Combination: " + this.getCombinations().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Combination other = ((Combination) o);
        return Objects.equals(other.getCombinations(), combinations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(combinations.keySet());
    }
}

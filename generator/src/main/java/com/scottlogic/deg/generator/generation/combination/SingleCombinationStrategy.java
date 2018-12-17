package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.restrictions.FieldSpec;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SingleCombinationStrategy implements CombinationStrategy {

    @Override
    public List<Combination> getCombinations(Map<Field, Stream<Object>> generatedData, Map<Field, FieldSpec> dataSources) {
        Combination combo = new Combination();
        generatedData.forEach((k, v) -> {
            combo.add(k, new DataValue(v.findFirst().get(), dataSources.get(k)));
        });
        return Collections.singletonList(combo);
    }
}

package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.restrictions.FieldSpec;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SingleCombinationStrategy implements CombinationStrategy {

    @Override
    public List<Combination> getCombinations(Map<Field, Stream<Object>> generatedData, Map<Field, FieldSpec> dataSources) {
        Combination combo = new Combination();
        generatedData.forEach((field, valueStream) -> {
            Iterator iterator = valueStream.iterator();
            if (!iterator.hasNext()) {
                throw new RuntimeException("No values for field " + field + ", field spec: " + dataSources.get(field));
            }
            combo.add(field, new DataValue(iterator.next(), dataSources.get(field)));
        });
        return Collections.singletonList(combo);
    }
}

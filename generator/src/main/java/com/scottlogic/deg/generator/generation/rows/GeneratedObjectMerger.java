package com.scottlogic.deg.generator.generation.rows;

import com.scottlogic.deg.generator.Value;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.FlatMappingSpliterator;

import java.util.*;


public class GeneratedObjectMerger {
    public static final Row empty = new Row(new HashMap<>());

    public static Row merge(Row... bags) {
        Map<Field, Value> newFieldToValue = new HashMap<>();

        FlatMappingSpliterator.flatMap(Arrays.stream(bags)
            .map(r -> r.getFieldToValue().entrySet().stream()),
            entrySetStream -> entrySetStream)
            .forEach(entry -> {
                if (newFieldToValue.containsKey(entry.getKey()))
                    throw new IllegalArgumentException("Databags can't be merged because they overlap on field " + entry.getKey().name);

                newFieldToValue.put(entry.getKey(), entry.getValue());
            });

        return new Row(newFieldToValue);
    }
}

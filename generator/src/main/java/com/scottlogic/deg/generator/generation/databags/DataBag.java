package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.common.output.DataBagValue;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.*;
import com.scottlogic.deg.common.output.GeneratedObject;

import java.util.*;


public class DataBag extends GeneratedObject {
    public static final DataBag empty = new DataBag(new HashMap<>());
    public DataBag(Map<Field, DataBagValue> fieldToValue) {
        super(fieldToValue);
    }

    public static DataBag merge(DataBag... bags) {
        Map<Field, DataBagValue> newFieldToValue = new HashMap<>();

        FlatMappingSpliterator.flatMap(Arrays.stream(bags)
            .map(r -> r.getFieldToValue().entrySet().stream()),
            entrySetStream -> entrySetStream)
            .forEach(entry -> {
                if (newFieldToValue.containsKey(entry.getKey()))
                    throw new IllegalArgumentException("Databags can't be merged because they overlap on field " + entry.getKey().name);

                newFieldToValue.put(entry.getKey(), entry.getValue());
            });

        return new DataBag(newFieldToValue);
    }
}

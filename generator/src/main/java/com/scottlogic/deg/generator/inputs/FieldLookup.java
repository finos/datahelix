package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.Field;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class FieldLookup {
    private final Map<String, Field> idToField;

    FieldLookup(Collection<Field> allFields) {
        this.idToField = new HashMap<>();

        for (Field field : allFields) {
            this.idToField.put(field.name, field);
        }
    }

    Field byId(String id) {
        return this.idToField.get(id);
    }
}

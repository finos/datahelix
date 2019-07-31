package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.common.profile.Field;

import java.util.List;

public class FieldGroup {

    private final List<Field> fields;

    public FieldGroup(List<Field> fields) {
        this.fields = fields;
    }

    public List<Field> fields() {
        return fields;
    }

}

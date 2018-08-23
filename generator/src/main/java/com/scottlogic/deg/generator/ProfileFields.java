package com.scottlogic.deg.generator;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class ProfileFields implements Iterable<Field> {
    private final List<Field> fields;

    public ProfileFields(List<Field> fields) {
        this.fields = fields;
    }

    public Field getByName(String fieldName) {
        return this.fields.stream()
            .filter(f -> f.name.equals(fieldName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Profile fields do not contain " + fieldName));
    }

    public int size() {
        return this.fields.size();
    }

    @Override
    public Iterator<Field> iterator() {
        return fields.iterator();
    }

    public Stream<Field> stream() {
        return this.fields.stream();
    }
}

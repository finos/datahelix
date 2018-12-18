package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.inputs.validation.ProfileVisitor;
import com.scottlogic.deg.generator.inputs.validation.VisitableProfileElement;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class ProfileFields implements Iterable<Field>, VisitableProfileElement {
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

    @Override
    public void accept(ProfileVisitor visitor) {
        visitor.visit(this);
    }
}

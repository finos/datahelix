package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;

import java.util.*;
import java.util.stream.Collectors;

public class ReductiveState {

    private final ProfileFields fields;
    private final Map<Field, DataBagValue> fieldValues;

    public ReductiveState(ProfileFields fields) {
        this(fields, new HashMap<>());
    }

    private ReductiveState(
        ProfileFields fields,
        Map<Field, DataBagValue> fieldValues) {
        this.fields = fields;
        this.fieldValues = fieldValues;
    }

    public ReductiveState withFixedFieldValue(DataBagValue value) {
        Map<Field, DataBagValue> newFixedFieldsMap = new HashMap<>(fieldValues);
        newFixedFieldsMap.put(value.getField(), value);

        return new ReductiveState(fields, newFixedFieldsMap);
    }

    public boolean allFieldsAreFixed() {
        return fieldValues.size() == fields.size();
    }

    public boolean isFieldFixed(Field field) {
        return fieldValues.containsKey(field);
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public Map<Field, DataBagValue> getFieldValues() {
        return this.fieldValues;
    }

    public String toString(boolean detailAllFields) {
        return fieldValues.size() > 10 && !detailAllFields
            ? String.format("Fixed fields: %d of %d", this.fieldValues.size(), this.fields.size())
            : String.join(", ", this.fieldValues.values()
            .stream()
            .sorted(Comparator.comparing(ff -> ff.getValue().toString()))
            .map(DataBagValue::toString)
            .collect(Collectors.toList()));
    }

    public ProfileFields getFields() {
        return this.fields;
    }
}

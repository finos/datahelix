package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;

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

    public ReductiveState withFixedFieldValue(Field field, DataBagValue value) {
        Map<Field, DataBagValue> newFixedFieldsMap = new HashMap<>(fieldValues);
        newFixedFieldsMap.put(field, value);

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
        if (fieldValues.size() > 10 && !detailAllFields){
            return String.format("Fixed fields: %d of %d", this.fieldValues.size(), this.fields.size());
        }

        return String.join(", ", fieldValues.entrySet()
            .stream()
            .sorted(Comparator.comparing(ff -> ff.getKey().toString()))
            .map(ff -> String.format("%s: %s", ff.getKey(), ff.getValue().getFormattedValue()))
            .collect(Collectors.toList()));
    }

    public ProfileFields getFields() {
        return this.fields;
    }

    public DataBag asDataBag() {
        return new DataBag(fieldValues);
    }
}

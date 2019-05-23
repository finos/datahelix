package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FieldValue;

import java.util.*;
import java.util.stream.Collectors;

public class ReductiveState {

    private final ProfileFields fields;
    private final Map<Field, FieldValue> fieldValues;

    public ReductiveState(ProfileFields fields) {
        this(fields, new HashMap<>());
    }

    private ReductiveState(
        ProfileFields fields,
        Map<Field, FieldValue> fieldValues) {
        this.fields = fields;
        this.fieldValues = fieldValues;
    }

    public ReductiveState withFixedFieldValue(FieldValue value) {
        Map<Field, FieldValue> newFixedFieldsMap = new HashMap<>(fieldValues);
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

    public Map<Field, FieldValue> getFieldValues() {
        return this.fieldValues;
    }

    public String toString(boolean detailAllFields) {
        if (fieldValues.size() > 10 && !detailAllFields){
            return String.format("Fixed fields: %d of %d", this.fieldValues.size(), this.fields.size());
        }

        return String.join(", ", this.fieldValues.values()
            .stream()
            .sorted(Comparator.comparing(ff -> ff.getField().toString()))
            .map(ff -> String.format("%s: %s", ff.getField(), ff.getDataBagValue().getFormattedValue()))
            .collect(Collectors.toList()));
    }

    public ProfileFields getFields() {
        return this.fields;
    }

    public DataBag asDataBag() {
        return new DataBag(fieldValues.values().stream().collect(Collectors.toMap(
            FieldValue::getField,
            FieldValue::getDataBagValue)));
    }
}

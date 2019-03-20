package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FieldValue;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReductiveState {

    private final ProfileFields fields;
    private final Map<Field, FieldValue> fieldValues;
    private final FixedField nextFieldToFix;

    public ReductiveState(ProfileFields fields) {
        this(fields, new HashMap<>(), null);
    }

    private ReductiveState(
        ProfileFields fields,
        Map<Field, FieldValue> fieldValues,
        FixedField nextFieldToFix) {
        this.fields = fields;
        this.fieldValues = fieldValues;
        this.nextFieldToFix = nextFieldToFix;
    }

    public boolean allFieldsAreFixed() {
        return fieldValues.size() == fields.size();
    }

    public boolean isFieldFixed(Field field) {
        return fieldValues.containsKey(field);
    }

    //get a stream of all possible values for the field that was fixed on the last iteration
    public Stream<Object> getValuesFromNextFieldToFix() {
        if (this.nextFieldToFix == null)
            throw new NullPointerException("Field has not been fixed yet");

        return this.nextFieldToFix.getStream();
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public Map<Field, FieldValue> getFieldValues() {
        return this.fieldValues;
    }

    public FixedField getNextFieldToFix() {
        return this.nextFieldToFix;
    }

    public String toString(boolean detailAllFields) {
        String fixedFieldsString = this.fieldValues.size() > 10 && !detailAllFields
            ? String.format("Fixed fields: %d of %d", this.fieldValues.size(), this.fields.size())
            : String.join(", ", this.fieldValues.values()
            .stream()
            .sorted(Comparator.comparing(ff -> ff.getValue().toString()))
            .map(FieldValue::toString)
            .collect(Collectors.toList()));

        if (this.nextFieldToFix == null) {
            return fixedFieldsString;
        }

        return this.fieldValues.isEmpty()
            ? this.nextFieldToFix.toString()
            : this.nextFieldToFix.toString() + " & " + fixedFieldsString;
    }

    public ProfileFields getFields() {
        return this.fields;
    }

    public ReductiveState withNextFieldToFixChosen(FixedField nextFieldToFix) {
        if (this.nextFieldToFix != null) {
            throw new UnsupportedOperationException();
        }

        return new ReductiveState(fields, fieldValues, nextFieldToFix);
    }

    public ReductiveState withCurrentFieldFixedToValue(Object value) {
        if (nextFieldToFix == null) {
            throw new UnsupportedOperationException();
        }

        Map<Field, FieldValue> newFixedFieldsMap = new HashMap<>(fieldValues);
        newFixedFieldsMap.put(nextFieldToFix.getField(), new FieldValue(nextFieldToFix.getField(), value));

        return new ReductiveState(fields, newFixedFieldsMap, null);
    }
}

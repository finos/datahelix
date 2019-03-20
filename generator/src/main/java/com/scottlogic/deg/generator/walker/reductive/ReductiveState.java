package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReductiveState {

    private final ProfileFields fields;
    private final Map<Field, FixedField> fixedFields;
    private final FixedField nextFieldToFix;

    public ReductiveState(ProfileFields fields) {
        this(fields, new HashMap<>(), null);
    }

    private ReductiveState(
        ProfileFields fields,
        Map<Field, FixedField> fixedFields,
        FixedField nextFieldToFix) {
        this.fields = fields;
        this.fixedFields = fixedFields;
        this.nextFieldToFix = nextFieldToFix;
    }

    public boolean allFieldsAreFixed() {
        return fixedFields.size() == fields.size();
    }

    public boolean isFieldFixed(Field field) {
        return fixedFields.containsKey(field);
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

    public Map<Field, FixedField> getFixedFields() {
        return this.fixedFields;
    }

    public FixedField getNextFieldToFix() {
        return this.nextFieldToFix;
    }

    public String toString(boolean detailAllFields) {
        String fixedFieldsString = this.fixedFields.size() > 10 && !detailAllFields
            ? String.format("Fixed fields: %d of %d", this.fixedFields.size(), this.fields.size())
            : String.join(", ", this.fixedFields.values()
            .stream()
            .sorted(Comparator.comparing(ff -> ff.getField().name))
            .map(FixedField::toString)
            .collect(Collectors.toList()));

        if (this.nextFieldToFix == null) {
            return fixedFieldsString;
        }

        return this.fixedFields.isEmpty()
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

        return new ReductiveState(fields, fixedFields, nextFieldToFix);
    }

    public ReductiveState whereCurrentFieldIsFixed() {
        if (nextFieldToFix == null) {
            throw new UnsupportedOperationException();
        }

        Map<Field, FixedField> newFixedFieldsMap = new HashMap<>(fixedFields);
        newFixedFieldsMap.put(nextFieldToFix.getField(), nextFieldToFix);

        return new ReductiveState(fields, newFixedFieldsMap, null);
    }

    public ReductiveState with(FixedField fixedField) {

        if (nextFieldToFix == null)
            return withNextFieldToFixChosen(fixedField);

        return whereCurrentFieldIsFixed().withNextFieldToFixChosen(fixedField);
    }
}

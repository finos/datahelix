package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReductiveState {

    private final ProfileFields fields;
    private final Map<Field, FixedField> fixedFields;
    private final FixedField lastFixedField;

    public ReductiveState(ProfileFields fields){
        this(fields, new HashMap<>(), null);
    }

    private ReductiveState(
        ProfileFields fields,
        Map<Field, FixedField> fixedFields,
        FixedField lastFixedField) {
        this.fields = fields;
        this.fixedFields = fixedFields;
        this.lastFixedField = lastFixedField;
    }

    public boolean allValuesAreFixed() {
        return this.lastFixedField.hasValueSet() && this.fixedFields.size() == this.fields.size() - 1;
    }

    public boolean allFieldsAreFixed() {
        int noOfFixedFields = this.lastFixedField == null
            ? this.fixedFields.size()
            : this.fixedFields.size() + 1;

        return noOfFixedFields == this.fields.size();
    }

    public boolean isFieldFixed(Field field) {
        return getFixedField(field) != null;
    }

    //get a stream of all possible values for the field that was fixed on the last iteration
    public Stream<Object> getValuesFromLastFixedField(){
        if (this.lastFixedField == null)
            throw new NullPointerException("No field has been fixed yet");

        return this.lastFixedField.getStream();
    }

    //get a copy of the current fixed field for the given field, will return null if the field isn't fixed
    public FixedField getFixedField(Field field) {
        if (lastFixedField != null && lastFixedField.field.equals(field)){
            return lastFixedField;
        }

        return this.fixedFields.getOrDefault(field, null);
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public Map<Field, FixedField> getFixedFieldsExceptLast(){
        return this.fixedFields;
    }

    FixedField getLastFixedField(){
        return this.lastFixedField;
    }

    public Set<Field> getUnfixedFields(){
        return this.fields.stream()
            .filter(f -> !this.fixedFields.containsKey(f) && !f.equals(this.lastFixedField.field))
            .collect(Collectors.toSet());
    }

    public String toString(boolean detailAllFields) {
        String fixedFieldsString = this.fixedFields.size() > 10 && !detailAllFields
            ? String.format("Fixed fields: %d of %d", this.fixedFields.size(), this.fields.size())
            : String.join(", ", this.fixedFields.values()
                .stream()
                .sorted(Comparator.comparing(ff -> ff.field.name))
                .map(FixedField::toString)
                .collect(Collectors.toList()));

        if (this.lastFixedField == null) {
            return fixedFieldsString;
        }

        return this.fixedFields.isEmpty()
            ? this.lastFixedField.toString()
            : this.lastFixedField.toString() + " & " + fixedFieldsString;
    }

    public ProfileFields getFields() {
        return this.fields;
    }

    public ReductiveState with(FixedField fixedField){

        if (lastFixedField == null)
            return new ReductiveState(fields, new HashMap<>(), fixedField);

        Map<Field, FixedField> newFixedFieldsMap = Stream.concat
            (fixedFields.values().stream(), Stream.of(lastFixedField))
            .collect(Collectors.toMap(x->x.field, Function.identity()));

        return new ReductiveState(fields, newFixedFieldsMap, fixedField);
    }

    public ReductiveState with(Deque<FixedField> fixedFields){
        FixedField current = fixedFields.pop();
        return new ReductiveState(
            fields,
            fixedFields.stream().collect(Collectors.toMap(ff -> ff.field, ff -> ff)),
            current);
    }
}

package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReductiveState {

    private final Profile profile;
    private final Map<Field, FixedField> fixedFields;
    private final FixedField lastFixedField;

    public ReductiveState(Profile profile){
        this(profile, new HashMap<>(), null);
    }

    private ReductiveState(
        Profile profile,
        Map<Field, FixedField> fixedFields,
        FixedField lastFixedField) {
        this.profile = profile;
        this.fixedFields = fixedFields;
        this.lastFixedField = lastFixedField;
    }

    public boolean allFieldsAreFixed() {
        int noOfFixedFields = lastFixedField == null
            ? fixedFields.size()
            : fixedFields.size() + 1;

        return noOfFixedFields == profile.fields.size();
    }

    public boolean isFieldFixed(Field field) {
        return getFixedField(field) != null;
    }

    //get a stream of all possible values for the field that was fixed on the last iteration
    public Stream<Object> getValuesFromLastFixedField(){
        if (lastFixedField == null)
            throw new NullPointerException("Field has not been fixed yet");

        return lastFixedField.getStream();
    }

    //get a copy of the current fixed field for the given field, will return null if the field isn't fixed
    public FixedField getFixedField(Field field) {
        if (lastFixedField != null && lastFixedField.getField().equals(field)){
            return lastFixedField;
        }

        return fixedFields.getOrDefault(field, null);
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public Map<Field, FixedField> getFixedFieldsExceptLast(){
        return fixedFields;
    }

    public FixedField getLastFixedField(){
        return lastFixedField;
    }

    public Set<Field> getUnfixedFields(){
        return profile.fields.stream()
            .filter(f -> !fixedFields.containsKey(f) && !f.equals(lastFixedField.getField()))
            .collect(Collectors.toSet());
    }

    public String toString(boolean detailAllFields) {
        String fixedFieldsString = fixedFields.size() > 10 && !detailAllFields
            ? String.format("Fixed fields: %d of %d", fixedFields.size(), profile.fields.size())
            : String.join(", ", fixedFields.values()
                .stream()
                .sorted(Comparator.comparing(ff -> ff.getField().name))
                .map(FixedField::toString)
                .collect(Collectors.toList()));

        if (lastFixedField == null) {
            return fixedFieldsString;
        }

        return fixedFields.isEmpty()
            ? lastFixedField.toString()
            : lastFixedField.toString() + " & " + fixedFieldsString;
    }
    
    public Profile getProfile() {
        return profile;
    }
    
    public ReductiveState with(FixedField fixedField){

        if (lastFixedField == null)
            return new ReductiveState(profile, new HashMap<>(), fixedField);

        Map<Field, FixedField> newFixedFieldsMap = Stream.concat
            (fixedFields.values().stream(), Stream.of(lastFixedField))
            .collect(Collectors.toMap(x->x.getField(), Function.identity()));

        return new ReductiveState(profile, newFixedFieldsMap, fixedField);
    }
}

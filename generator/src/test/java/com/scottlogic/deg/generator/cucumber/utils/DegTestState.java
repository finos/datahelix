package com.scottlogic.deg.generator.cucumber.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.inputs.IConstraintReader;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.MainConstraintReader;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DegTestState {

    public GenerationConfig.DataGenerationType generationStrategy;
    final List<Field> profileFields = new ArrayList<>();
    final List<IConstraint> constraints = new ArrayList<>();
    final List<Exception> testExceptions = new ArrayList<>();

    public void addConstraint(String fieldName, String constraintName, Object value) throws Exception {
        ConstraintDTO dto = this.createConstraint(fieldName, constraintName, value);
        this.addConstraintToList(dto);
    }

    public void addNotConstraint(String fieldName, String constraintName, Object value) throws Exception {
        ConstraintDTO notDto = new ConstraintDTO();
        notDto.not = this.createConstraint(fieldName, constraintName, value);
        this.addConstraintToList(notDto);
    }

    public void addConstraintsFromJson(String constraintProfile) throws IOException, InvalidProfileException {
        IConstraintReader reader = new MainConstraintReader();
        ProfileFields pf = new ProfileFields(this.profileFields);
        ConstraintHolder holder = this.deserialise(constraintProfile);
        for(ConstraintDTO cd : holder.constraints){
            this.constraints.add(reader.apply(cd, pf));
        }
    }

    public void clearState(){
        this.profileFields.clear();
        this.constraints.clear();
        this.testExceptions.clear();
    }

    public void addField(String fieldName) {
        this.profileFields.add(new Field(fieldName));
    }

    public void addException(Exception e){
        this.testExceptions.add(e);
    }

    private ConstraintDTO createConstraint(String fieldName, String constraintName, Object value) {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = fieldName;
        dto.is = this.extractConstraint(constraintName);
        if (value != null){
            if (value instanceof Collection){
                dto.values = (Collection<Object>) value;
            } else {
                dto.value = value;
            }
        }
        return dto;
    }

    private String extractConstraint(String gherkinConstraint) {
        List<String> allConstraints = Arrays.asList(gherkinConstraint.split(" "));
        return allConstraints.get(0) + allConstraints
            .stream()
            .skip(1)
            .map(value -> value.substring(0, 1).toUpperCase() + value.substring(1))
            .collect(Collectors.joining());
    }

    private void addConstraintToList(ConstraintDTO constraintDTO) throws Exception {
        IConstraint constraint = new MainConstraintReader().apply(
            constraintDTO,
            new ProfileFields(this.profileFields));
        this.constraints.add(constraint);
    }

    private ConstraintHolder deserialise(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        return mapper.readerFor(ConstraintHolder.class).readValue(json);
    }
}

class ConstraintHolder {
    public Collection<ConstraintDTO> constraints;
    public ConstraintHolder(){}
}

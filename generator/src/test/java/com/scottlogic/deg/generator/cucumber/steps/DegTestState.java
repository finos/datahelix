package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.inputs.MainConstraintReader;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;

import java.util.*;
import java.util.stream.Collectors;

public class DegTestState {
    final List<Field> profileFields = new ArrayList<>();
    final List<IConstraint> constraints = new ArrayList<>();
    TestCaseGenerationResult generationResult;

    public void addConstraint(String fieldName, String constraintName, Object value) throws Exception {
        ConstraintDTO dto = this.createConstraint(fieldName, constraintName, value);
        this.addConstraintToList(dto);
    }

    public void addNotConstraint(String fieldName, String constraintName, Object value) throws Exception {
        ConstraintDTO notDto = new ConstraintDTO();
        notDto.not = this.createConstraint(fieldName, constraintName, value);
        this.addConstraintToList(notDto);
    }

    private ConstraintDTO createConstraint(String fieldName, String constraintName, Object value) {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = fieldName;
        dto.is = this.extractConstraint(constraintName);
        if (value != null){
            if (value instanceof Collection){
                dto.values = (Collection) value;
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

}

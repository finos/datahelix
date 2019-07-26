/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.config.detail.CombinationStrategyType;
import com.scottlogic.deg.generator.config.detail.DataGenerationType;
import com.scottlogic.deg.generator.config.detail.TreeWalkerType;
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;
import com.scottlogic.deg.profile.v0_1.ConstraintDTO;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to represent the state during cucumber test running and execution
 */
public class CucumberTestState {
    public DataGenerationType dataGenerationType = DataGenerationType.FULL_SEQUENTIAL;
    public CombinationStrategyType combinationStrategyType = CombinationStrategyType.PINNING;
    public TreeWalkerType walkerType = TreeWalkerType.DECISION_BASED;

    /**
     * Boolean to represent if the generation mode is validating or violating.
     * If true, generation is in violate mode.
     */
    public Boolean shouldViolate;
    // Sets or unsets flag --allow-untyped-fields
    boolean requireFieldTyping;

    /** If true, we inject a no-op generation engine during the test (e.g. because we're just testing profile validation) */
    private Boolean shouldSkipGeneration;
    private int maxStringLength = 200;

    Boolean shouldSkipGeneration() { return shouldSkipGeneration; }
    void disableGeneration() { shouldSkipGeneration = true; }

    public long maxRows = 200;

    Boolean generationHasAlreadyOccured;

    List<List<Object>> generatedObjects;
    List<Field> profileFields;
    List<ConstraintDTO> constraints;
    List<Exception> testExceptions;

    private final List<AtomicConstraintType> contstraintsToNotViolate = new ArrayList<>();

    public CucumberTestState() {
        this.initialise();
    }

    public void initialise(){
        profileFields = new ArrayList<>();
        constraints = new ArrayList<>();
        testExceptions = new ArrayList<>();
        generatedObjects = new ArrayList<>();
        contstraintsToNotViolate.clear();
        generationHasAlreadyOccured = false;
        shouldSkipGeneration = false;
        requireFieldTyping = true;
        shouldViolate = false;
    }

    public void addConstraint(String fieldName, String constraintName, List<Object> value) {
        if (value == null)
            addConstraint(fieldName, constraintName, (Object)value);
        else
            addConstraint(fieldName, constraintName, getSetValues(value));
    }

    public void addNotConstraint(String fieldName, String constraintName, List<Object> value) {
        if (value == null)
            addNotConstraint(fieldName, constraintName, (Object)value);
        else
            addNotConstraint(fieldName, constraintName, getSetValues(value));
    }

    public void addConstraint(String fieldName, String constraintName, Object value) {
        ConstraintDTO dto = this.createConstraint(fieldName, constraintName, value);
        this.addConstraintToList(dto);
    }

    public void addNotConstraint(String fieldName, String constraintName, Object value) {
        ConstraintDTO notDto = new ConstraintDTO();
        notDto.not = this.createConstraint(fieldName, constraintName, value);
        this.addConstraintToList(notDto);
    }

    public void addConstraintsFromJson(String constraintProfile) throws IOException {
        ConstraintHolder holder = this.deserialise(constraintProfile);
        this.constraints.addAll(holder.constraints);
    }

    private Collection<Object> getSetValues(List<Object> values) {
        if (values == null){
            throw new IllegalArgumentException("Values cannot be null");
        }

        values.stream()
            .filter(value -> value instanceof Exception)
            .map(value -> (Exception)value)
            .forEach(this::addException);

        return values.stream()
            .filter(value -> !(value instanceof Exception))
            .collect(Collectors.toSet());
    }

    public void addField(String fieldName) {
        this.profileFields.add(new Field(fieldName));
    }

    public void addException(Exception e){
        this.testExceptions.add(e);
    }

    public void addConstraintToNotViolate(AtomicConstraintType atomicConstraintType){
        contstraintsToNotViolate.add(atomicConstraintType);
    }

    public List<AtomicConstraintType> getConstraintsToNotViolate() {
        return contstraintsToNotViolate;
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

    private void addConstraintToList(ConstraintDTO constraintDTO) {
        this.constraints.add(constraintDTO);
    }

    private ConstraintHolder deserialise(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        return mapper.readerFor(ConstraintHolder.class).readValue(json);
    }

    int getMaxStringLength() {
        return maxStringLength;
    }

    public void setMaxStringLength(int maxLength) {
        if (maxLength > Defaults.MAX_STRING_LENGTH){
            throw new IllegalArgumentException("String lengths are limited to " + Defaults.MAX_STRING_LENGTH + " characters in production");
        }

        maxStringLength = maxLength;
    }

    public void setRequireFieldTyping(boolean requireFieldTyping) {
        this.requireFieldTyping = requireFieldTyping;
    }
}

class ConstraintHolder {
    public Collection<ConstraintDTO> constraints;
    public ConstraintHolder(){}
}

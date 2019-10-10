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
import com.scottlogic.deg.generator.config.detail.CombinationStrategyType;
import com.scottlogic.deg.generator.config.detail.DataGenerationType;
import com.scottlogic.deg.common.profile.constraintdetail.AtomicConstraintType;
import com.scottlogic.deg.profile.dto.ConstraintDTO;
import com.scottlogic.deg.profile.dto.FieldDTO;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;
import static com.scottlogic.deg.common.profile.FieldBuilder.createInternalField;

/**
 * Class to represent the state during cucumber test running and execution
 */
public class CucumberTestState {
    public DataGenerationType dataGenerationType = DataGenerationType.FULL_SEQUENTIAL;
    public CombinationStrategyType combinationStrategyType = CombinationStrategyType.PINNING;

    public boolean shouldViolate;
    public boolean expectExceptions;
    public boolean shouldSkipGeneration;
    boolean generationHasAlreadyOccured;
    public long maxRows = 200;

    List<List<Object>> generatedObjects = new ArrayList<>();
    List<FieldDTO> profileFields = new ArrayList<>();;
    List<ConstraintDTO> constraints = new ArrayList<>();
    List<Exception> testExceptions = new ArrayList<>();
    Map<String, List<List<String>>> inMapFiles = new HashMap<>();

    private final List<AtomicConstraintType> contstraintsToNotViolate = new ArrayList<>();

    public void addInMapConstraint(String fieldName, String key, String file) {

        ConstraintDTO dto = new ConstraintDTO();
        dto.field = fieldName;
        dto.is = "inMap";
        dto.key = key;
        dto.file = file;
        dto.values = getValuesFromMap(file, key);
        this.addConstraintToList(dto);
    }

    public void addRelationConstraint(String field, String relationType, String other) {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = field;
        dto.is = relationType;
        dto.otherField = other;
        this.addConstraintToList(dto);
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

    public void addMapFile(String name, List<List<String>> map) {
        this.inMapFiles.put(name, map);
    }

    private List<Object> getValuesFromMap(String name, String key) {
        List<List<String>> map = this.inMapFiles.get(name);
        int index = map.get(0).indexOf(key);
        List<Object> rtnList = new ArrayList<>();

        for (int i = 1; i < map.size() ; i++) {
            rtnList.add(map.get(i).get(index));
        }
        return rtnList;
    }


    public void addField(String fieldName) {
        FieldDTO fieldDTO = new FieldDTO();
        fieldDTO.name = fieldName;
        this.profileFields.add(fieldDTO);
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
            if (value instanceof String) {
                dto.value = value;
            }
            else if (value instanceof Collection){
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

    public void setFieldUnique(String fieldName) {
        profileFields = profileFields.stream()
            .map(fieldDTO -> {
                if (fieldDTO.name.equals(fieldName)) {
                    fieldDTO.unique = true;
                }
                return fieldDTO;
            }).collect(Collectors.toList());
    }

    public void setFieldType(String fieldName, String types) {
        profileFields = profileFields.stream()
            .map(fieldDTO -> {
                if (fieldDTO.name.equals(fieldName)) {
                    fieldDTO.type = types;
                }
                return fieldDTO;
            }).collect(Collectors.toList());
    }

    public void setFieldFormatting(String fieldName, String formatting) {
        profileFields = profileFields.stream()
            .map(fieldDTO -> {
                if (fieldDTO.name.equals(fieldName)) {
                    fieldDTO.formatting = formatting;
                }
                return fieldDTO;
            }).collect(Collectors.toList());
    }
}

class ConstraintHolder {
    public Collection<ConstraintDTO> constraints;
    public ConstraintHolder(){}
}

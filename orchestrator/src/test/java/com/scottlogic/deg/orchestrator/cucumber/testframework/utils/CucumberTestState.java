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
import com.scottlogic.deg.common.profile.SpecificFieldType;
import com.scottlogic.deg.common.profile.constraintdetail.AtomicConstraintType;
import com.scottlogic.deg.generator.config.detail.CombinationStrategyType;
import com.scottlogic.deg.generator.config.detail.DataGenerationType;
import com.scottlogic.deg.profile.common.ConstraintType;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.constraints.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

    Deque<NestedConstraint> nestedConstraints = new ArrayDeque<>();

    private final List<AtomicConstraintType> contstraintsToNotViolate = new ArrayList<>();

    public void startCreatingIfConstraint(int total) {
        nestedConstraints.push(new NestedConstraint("if", total));
    }

    public void startCreatingAllOfConstraint(int total) {
        nestedConstraints.push(new NestedConstraint("allOf", total));
    }

    public void startCreatingAnyOfConstraint(int total) {
        nestedConstraints.push(new NestedConstraint("anyOf", total));
    }

    public void addInMapConstraint(String fieldName, String key, String file) {
        this.addConstraintToList(createInMapConstraint(fieldName, key, file));
    }

    public void addRelationConstraint(String field, ConstraintType relationType, String other) {
        this.addConstraintToList(createRelationConstraint(field, relationType, other));
    }

    public void addConstraint(String fieldName, String constraintName, Object value) {
        this.addConstraintToList(createConstraint(fieldName, constraintName, value));
    }

    public void addNotConstraint(String fieldName, String constraintName, Object value) {
        this.addConstraintToList(createNotConstraint(fieldName, constraintName, value));
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

    private ConstraintDTO createConstraint(String fieldName, String constraintName, Object _value)
    {
        switch (ConstraintType.fromPropertyName(constraintName))
        {
            case EQUAL_TO:
                return new EqualToConstraintDTO()
                {{
                    field = fieldName;
                    value = _value;
                }};
            case IN_SET:
                return _value instanceof String
                        ? new InSetFromFileConstraintDTO()
                {{
                    field = fieldName;
                    file = (String)_value;
                }}
                        : new InSetOfValuesConstraintDTO()
                {{
                    field = fieldName;
                    values = (Collection<Object>)_value;
                }};
            case NULL:
                return new NullConstraintDTO()
                {{
                    field = fieldName;
                }};
            case GRANULAR_TO:
                return new GranularToConstraintDTO()
                {{
                    field = fieldName;
                    value = _value;
                }};
            case MATCHES_REGEX:
                return new MatchesRegexConstraintDTO()
                {{
                    field = fieldName;
                    value = (String) _value;
                }};
            case CONTAINS_REGEX:
                return new ContainsRegexConstraintDTO()
                {{
                    field = fieldName;
                    value = (String) _value;
                }};
            case OF_LENGTH:
                return new OfLengthConstraintDTO()
                {{
                    field = fieldName;
                    value = (int) _value;
                }};
            case LONGER_THAN:
                return new LongerThanConstraintDTO()
                {{
                    field = fieldName;
                    value = (int) _value;
                }};
            case SHORTER_THAN:
                return new ShorterThanConstraintDTO()
                {{
                    field = fieldName;
                    value = (int) _value;
                }};
            case GREATER_THAN:
                return new GreaterThanConstraintDTO()
                {{
                    field = fieldName;
                    value = (Number) _value;
                }};
            case GREATER_THAN_OR_EQUAL_TO:
                return new GreaterThanOrEqualToConstraintDTO()
                {{
                    field = fieldName;
                    value = (Number) _value;
                }};
            case LESS_THAN:
                return new LessThanConstraintDTO()
                {{
                    field = fieldName;
                    value = (Number) _value;
                }};
            case LESS_THAN_OR_EQUAL_TO:
                return new LessThanOrEqualToConstraintDTO()
                {{
                    field = fieldName;
                    value = (Number) _value;
                }};
            case AFTER:
                return new AfterConstraintDTO()
                {{
                    field = fieldName;
                    value = (String) _value;
                }};
            case AFTER_OR_AT:
                return new AfterOrAtConstraintDTO()
                {{
                    field = fieldName;
                    value = (String) _value;
                }};
            case BEFORE:
                return new BeforeConstraintDTO()
                {{
                    field = fieldName;
                    value = (String) _value;
                }};
            case BEFORE_OR_AT:
                return new BeforeOrAtConstraintDTO()
                {{
                    field = fieldName;
                    value = (String) _value;
                }};
            default:
                return null;
        }
    }


    private ConstraintDTO createNotConstraint(String fieldName, String constraintName, Object value) {
        NotConstraintDTO notDto = new NotConstraintDTO();
        notDto.constraint = this.createConstraint(fieldName, constraintName, value);
        return notDto;
    }

    private ConstraintDTO createRelationConstraint(String field, ConstraintType type, String other) {
        RelationalConstraintDTO relationalConstraintDTO;
        switch (type)
        {
            case EQUAL_TO_FIELD:
                relationalConstraintDTO = new EqualToFieldConstraintDTO(){{otherField = other;}};
                break;
            case GREATER_THAN_FIELD:
                relationalConstraintDTO = new GreaterThanFieldConstraintDTO(){{otherField = other;}};
                break;
            case GREATER_THAN_OR_EQUAL_TO_FIELD:
                relationalConstraintDTO = new GreaterThanOrEqualToFieldConstraintDTO(){{otherField = other;}};
                break;
            case LESS_THAN_FIELD:
                relationalConstraintDTO = new LessThanFieldConstraintDTO(){{otherField = other;}};
                break;
            case LESS_THAN_OR_EQUAL_TO_FIELD:
                relationalConstraintDTO = new LessThanOrEqualToFieldConstraintDTO(){{otherField = other;}};
                break;
            case AFTER_FIELD:
                relationalConstraintDTO = new AfterFieldConstraintDTO(){{otherField = other;}};
                break;
            case AFTER_OR_AT_FIELD:
                relationalConstraintDTO = new AfterOrAtFieldConstraintDTO(){{otherField = other;}};
                break;
            case BEFORE_FIELD:
                relationalConstraintDTO = new BeforeFieldConstraintDTO(){{otherField = other;}};
                break;
            case BEFORE_OR_AT_FIELD:
                relationalConstraintDTO = new BeforeOrAtFieldConstraintDTO(){{otherField = other;}};
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        relationalConstraintDTO.field = field;
        return relationalConstraintDTO;
    }

    private ConstraintDTO createInMapConstraint(String fieldName, String key, String file) {
        InMapConstraintDTO dto = new InMapConstraintDTO();
        dto.field = fieldName;
        dto.key = key;
        dto.file = file;
        return dto;
    }

    private void createIfConstraint(int total) {
        IfConstraintDTO dto = new IfConstraintDTO();
        if (total == 3) {
            dto.elseConstraint = constraints.remove(constraints.size() - 1);
            total--;
        }
        if (total == 2) {
            dto.thenConstraint = constraints.remove(constraints.size() - 1);
            dto.ifConstraint = constraints.remove(constraints.size() - 1);
        }
        this.addConstraintToList(dto);
    }

    private void createAllOfConstraint(int total) {
        AllOfConstraintDTO dto = new AllOfConstraintDTO();
        dto.constraints = new ArrayList<>();

        for (int i = 0; i < total; i++) {
            dto.constraints.add(constraints.remove(constraints.size() - 1));
        }
        this.addConstraintToList(dto);
    }

    private void createAnyOfConstraint(int total) {
        AnyOfConstraintDTO dto = new AnyOfConstraintDTO();
        dto.constraints = new ArrayList<>();

        for (int i = 0; i < total; i++) {
            dto.constraints.add(constraints.remove(constraints.size() - 1));
        }
        this.addConstraintToList(dto);
    }

    private void createNestedConstraint() {
        NestedConstraint peek = nestedConstraints.peek();

        assert peek != null;
        peek.reduceRemaining();
        if (peek.isCompleted()) {
            NestedConstraint pop = nestedConstraints.pop();
            switch (pop.constraintType) {
                case "if":
                    createIfConstraint(pop.total);
                    break;
                case "anyOf":
                    createAnyOfConstraint(pop.total);
                    break;
                case "allOf":
                    createAllOfConstraint(pop.total);
                    break;
            }
        }
    }

    private void addConstraintToList(ConstraintDTO constraintDTO) {
        this.constraints.add(constraintDTO);
        if (!nestedConstraints.isEmpty()) {
            createNestedConstraint();
        }
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

    public void setFieldType(String fieldName, String type) {
        profileFields = profileFields.stream()
            .map(fieldDTO -> {
                if (fieldDTO.name.equals(fieldName)) {
                    fieldDTO.type = SpecificFieldType.from(type);
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

    private static class NestedConstraint {
        String constraintType;
        int total;
        int remaining;

        NestedConstraint(String constraintType, int total) {
            this.constraintType = constraintType;
            this.total = total;
            this.remaining = total;
        }

        boolean isCompleted() {
            return remaining <= 0;
        }

        void reduceRemaining() {
            remaining = remaining - 1;
        }
    }
}

class ConstraintHolder {
    public Collection<ConstraintDTO> constraints;
    public ConstraintHolder(){}
}

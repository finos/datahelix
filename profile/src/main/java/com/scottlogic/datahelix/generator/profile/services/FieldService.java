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

package com.scottlogic.datahelix.generator.profile.services;

import com.scottlogic.datahelix.generator.common.profile.*;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.ProfileDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintType;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.grammatical.AllOfConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.grammatical.AnyOfConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.grammatical.ConditionalConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.grammatical.NotConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.InMapConstraintDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldService {

    public Fields createFields(ProfileDTO dto) {
        List<Field> fields = dto.fields.stream().map(this::createRegularField).collect(Collectors.toList());
        getInMapFieldNames(dto.constraints).stream().map(this::createInMapField).forEach(fields::add);
        return new ProfileFields(fields);
    }

    public static Optional<SpecificFieldType> specificFieldTypeFromString(String type, String formatting)
    {
        return type.startsWith(StandardSpecificFieldType.FAKER.getType())
            ? Optional.of(new SpecificFieldType(StandardSpecificFieldType.FAKER.getType(),
            StandardSpecificFieldType.FAKER.getFieldType(),
            formatting,
            type.substring(6)))
            : StandardSpecificFieldType.from(type).map(StandardSpecificFieldType::toSpecificFieldType);
    }

    private Field createRegularField(FieldDTO fieldDTO) {
        String formatting = formatting(fieldDTO);
        SpecificFieldType type = specificFieldTypeFromString(fieldDTO.type, formatting)
            .orElseThrow(() -> new IllegalStateException(String.format("No data types with type '%s'", fieldDTO.type)));
        return new Field(
            fieldDTO.name,
            type,
            fieldDTO.unique,
            formatting,
            false,
            fieldDTO.nullable,
            fieldDTO.generator);
    }

    private String formatting(FieldDTO fieldDTO) {
        if (fieldDTO.formatting != null) {
            return fieldDTO.formatting;
        }

        if (fieldDTO.type.startsWith(StandardSpecificFieldType.FAKER.getType())) {
            return null;
        } else {
            return StandardSpecificFieldType.from(fieldDTO.type)
                .map(StandardSpecificFieldType::getDefaultFormatting).orElse(null);
        }
    }

    private Field createInMapField(String inMapFile) {
        return new Field(inMapFile, StandardSpecificFieldType.INTEGER.toSpecificFieldType(), false, null, true, false, null);
    }

    private List<String> getInMapFieldNames(List<ConstraintDTO> constraintDTOs) {
        return constraintDTOs.stream()
            .flatMap(constraint -> getAllAtomicConstraints(Stream.of(constraint)))
            .filter(constraintDTO -> constraintDTO.getType() == ConstraintType.IN_MAP)
            .map(constraintDTO -> ((InMapConstraintDTO) constraintDTO).otherField)
            .distinct()
            .collect(Collectors.toList());
    }

    public static Stream<ConstraintDTO> getAllAtomicConstraints(Stream<ConstraintDTO> constraintDTOs) {
        return constraintDTOs.flatMap(FieldService::getAllAtomicSubConstraints);
    }

    private static Stream<ConstraintDTO> getAllAtomicSubConstraints(ConstraintDTO constraintDTO) {
        switch (constraintDTO.getType()) {
            case IF:
                ConditionalConstraintDTO conditionalConstraintDTO = (ConditionalConstraintDTO) constraintDTO;
                return getAllAtomicConstraints(conditionalConstraintDTO.elseConstraint == null
                    ? Stream.of(((ConditionalConstraintDTO) constraintDTO).thenConstraint)
                    : Stream.of(((ConditionalConstraintDTO) constraintDTO).thenConstraint, ((ConditionalConstraintDTO) constraintDTO).elseConstraint));
            case ALL_OF:
                return getAllAtomicConstraints(((AllOfConstraintDTO) constraintDTO).constraints.stream());
            case ANY_OF:
                return getAllAtomicConstraints(((AnyOfConstraintDTO) constraintDTO).constraints.stream());
            case NOT:
                return getAllAtomicConstraints(Stream.of(((NotConstraintDTO) constraintDTO).constraint));
            default:
                return Stream.of(constraintDTO);
        }
    }
}

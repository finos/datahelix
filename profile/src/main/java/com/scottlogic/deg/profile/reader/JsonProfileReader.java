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

package com.scottlogic.deg.profile.reader;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.common.profile.fields.Field;
import com.scottlogic.deg.common.profile.fields.Fields;
import com.scottlogic.deg.common.profile.fields.SpecificFieldType;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.profile.common.ConstraintType;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.ProfileDTO;
import com.scottlogic.deg.profile.dtos.RuleDTO;
import com.scottlogic.deg.profile.dtos.constraints.*;
import com.scottlogic.deg.profile.serialisation.ProfileSerialiser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * JsonProfileReader is responsible for reading and validating a profile from a path to a profile JSON file.
 * It returns a Profile object for consumption by a generator
 */
public class JsonProfileReader implements ProfileReader {
    private final File profileFile;
    private final ConstraintReader constraintReader;

    @Inject
    public JsonProfileReader(@Named("config:profileFile") File profileFile, ConstraintReader constraintReader) {
        this.profileFile = profileFile;
        this.constraintReader = constraintReader;
    }

    public Profile read() throws IOException {
        byte[] encoded = Files.readAllBytes(profileFile.toPath());
        String profileJson = new String(encoded, StandardCharsets.UTF_8);
        return read(profileJson);
    }

    public Profile read(String profileJson) {
        ProfileDTO profileDTO = new ProfileSerialiser().deserialise(profileJson);
        if (profileDTO.fields == null)
            throw new InvalidProfileException("Profile is invalid: 'fields' have not been defined.");
        if (profileDTO.rules == null)
            throw new InvalidProfileException("Profile is invalid: 'rules' have not been defined.");
        Collection<Field> fieldCollection = new ArrayList<>();
        fieldCollection.addAll(profileDTO.fields.stream().map(this::createField).collect(Collectors.toList()));
        fieldCollection.addAll(profileDTO.rules.stream().map(this::createInMapFields).flatMap(Collection::stream).collect(Collectors.toList()));
        Fields fields = Fields.create(fieldCollection);

        Collection<Rule> rules = profileDTO.rules.stream()
            .map(r -> Rule.create(r.rule, constraintReader.read(r.constraints, fields)))
            .collect(Collectors.toList());

        Collection<Constraint> nonNullableConstraints = createNullableConstraints(fields, constraintReader);
        Collection<Constraint> typeConstraints = createSpecificTypeConstraints(fields);

        if (!nonNullableConstraints.isEmpty()) {
            rules.add(Rule.create("nullable-rules", nonNullableConstraints));
        }
        if (!typeConstraints.isEmpty()) {
            rules.add(Rule.create("type-rules", typeConstraints));
        }
        return new Profile(fields, rules, profileDTO.description);
    }
    
    private String getFormatting(FieldDTO fDto) {
        if (fDto.formatting != null) {
            return fDto.formatting;
        } else  {
            return fDto.type.getDefaultFormatting();
        }
    }

    private NullConstraintDTO nullDtoOf(Field field) {
        NullConstraintDTO nullConstraint = new NullConstraintDTO();
        nullConstraint.field = field.getName();
        return nullConstraint;
    }

    private Field createField(FieldDTO dto)
    {
        return Field.create(dto.name, dto.type, dto.unique, getFormatting(dto), false, dto.nullable);
    }

    private Collection<Field> createInMapFields(RuleDTO ruleDTO)
    {
        return ruleDTO.constraints.stream()
            .flatMap(constraintDTO -> Stream.of(constraintDTO).flatMap(this::getAllNestedConstraintDTOs))
            .filter(constraintDTO -> constraintDTO.getType() == ConstraintType.IN_MAP)
            .map(constraintDTO -> ((InMapConstraintDTO) constraintDTO).file)
            .distinct()
            .map(file -> Field.create(file, SpecificFieldType.INTEGER, false, null, true, true))
            .collect(Collectors.toList());
    }

    private Stream<ConstraintDTO> getAllNestedConstraintDTOs(ConstraintDTO constraintDTO)
    {
        switch (constraintDTO.getType())
        {
            case IF:
                IfConstraintDTO conditionalConstraintDTO = (IfConstraintDTO)constraintDTO;
                return (conditionalConstraintDTO.elseConstraint == null
                    ? Stream.of(conditionalConstraintDTO.thenConstraint)
                    : Stream.of(conditionalConstraintDTO.thenConstraint, conditionalConstraintDTO.elseConstraint)
                ).flatMap(this::getAllNestedConstraintDTOs);
            case ALL_OF:
                return ((AllOfConstraintDTO)constraintDTO).constraints.stream().flatMap(this::getAllNestedConstraintDTOs);
            case ANY_OF:
                return ((AnyOfConstraintDTO)constraintDTO).constraints.stream().flatMap(this::getAllNestedConstraintDTOs);
            default:
                return Stream.of(constraintDTO);
        }
    }

    private Collection<Constraint> createNullableConstraints(Fields fields, ConstraintReader constraintReader)
    {
        return fields.stream()
            .filter(field -> !field.isNullable())
            .map(fieldDTO -> constraintReader.read(nullDtoOf(fieldDTO), fields))
            .collect(Collectors.toList());
    }

    private Collection<Constraint> createSpecificTypeConstraints(Fields fields)
    {
        return fields.stream()
            .map(FieldReader::read)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }
}

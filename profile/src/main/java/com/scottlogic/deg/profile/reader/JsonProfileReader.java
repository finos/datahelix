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
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.RuleInformation;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.ProfileDTO;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.common.ConstraintType;
import com.scottlogic.deg.profile.dtos.constraints.InMapConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.AllOfConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.AnyOfConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.IfConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.NullConstraintDTO;
import com.scottlogic.deg.profile.reader.atomic.FieldReader;
import com.scottlogic.deg.profile.serialisation.ProfileSerialiser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
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

        List<Field> fields = profileDTO.fields.stream()
                .map(fieldDTO -> new Field(
                    fieldDTO.name,
                    fieldDTO.type.getFieldType(),
                    fieldDTO.unique,
                    getFormatting(fieldDTO),
                    false,
                    fieldDTO.nullable))
                .collect(Collectors.toList());

        List<Field> inMapFields = profileDTO.rules.stream()
            .flatMap(ruleDTO -> ruleDTO.constraints.stream())
            .flatMap(constraintDTO -> getInMapConstraints(profileDTO).stream())
            .distinct()
            .map(file -> new Field(file, FieldType.NUMERIC, false, null, true, false)
            ).collect(Collectors.toList());

        fields.addAll(inMapFields);
        Fields profileFields = new Fields(fields);

        Collection<Rule> rules = profileDTO.rules.stream()
            .map(r -> new Rule(new RuleInformation(r.rule), constraintReader.read(r.constraints, profileFields)))
            .collect(Collectors.toList());

        Collection<Constraint> nonNullableConstraints = profileDTO.fields.stream()
            .filter(fieldDTO -> !fieldDTO.nullable)
            .map(fieldDTO -> constraintReader.read(nullDtoOf(fieldDTO), profileFields))
            .collect(Collectors.toList());

        Collection<Constraint> typeConstraints = profileDTO.fields.stream()
            .map(fieldDto -> FieldReader.read(profileFields.getByName(fieldDto.name), fieldDto.type))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

        if (!nonNullableConstraints.isEmpty()) {
            rules.add(new Rule(new RuleInformation("nullable-rules"), nonNullableConstraints));
        }
        if (!typeConstraints.isEmpty()) {
            rules.add(new Rule(new RuleInformation("type-rules"), typeConstraints));
        }
        return new Profile(profileFields, rules, profileDTO.description);
    }
    
    private String getFormatting(FieldDTO fDto) {
        if (fDto.formatting != null) {
            return fDto.formatting;
        } else  {
            return fDto.type.getDefaultFormatting();
        }
    }

    private NullConstraintDTO nullDtoOf(FieldDTO fieldDTO) {
        NullConstraintDTO nullConstraint = new NullConstraintDTO();
        nullConstraint.field = fieldDTO.name;
        return nullConstraint;
    }

    private List<String> getInMapConstraints(ProfileDTO profileDto) {
        return profileDto.rules.stream()
            .flatMap(ruleDTO -> ruleDTO.constraints.stream())
            .flatMap(constraint -> getAllAtomicConstraints(Stream.of(constraint)))
            .filter(constraintDTO -> constraintDTO.getType() == ConstraintType.IN_MAP)
            .map(constraintDTO -> ((InMapConstraintDTO) constraintDTO).file)
            .collect(Collectors.toList());
    }

    private Stream<ConstraintDTO> getAllAtomicConstraints(Stream<ConstraintDTO> constraints) {
        return constraints.flatMap(this::getUnpackedConstraintsToStream);
    }

    private Stream<ConstraintDTO> getUnpackedConstraintsToStream(ConstraintDTO constraintDTO) {
        switch (constraintDTO.getType()) {
            case IF:
                IfConstraintDTO conditionalConstraintDTO = (IfConstraintDTO) constraintDTO;
                return getAllAtomicConstraints(conditionalConstraintDTO.elseConstraint == null
                    ? Stream.of(((IfConstraintDTO) constraintDTO).thenConstraint)
                    : Stream.of(((IfConstraintDTO) constraintDTO).thenConstraint, ((IfConstraintDTO) constraintDTO).elseConstraint));
            case ALL_OF:
                return getAllAtomicConstraints(((AllOfConstraintDTO) constraintDTO).constraints.stream());
            case ANY_OF:
                return getAllAtomicConstraints(((AnyOfConstraintDTO) constraintDTO).constraints.stream());
            default:
                return Stream.of(constraintDTO);
        }
    }
}

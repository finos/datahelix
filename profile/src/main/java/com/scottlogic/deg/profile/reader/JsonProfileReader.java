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
import com.scottlogic.deg.common.profile.*;
import com.scottlogic.deg.common.profile.constraintdetail.AtomicConstraintType;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.RuleInformation;
import com.scottlogic.deg.profile.dto.ConstraintDTO;
import com.scottlogic.deg.profile.reader.atomic.ConstraintReaderHelpers;
import com.scottlogic.deg.profile.reader.atomic.OfTypeConstraintFactory;
import com.scottlogic.deg.profile.serialisation.ProfileDeserialiser;
import com.scottlogic.deg.profile.dto.ProfileDTO;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.profile.reader.atomic.AtomicConstraintFactory.create;
import static com.scottlogic.deg.profile.reader.atomic.ConstraintReaderHelpers.getFieldType;

/**
 * JsonProfileReader is responsible for reading and validating a profile from a path to a profile JSON file.
 * It returns a Profile object for consumption by a generator
 */
public class JsonProfileReader implements ProfileReader {
    private final File profileFile;
    private final MainConstraintReader mainConstraintReader;

    @Inject
    public JsonProfileReader(@Named("config:profileFile") File profileFile, MainConstraintReader mainConstraintReader) {
        this.profileFile = profileFile;
        this.mainConstraintReader = mainConstraintReader;
    }

    public Profile read() throws IOException {
        byte[] encoded = Files.readAllBytes(profileFile.toPath());
        String profileJson = new String(encoded, StandardCharsets.UTF_8);

        return this.read(profileJson);
    }

    public Profile read(String profileJson) throws IOException {
        ProfileDTO profileDto = new ProfileDeserialiser()
            .deserialise(profileJson);

        if (profileDto.fields == null) {
            throw new InvalidProfileException("Profile is invalid: 'fields' have not been defined.");
        }
        if (profileDto.rules == null) {
            throw new InvalidProfileException("Profile is invalid: 'rules' have not been defined.");
        }

        List<Field> inMapFields = getInMapConstraints(profileDto).stream()
            .map(file ->
                new Field(
                    file,
                    getFieldType("integer"),
                    false,
                    null,
                    true)
            ).collect(Collectors.toList());


        List<Field> fields = profileDto.fields.stream()
            .map(fDto -> new Field(fDto.name, ConstraintReaderHelpers.getFieldType(fDto.type), fDto.unique, fDto.formatting,false))
            .collect(Collectors.toList());

        fields.addAll(inMapFields);

        ProfileFields profileFields = new ProfileFields(fields);

        Collection<Rule> rules = profileDto.rules.stream().map(
            r -> {
                RuleInformation constraintRule = new RuleInformation(r.rule);
                return new Rule(constraintRule, mainConstraintReader.getSubConstraints(profileFields, r.constraints));
            }).collect(Collectors.toList());


        // add nullable
        Collection<Constraint> nullableRules = profileDto.fields.stream()
            .filter(fieldDTO -> !fieldDTO.nullable)
            .map(fieldDTO -> create(AtomicConstraintType.IS_NULL, profileFields.getByName(fieldDTO.name), null).negate())
            .collect(Collectors.toList());

        if (nullableRules.size() > 0) {
            rules.add(new Rule(new RuleInformation("nullable-rules"), nullableRules));
        }

        // add types
        Collection<Constraint> typeRules = profileDto.fields.stream()
                .filter(fieldDTO -> fieldDTO.type != null )
                .map(fieldDTO -> OfTypeConstraintFactory.create(profileFields.getByName(fieldDTO.name), fieldDTO.type))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (typeRules.size() > 0) {
            rules.add(new Rule(new RuleInformation("type-rules"), typeRules));
        }
        return new Profile(profileFields, rules, profileDto.description);
    }

    private Set<String> getInMapConstraints(ProfileDTO profileDto) {
        return profileDto.rules.stream()
            .flatMap(ruleDTO -> ruleDTO.constraints.stream())
            .flatMap(constraint -> getAllAtomicConstraints(Stream.of(constraint)))
            .filter(constraintDTO -> constraintDTO.is != null)
            .filter(constraintDTO -> constraintDTO.is.equals(AtomicConstraintType.IS_IN_MAP.getText()))
            .map(constraintDTO -> constraintDTO.file)
            .collect(Collectors.toSet());
    }

    private Stream<ConstraintDTO> getAllAtomicConstraints(Stream<ConstraintDTO> constraints) {
        return constraints.flatMap(this::getUnpackedConstraintsToStream);

    }

    private Stream<ConstraintDTO> getUnpackedConstraintsToStream(ConstraintDTO constraintDTO) {
        if (constraintDTO.then != null) {
            return getAllAtomicConstraints(constraintDTO.else_ == null ?
                Stream.of(constraintDTO.then) :
                Stream.of(constraintDTO.then, constraintDTO.else_));
        }
        if (constraintDTO.allOf != null){
            return getAllAtomicConstraints(constraintDTO.allOf.stream());
        }
        if (constraintDTO.anyOf != null){
            return getAllAtomicConstraints(constraintDTO.anyOf.stream());
        }
        return Stream.of(constraintDTO);
    }
}

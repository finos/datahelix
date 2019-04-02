package com.scottlogic.deg.generator.inputs;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.schemas.common.ProfileDeserialiser;
import com.scottlogic.deg.schemas.v0_1.ProfileDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * JsonProfileReader is responsible for reading and validating a profile from a path to a profile JSON file.
 * It returns a Profile object for consumption by a generator
 */
public class JsonProfileReader implements ProfileReader {
    public Profile read(Path filePath) throws IOException, InvalidProfileException {
        byte[] encoded = Files.readAllBytes(filePath);
        String profileJson = new String(encoded, StandardCharsets.UTF_8);

        return this.read(profileJson);
    }

    public Profile read(String profileJson) throws IOException, InvalidProfileException {
        ProfileDTO profileDto = (ProfileDTO) new ProfileDeserialiser()
            .deserialise(
                profileJson,
                ProfileDTO.SchemaVersion);

        if (profileDto.fields == null) {
            throw new InvalidProfileException("Profile is invalid: 'fields' have not been defined.");
        }
        if (profileDto.rules == null) {
            throw new InvalidProfileException("Profile is invalid: 'rules' have not been defined.");
        }

        ProfileFields profileFields = new ProfileFields(
            profileDto.fields.stream()
                .map(fDto -> new Field(fDto.name))
                .collect(Collectors.toList()));

        ConstraintReader constraintReader = new MainConstraintReader();

        Collection<Rule> rules = mapDtos(
            profileDto.rules,
            r -> {
                if (r.constraints.isEmpty()) {
                    throw new InvalidProfileException("Profile is invalid: unable to find 'constraints' for rule: " + r.rule);
                }
                RuleInformation constraintRule = new RuleInformation(r);
                return new Rule(
                    constraintRule,
                    mapDtos(
                        r.constraints,
                        dto -> {
                            try {
                                return constraintReader.apply(
                                    dto,
                                    profileFields,
                                    Collections.singleton(constraintRule));
                            } catch (InvalidProfileException e) {
                                throw new InvalidProfileException("Rule: " + r.rule + "\n" + e.getMessage());
                            }
                        }));
            });

        return new Profile(profileFields, rules, profileDto.description);
    }

    static <TInput, TOutput> Collection<TOutput> mapDtos(
        Collection<TInput> dtos,
        DtoConverterFunction<TInput, TOutput> mapFunc) throws InvalidProfileException {

        Collection<TOutput> resultSet = new ArrayList<>();

        for (TInput dto : dtos) {
            resultSet.add(mapFunc.apply(dto));
        }

        return resultSet;
    }

    @FunctionalInterface
    interface DtoConverterFunction<TInput, TOutput> {
        TOutput apply(TInput t) throws InvalidProfileException;
    }
}

package com.scottlogic.deg.generator.inputs;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidationVisitor;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.reporters.NoopProfileValidationReporter;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;
import com.scottlogic.deg.schemas.common.ProfileDeserialiser;
import com.scottlogic.deg.schemas.v3.V3ProfileDTO;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class ProfileReader {

    private ProfileValidator validator;

    @Inject
    public ProfileReader(ProfileValidator validator) {
        this.validator = validator;
    }

    public Profile read(Path filePath) throws IOException, InvalidProfileException {
        byte[] encoded = Files.readAllBytes(filePath);
        String profileJson = new String(encoded, StandardCharsets.UTF_8);

        Profile profile =  this.read(profileJson);

        validator.validate(profile);

        return profile;
    }

    public Profile read(String profileJson) throws IOException, InvalidProfileException {
        V3ProfileDTO profileDto = (V3ProfileDTO) new ProfileDeserialiser()
            .deserialise(
                profileJson,
                V3ProfileDTO.SchemaVersion);

        if (profileDto.fields == null || profileDto.rules == null) {
            throw new InvalidProfileException("Profile is invalid, either 'fields' or 'rules' have not been defined.");
        }

        ProfileFields profileFields = new ProfileFields(
            profileDto.fields.stream()
                .map(fDto -> new Field(fDto.name))
                .collect(Collectors.toList()));

        ConstraintReader constraintReader = new MainConstraintReader();

        Collection<Rule> rules = mapDtos(
            profileDto.rules,
            r -> {
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

package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.schemas.v3.V3ProfileDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Defines a mapper between a ProfileDto and a Profile
 */
public class ProfileDtoMapper {
    public static Profile mapToProfile(V3ProfileDTO profileDto) throws InvalidProfileException {
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

    public static V3ProfileDTO mapToProfileDto(Profile profile) {
        //TODO Write a mapper from Profile to ProfileDTO
        return new V3ProfileDTO();
    }

    static <TInput, TOutput> Collection<TOutput> mapDtos (
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

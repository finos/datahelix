package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.schemas.common.ProfileDeserialiser;
import com.scottlogic.deg.schemas.v3.V3ProfileDTO;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class ProfileReader {
    public Profile read(Path filePath) throws IOException, InvalidProfileException {
        byte[] encoded = Files.readAllBytes(filePath);
        String profileJson = new String(encoded, Charset.forName("UTF-8"));

        return this.read(profileJson);
    }

    public Profile read(String profileJson) throws IOException, InvalidProfileException {
        V3ProfileDTO profileDto = (V3ProfileDTO) new ProfileDeserialiser()
            .deserialise(
                profileJson,
                V3ProfileDTO.SchemaVersion);

        ProfileFields profileFields = new ProfileFields(
            profileDto.fields.stream()
                .map(fDto -> new Field(fDto.name))
                .collect(Collectors.toList()));

        IConstraintReader constraintReader = new MainConstraintReader();

        Collection<Rule> rules = mapDtos(
            profileDto.rules,
            r -> new Rule(
                r.rule != null
                    ? r.rule
                    : "Unnamed rule",
                mapDtos(
                    r.constraints,
                    dto -> constraintReader.apply(
                        dto,
                        profileFields))));

        return new Profile(profileFields, rules, profileDto.description);
    }

    //* Because Java sucks at handling exceptions during stream operations */
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

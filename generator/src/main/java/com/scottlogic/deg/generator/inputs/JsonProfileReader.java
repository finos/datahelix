package com.scottlogic.deg.generator.inputs;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.schemas.common.ProfileDeserialiser;
import com.scottlogic.deg.schemas.v3.V3ProfileDTO;

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

    private ProfileValidator validator;

    @Inject
    public JsonProfileReader(ProfileValidator validator) {
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

        if (profileDto.fields == null) {
            throw new InvalidProfileException("Profile is invalid: 'fields' have not been defined.");
        }
        if (profileDto.rules == null) {
            throw new InvalidProfileException("Profile is invalid: 'rules' have not been defined.");
        }

        return ProfileDtoMapper.mapToProfile(profileDto);
    }
}

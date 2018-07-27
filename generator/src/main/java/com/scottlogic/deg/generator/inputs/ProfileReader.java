package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.schemas.common.ProfileDeserialiser;
import com.scottlogic.deg.schemas.v3.V3Profile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Collectors;

public class ProfileReader {
    public Profile reader(Path filePath) throws IOException {
        byte[] encoded = Files.readAllBytes(filePath);
        String profileJson = new String(encoded, Charset.forName("UTF-8"));

        V3Profile profileDto = (V3Profile) new ProfileDeserialiser()
            .deserialise(
                profileJson,
                V3Profile.SchemaVersion);

        return new Profile(
            profileDto.fields.stream()
                .map(fDto -> new Field(fDto.name))
                .collect(Collectors.toList()),
            Collections.emptyList());
    }
}

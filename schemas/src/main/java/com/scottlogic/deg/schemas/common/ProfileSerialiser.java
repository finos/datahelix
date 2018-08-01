package com.scottlogic.deg.schemas.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ProfileSerialiser {
    public String serialise(BaseProfile profile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(profile);
    }
}


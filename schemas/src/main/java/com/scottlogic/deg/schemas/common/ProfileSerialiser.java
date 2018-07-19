package com.scottlogic.deg.schemas.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ProfileSerialiser {
    public String serialise(BaseProfile profile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(profile);
    }
}


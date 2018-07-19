package com.scottlogic.deg.schemas.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ProfileDeserialiser {
    public BaseProfile deserialise(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readerFor(BaseProfile.class).readValue(json);
    }
}

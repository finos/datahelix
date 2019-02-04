package com.scottlogic.deg.schemas.v3;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class RuleSerializer extends JsonSerializer<RuleDTO> {
    @Override
    public void serialize(
        RuleDTO rule,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider)
        throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("rule");
        jsonGenerator.writeString(rule.rule);
        jsonGenerator.writeArrayFieldStart("constraints");
        for (ConstraintDTO constraint : rule.constraints)
        {
            jsonGenerator.writeObject(constraint);
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}

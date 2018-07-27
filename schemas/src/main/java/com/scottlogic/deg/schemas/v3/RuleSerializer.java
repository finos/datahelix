package com.scottlogic.deg.schemas.v3;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class RuleSerializer extends JsonSerializer<Rule> {
    @Override
    public void serialize(
        Rule rule,
        JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider)
        throws IOException {

        if (rule.description == null) {
            for (Constraint constraint : rule.constraints)
            {
                jsonGenerator.writeObject(constraint);
            }
        }
        else {
            throw new UnsupportedOperationException(); // TODO
        }
    }
}

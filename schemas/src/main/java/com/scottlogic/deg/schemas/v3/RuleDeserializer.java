package com.scottlogic.deg.schemas.v3;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;

import java.io.IOException;
import java.util.*;

public class RuleDeserializer extends JsonDeserializer<RuleDTO> {

    @Override
    public RuleDTO deserialize(
        JsonParser jsonParser,
        DeserializationContext deserializationContext)
        throws IOException {

        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();

        JsonNode node = mapper.readTree(jsonParser);

        RuleDTO rule = new RuleDTO();
        rule.rule = node.has("rule")
            ? node.get("rule").asText()
            : null;
        rule.constraints = node.has("constraints")
            ? readConstraintsFromArrayNode(node.get("constraints"), mapper)
            : Collections.emptySet();
        return rule;
    }

    private Collection<ConstraintDTO> readConstraintsFromArrayNode(
        JsonNode node,
        ObjectMapper mapper)
        throws JsonProcessingException {

        List<ConstraintDTO> constraints = new ArrayList<>();

        Iterator<JsonNode> constraintNodeIterator = node.elements();

        while (constraintNodeIterator.hasNext())
        {
            JsonNode constraintNode = constraintNodeIterator.next();

            constraints.add(
                mapper.treeToValue(
                    constraintNode,
                    ConstraintDTO.class));
        }

        return constraints;
    }
}


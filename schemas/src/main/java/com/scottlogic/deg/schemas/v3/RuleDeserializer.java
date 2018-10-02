package com.scottlogic.deg.schemas.v3;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.*;

public class RuleDeserializer extends JsonDeserializer<RuleDTO> {

    @Override
    public RuleDTO deserialize(
        JsonParser jsonParser,
        DeserializationContext deserializationContext)
        throws IOException, JsonProcessingException {

        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();

        JsonNode node = mapper.readTree(jsonParser);

        if (node.has("rule")) {
            RuleDTO rule = new RuleDTO();
            rule.rule = node.get("rule").asText();
            rule.constraints = readConstraintsFromArrayNode(node.get("constraints"), mapper);
            return rule;
        }
        else {
            ConstraintDTO constraint = mapper.treeToValue(node, ConstraintDTO.class);

            RuleDTO rule = new RuleDTO();
            rule.constraints = Collections.singleton(constraint);
            return rule;
        }
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


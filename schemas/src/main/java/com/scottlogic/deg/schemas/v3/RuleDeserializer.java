package com.scottlogic.deg.schemas.v3;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.*;

public class RuleDeserializer extends JsonDeserializer<Rule> {

    @Override
    public Rule deserialize(
        JsonParser jsonParser,
        DeserializationContext deserializationContext)
        throws IOException, JsonProcessingException {

        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();

        JsonNode node = mapper.readTree(jsonParser);

        if (node.has("rule")) {
            Rule rule = new Rule();
            rule.description = node.get("rule").asText();
            rule.constraints = readConstraintsFromArrayNode(node.get("constraints"), mapper);
            return rule;
        }
        else {
            Constraint constraint = mapper.treeToValue(node, Constraint.class);

            Rule rule = new Rule();
            rule.constraints = Collections.singleton(constraint);
            return rule;
        }
    }

    private Collection<Constraint> readConstraintsFromArrayNode(
        JsonNode node,
        ObjectMapper mapper)
        throws JsonProcessingException {

        List<Constraint> constraints = new ArrayList<>();

        Iterator<JsonNode> constraintNodeIterator = node.elements();

        while (constraintNodeIterator.hasNext())
        {
            JsonNode constraintNode = constraintNodeIterator.next();

            constraints.add(
                mapper.treeToValue(
                    constraintNode,
                    Constraint.class));
        }

        return constraints;
    }
}


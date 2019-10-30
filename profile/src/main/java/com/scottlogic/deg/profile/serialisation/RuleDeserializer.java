/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.profile.serialisation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.profile.dtos.RuleDTO;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO;

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

        RuleDTO dto = new RuleDTO();
        dto.description = node.has("rule")
            ? node.get("rule").asText()
            : null;

        dto.constraints = node.has("constraints")
            ? readConstraintsFromArrayNode(node.get("constraints"), mapper)
            : Collections.emptySet();

        return dto;
    }

    private Collection<ConstraintDTO> readConstraintsFromArrayNode(
        JsonNode node,
        ObjectMapper mapper)
        throws JsonProcessingException {

        List<ConstraintDTO> constraints = new ArrayList<>();

        Iterator<JsonNode> constraintNodeIterator = node.elements();

        while (constraintNodeIterator.hasNext()) {
            JsonNode constraintNode = constraintNodeIterator.next();

            constraints.add(
                mapper.treeToValue(
                    constraintNode,
                    ConstraintDTO.class));
        }

        return constraints;
    }
}


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

package com.scottlogic.deg.profile.dtos.constraints;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scottlogic.deg.profile.common.ConstraintType;
import com.scottlogic.deg.profile.common.ConstraintTypeJsonProperty;
import com.scottlogic.deg.profile.reader.InvalidProfileException;

import java.io.IOException;
import java.util.Arrays;

@JsonDeserialize(using = ConstraintDTO.ConstraintDeserializer.class)
public abstract class ConstraintDTO
{
    private final ConstraintType type;

    ConstraintDTO(ConstraintType type)
    {
        this.type = type;
    }

    @JsonIgnore
    public ConstraintType getType()
    {
        return type;
    }

    static class ConstraintDeserializer extends JsonDeserializer<ConstraintDTO>
    {
        @Override
        public ConstraintDTO deserialize(JsonParser p, DeserializationContext context) throws IOException
        {
            ObjectMapper mapper = (ObjectMapper) p.getCodec();
            ObjectNode node = mapper.readTree(p);
            ConstraintType type = Arrays.stream(ConstraintType.values())
                    .filter(constraintType -> node.has(constraintType.propertyName))
                    .findFirst().orElse(null);
            if (type != null)
            {
                switch (type)
                {
                    case EQUAL_TO:
                        return mapper.treeToValue(node, EqualToConstraintDTO.class);
                    case EQUAL_TO_FIELD:
                        return mapper.treeToValue(node, EqualToFieldConstraintDTO.class);
                    case IN_SET:
                        return node.get(ConstraintTypeJsonProperty.IN_SET).isArray()
                                ? mapper.treeToValue(node, InSetOfValuesConstraintDTO.class)
                                : mapper.treeToValue(node, InSetFromFileConstraintDTO.class);
                    case IN_MAP:
                        return mapper.treeToValue(node, InMapConstraintDTO.class);
                    case NULL:
                        return mapper.treeToValue(node, NullConstraintDTO.class);
                    case GRANULAR_TO:
                        return mapper.treeToValue(node, GranularToConstraintDTO.class);
                    case MATCHES_REGEX:
                        return mapper.treeToValue(node, MatchesRegexConstraintDTO.class);
                    case CONTAINS_REGEX:
                        return mapper.treeToValue(node, ContainsRegexConstraintDTO.class);
                    case OF_LENGTH:
                        return mapper.treeToValue(node, OfLengthConstraintDTO.class);
                    case LONGER_THAN:
                        return mapper.treeToValue(node, LongerThanConstraintDTO.class);
                    case SHORTER_THAN:
                        return mapper.treeToValue(node, ShorterThanConstraintDTO.class);
                    case GREATER_THAN:
                        return mapper.treeToValue(node, GreaterThanConstraintDTO.class);
                    case GREATER_THAN_FIELD:
                        return mapper.treeToValue(node, GreaterThanFieldConstraintDTO.class);
                    case GREATER_THAN_OR_EQUAL_TO:
                        return mapper.treeToValue(node, GreaterThanOrEqualToConstraintDTO.class);
                    case GREATER_THAN_OR_EQUAL_TO_FIELD:
                        return mapper.treeToValue(node, GreaterThanOrEqualToFieldConstraintDTO.class);
                    case LESS_THAN:
                        return mapper.treeToValue(node, LessThanConstraintDTO.class);
                    case LESS_THAN_FIELD:
                        return mapper.treeToValue(node, LessThanFieldConstraintDTO.class);
                    case LESS_THAN_OR_EQUAL_TO:
                        return mapper.treeToValue(node, LessThanOrEqualToConstraintDTO.class);
                    case LESS_THAN_OR_EQUAL_TO_FIELD:
                        return mapper.treeToValue(node, LessThanOrEqualToFieldConstraintDTO.class);
                    case AFTER:
                        return mapper.treeToValue(node, AfterConstraintDTO.class);
                    case AFTER_FIELD:
                        return mapper.treeToValue(node, AfterFieldConstraintDTO.class);
                    case AFTER_OR_AT:
                        return mapper.treeToValue(node, AfterOrAtConstraintDTO.class);
                    case AFTER_OR_AT_FIELD:
                        return mapper.treeToValue(node, AfterOrAtFieldConstraintDTO.class);
                    case BEFORE:
                        return mapper.treeToValue(node, BeforeConstraintDTO.class);
                    case BEFORE_FIELD:
                        return mapper.treeToValue(node, BeforeFieldConstraintDTO.class);
                    case BEFORE_OR_AT:
                        return mapper.treeToValue(node, BeforeOrAtConstraintDTO.class);
                    case BEFORE_OR_AT_FIELD:
                        return mapper.treeToValue(node, BeforeOrAtFieldConstraintDTO.class);
                    case NOT:
                        return mapper.treeToValue(node, NotConstraintDTO.class);
                    case ANY_OF:
                        return mapper.treeToValue(node, AnyOfConstraintDTO.class);
                    case ALL_OF:
                        return mapper.treeToValue(node, AllOfConstraintDTO.class);
                    case IF:
                        return mapper.treeToValue(node, IfConstraintDTO.class);
                }
            }
            throw new InvalidProfileException("The constraint json object node doesn't contain any of the expected keywords as properties: " + node);
        }
    }
}

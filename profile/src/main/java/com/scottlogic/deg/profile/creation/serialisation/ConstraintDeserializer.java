package com.scottlogic.deg.profile.creation.serialisation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scottlogic.deg.profile.common.ConstraintType;
import com.scottlogic.deg.profile.common.ConstraintTypeJsonProperty;
import com.scottlogic.deg.profile.creation.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.NotConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.InvalidConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.*;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.numeric.*;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.temporal.AfterConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.temporal.AfterOrAtConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.temporal.BeforeConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.temporal.BeforeOrAtConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.textual.ContainsRegexConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.atomic.textual.MatchesRegexConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.AllOfConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.AnyOfConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.ConditionalConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.relations.*;
import com.scottlogic.deg.profile.reader.FileReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConstraintDeserializer extends JsonDeserializer<ConstraintDTO> {

    public static FileReader fileReader;

    @Override
    public ConstraintDTO deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException
    {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode node = mapper.readTree(jsonParser);
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
                    JsonNode inSetNode = node.get(ConstraintTypeJsonProperty.IN_SET);
                    return (inSetNode.isNull() || inSetNode.isArray())
                        ? mapper.treeToValue(node, InSetConstraintDTO.class)
                        : map(mapper.treeToValue(node, InSetFromFileConstraintDTO.class));
                case IN_MAP:
                    return map(mapper.treeToValue(node, InMapFromFileConstraintDTO.class));
                case IS_NULL:
                    return mapper.treeToValue(node, IsNullConstraintDTO.class);
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
                    return mapper.treeToValue(node, ConditionalConstraintDTO.class);
            }
        }
        return new InvalidConstraintDTO(node.toString());
    }

    private InMapConstraintDTO map(InMapFromFileConstraintDTO dto)
    {
        List<Object> values = fileReader.listFromMapFile(dto.file, dto.key).stream().collect(Collectors.toList());
        InMapConstraintDTO inMapConstraintDTO = new InMapConstraintDTO();
        inMapConstraintDTO.field = dto.field;
        inMapConstraintDTO.otherField = dto.file;
        inMapConstraintDTO.values = values;
        return inMapConstraintDTO;
    }

    private InSetConstraintDTO map(InSetFromFileConstraintDTO dto)
    {
        List<Object> values = fileReader.setFromFile(dto.file).stream().collect(Collectors.toList());
        InSetConstraintDTO inSetConstraintDTO = new InSetConstraintDTO();
        inSetConstraintDTO.field = dto.field;
        inSetConstraintDTO.values = values;
        return inSetConstraintDTO;
    }
}

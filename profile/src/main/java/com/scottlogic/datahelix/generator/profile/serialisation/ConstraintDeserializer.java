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

package com.scottlogic.datahelix.generator.profile.serialisation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintType;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.InvalidConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.InMapFromFileConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.InSetConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.InSetFromFileConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.InMapConstraintDTO;
import com.scottlogic.datahelix.generator.profile.reader.FileReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConstraintDeserializer extends JsonDeserializer<ConstraintDTO> {
    private final FileReader fileReader;
    private final Path profileDirectory;

    public ConstraintDeserializer(FileReader fileReader, Path profileDirectory) {
        this.fileReader = fileReader;
        this.profileDirectory = profileDirectory;
    }

    @Override
    public ConstraintDTO deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException
    {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode node = mapper.readTree(jsonParser);
        ConstraintType type = Arrays.stream(ConstraintType.values())
            .filter(constraintType -> node.has(constraintType.propertyName))
            .findFirst().orElse(ConstraintType.INVALID);
        switch (type)
        {
            case INVALID:
                return new InvalidConstraintDTO(node.toString());
            case IN_SET:
                JsonNode inSetNode = node.get(InSetConstraintDTO.NAME);
                return (inSetNode.isNull() || inSetNode.isArray())
                    ? mapper.treeToValue(node, InSetConstraintDTO.class)
                    : map(mapper.treeToValue(node, InSetFromFileConstraintDTO.class));
            case IN_MAP:
                return map(mapper.treeToValue(node, InMapFromFileConstraintDTO.class));
            default:
                return mapper.treeToValue(node, type.clazz);
        }
    }

    private InMapConstraintDTO map(InMapFromFileConstraintDTO dto)
    {
        List<Object> values = fileReader.listFromMapFile(getFile(dto.file), dto.key).stream().collect(Collectors.toList());
        InMapConstraintDTO inMapConstraintDTO = new InMapConstraintDTO();
        inMapConstraintDTO.field = dto.field;
        inMapConstraintDTO.otherField = dto.file;
        inMapConstraintDTO.values = values;
        return inMapConstraintDTO;
    }

    private InSetConstraintDTO map(InSetFromFileConstraintDTO dto)
    {
        List<Object> values = new ArrayList<>(fileReader.setFromFile(getFile(dto.file)).distributedList());
        InSetConstraintDTO inSetConstraintDTO = new InSetConstraintDTO();
        inSetConstraintDTO.field = dto.field;
        inSetConstraintDTO.values = values;
        return inSetConstraintDTO;
    }

    private File getFile(String fileName) {
        return Paths.get(profileDirectory.toString(), fileName).toFile();
    }
}

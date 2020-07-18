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

package com.scottlogic.datahelix.generator.profile.creation;

import com.scottlogic.datahelix.generator.common.profile.StandardSpecificFieldType;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;

public class FieldDTOBuilder
{
    private final String name;
    private final String type;
    private final String formatting;
    private final boolean unique;
    private final boolean nullable;

    private FieldDTOBuilder(String name, String type, String formatting, boolean unique, boolean nullable)
    {
        this.name = name;
        this.type = type;
        this.formatting = formatting;
        this.unique = unique;
        this.nullable = nullable;
    }

    private FieldDTOBuilder(String name, String type)
    {
        this(name, type, null, false, false);
    }

    public FieldDTO build()
    {
        FieldDTO fieldDTO = new FieldDTO();
        fieldDTO.name = name;
        fieldDTO.type = type;
        fieldDTO.formatting = formatting;
        fieldDTO.unique = unique;
        fieldDTO.nullable = nullable;
        return fieldDTO;
    }

    public FieldDTOBuilder withUniqueness()
    {
        return new FieldDTOBuilder(name, type, formatting, true, nullable);
    }

    public FieldDTOBuilder withNullability()
    {
        return new FieldDTOBuilder(name, type, formatting, unique, true);
    }

    public FieldDTOBuilder withFormatting(String formatting)
    {
        return new FieldDTOBuilder(name, type, formatting, unique, nullable);
    }

    public static FieldDTOBuilder fieldDTO(String name, StandardSpecificFieldType type)
    {
        return new FieldDTOBuilder(name, type == null ? null : type.toSpecificFieldType().getType());
    }

    public static FieldDTOBuilder fieldDTOWithStringType(String name)
    {
        return FieldDTOBuilder.fieldDTO(name, StandardSpecificFieldType.STRING);
    }

}

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

import com.scottlogic.datahelix.generator.common.profile.SpecificFieldType;
import com.scottlogic.datahelix.generator.common.profile.StandardSpecificFieldType;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;

public class FieldDTOBuilder
{
    public static FieldDTO create(String name)
    {
        FieldDTO fieldDTO = new FieldDTO();
        fieldDTO.name = name;
        fieldDTO.type = StandardSpecificFieldType.STRING.toSpecificFieldType().getType();
        return fieldDTO;
    }
    public static FieldDTO create(String name, SpecificFieldType type)
    {
        FieldDTO fieldDTO = new FieldDTO();
        fieldDTO.name = name;
        fieldDTO.type = type == null ? null : type.getType();
        return fieldDTO;
    }
}

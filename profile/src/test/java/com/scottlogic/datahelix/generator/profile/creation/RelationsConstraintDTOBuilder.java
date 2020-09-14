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

import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.AfterFieldConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.EqualToFieldConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.GreaterThanFieldConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.relations.InMapConstraintDTO;

import java.util.List;

public class RelationsConstraintDTOBuilder
{
    private final String field;
    private final int offset;
    private final String offsetUnit;

    private RelationsConstraintDTOBuilder(String field, int offset, String offsetUnit)
    {
        this.field = field;
        this.offset = offset;
        this.offsetUnit = offsetUnit;
    }

    public static RelationsConstraintDTOBuilder relationsConstraintDTO(String field)
    {
        return new RelationsConstraintDTOBuilder(field, 0, null);
    }

    public RelationsConstraintDTOBuilder withOffset(int offset)
    {
        return withOffset(offset, null);
    }

    public RelationsConstraintDTOBuilder withOffset(int offset, String offsetUnit)
    {
        return new RelationsConstraintDTOBuilder(field, offset, offsetUnit);
    }

    public AfterFieldConstraintDTO afterField(String otherField)
    {
        AfterFieldConstraintDTO dto = new AfterFieldConstraintDTO();
        dto.field = field;
        dto.offset = offset;
        dto.offsetUnit = offsetUnit;
        dto.otherField = otherField;
        return dto;
    }

    public EqualToFieldConstraintDTO equalToField(String otherField)
    {
        EqualToFieldConstraintDTO dto = new EqualToFieldConstraintDTO();
        dto.field = field;
        dto.offset = offset;
        dto.offsetUnit = offsetUnit;
        dto.otherField = otherField;
        return dto;
    }

    public GreaterThanFieldConstraintDTO greaterThanField(String otherField)
    {
        GreaterThanFieldConstraintDTO dto = new GreaterThanFieldConstraintDTO();
        dto.field = field;
        dto.offset = offset;
        dto.offsetUnit = offsetUnit;
        dto.otherField = otherField;
        return dto;
    }

    public InMapConstraintDTO inMap(List<Object> values)
    {
        InMapConstraintDTO dto = new InMapConstraintDTO();
        dto.field = field;
        dto.offset = offset;
        dto.offsetUnit = offsetUnit;
        dto.values = values;
        return dto;
    }
}

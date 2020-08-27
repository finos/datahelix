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

import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.EqualToConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.GranularToConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.InSetConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.IsNullConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.integer.LongerThanConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.integer.OfLengthConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.integer.ShorterThanConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.numeric.LessThanConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.temporal.AfterConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.atomic.textual.ContainsRegexConstraintDTO;

import java.util.List;

public class AtomicConstraintDTOBuilder
{
    private final String field;

    private AtomicConstraintDTOBuilder(String field)
    {
        this.field = field;
    }

    public static AtomicConstraintDTOBuilder atomicConstraintDTO(String field)
    {
        return new AtomicConstraintDTOBuilder(field);
    }

    public AfterConstraintDTO buildAfter(String value)
    {
        AfterConstraintDTO dto = new AfterConstraintDTO();
        dto.field = field;
        dto.value = value;
        return dto;
    }

    public EqualToConstraintDTO buildEqualTo(Object value)
    {
        EqualToConstraintDTO dto = new EqualToConstraintDTO();
        dto.field = field;
        dto.value = value;
        return dto;
    }

    public GranularToConstraintDTO buildGranularTo(Object value)
    {
        GranularToConstraintDTO dto = new GranularToConstraintDTO();
        dto.field = field;
        dto.value = value;
        return dto;
    }

    public InSetConstraintDTO buildInSet(List<Object> values)
    {
        InSetConstraintDTO dto = new InSetConstraintDTO();
        dto.field = field;
        dto.values = values;
        return dto;
    }

    public IsNullConstraintDTO buildIsNull(boolean isNull)
    {
        IsNullConstraintDTO dto = new IsNullConstraintDTO();
        dto.field = field;
        dto.isNull = isNull;
        return dto;
    }

    public LongerThanConstraintDTO buildLongerThan(int length)
    {
        LongerThanConstraintDTO dto = new LongerThanConstraintDTO();
        dto.field = field;
        dto.value = length;
        return dto;
    }

    public LessThanConstraintDTO buildLessThan(Number value)
    {
        LessThanConstraintDTO dto = new LessThanConstraintDTO();
        dto.field = field;
        dto.value = value;
        return dto;
    }

    public OfLengthConstraintDTO buildOfLength(int length)
    {
        OfLengthConstraintDTO dto = new OfLengthConstraintDTO();
        dto.field = field;
        dto.value = length;
        return dto;
    }

    public ContainsRegexConstraintDTO buildContainsRegex(String value)
    {
        ContainsRegexConstraintDTO dto = new ContainsRegexConstraintDTO();
        dto.field = field;
        dto.value = value;
        return dto;
    }

    public ShorterThanConstraintDTO buildShorterThan(int value)
    {
        ShorterThanConstraintDTO dto = new ShorterThanConstraintDTO();
        dto.field = field;
        dto.value = value;
        return dto;
    }
}

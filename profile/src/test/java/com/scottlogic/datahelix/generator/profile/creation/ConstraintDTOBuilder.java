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

import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.grammatical.AllOfConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.grammatical.AnyOfConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.grammatical.ConditionalConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.grammatical.NotConstraintDTO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConstraintDTOBuilder
{
    public static AllOfConstraintDTO allOf(ConstraintDTO... constraints)
    {
        return allOfValue(Arrays.stream(constraints).collect(Collectors.toList()));
    }

    public static AllOfConstraintDTO allOfValue(List<ConstraintDTO> constraints)
    {
        AllOfConstraintDTO dto = new AllOfConstraintDTO();
        dto.constraints = constraints;
        return dto;
    }

    public static AnyOfConstraintDTO anyOf(ConstraintDTO... constraints)
    {
        return anyOfValue(Arrays.stream(constraints).collect(Collectors.toList()));
    }

    public static AnyOfConstraintDTO anyOfValue(List<ConstraintDTO> constraints)
    {
        AnyOfConstraintDTO dto = new AnyOfConstraintDTO();
        dto.constraints = constraints;
        return dto;
    }

    public static ConditionalConstraintDTO conditional(ConstraintDTO ifConstraint, ConstraintDTO thenConstraint)
    {
        return conditional(ifConstraint, thenConstraint, null);
    }

    public static ConditionalConstraintDTO conditional(ConstraintDTO ifConstraint,
                                                       ConstraintDTO thenConstraint,
                                                       ConstraintDTO elseConstraint)
    {
        ConditionalConstraintDTO dto = new ConditionalConstraintDTO();
        dto.ifConstraint = ifConstraint;
        dto.thenConstraint = thenConstraint;
        dto.elseConstraint = elseConstraint;
        return dto;
    }

    public static NotConstraintDTO not(ConstraintDTO constraint)
    {
        NotConstraintDTO dto = new NotConstraintDTO();
        dto.constraint = constraint;
        return dto;
    }

}

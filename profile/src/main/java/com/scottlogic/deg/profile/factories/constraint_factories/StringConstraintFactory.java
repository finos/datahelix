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


package com.scottlogic.deg.profile.factories.constraint_factories;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.HelixStringLength;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.dtos.constraints.atomic.GranularToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.numeric.*;
import com.scottlogic.deg.profile.dtos.constraints.atomic.temporal.AfterConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.temporal.AfterOrAtConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.temporal.BeforeConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.temporal.BeforeOrAtConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.textual.ContainsRegexConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.textual.MatchesRegexConstraintDTO;

import java.util.regex.Pattern;

public class StringConstraintFactory extends AtomicConstraintFactory
{
    @Override
    Object parseValue(Object value)
    {
        return value;
    }

    @Override
    MatchesRegexConstraint createMatchesRegexConstraint(MatchesRegexConstraintDTO dto, Field field) {
        return new MatchesRegexConstraint(field, createPattern(dto.value));
    }

    @Override
    ContainsRegexConstraint createContainsRegexConstraint(ContainsRegexConstraintDTO dto, Field field) {
        return new ContainsRegexConstraint(field, createPattern(dto.value));
    }

    @Override
    OfLengthConstraint createOfLengthConstraint(OfLengthConstraintDTO dto, Field field) {
        return new OfLengthConstraint(field, HelixStringLength.create(dto.value));
    }

    @Override
    ShorterThanConstraint createShorterThanConstraint(ShorterThanConstraintDTO dto, Field field) {
        return new ShorterThanConstraint(field, HelixStringLength.create(dto.value));
    }

    @Override
    LongerThanConstraint createLongerThanConstraint(LongerThanConstraintDTO dto, Field field) {
        return new LongerThanConstraint(field, HelixStringLength.create(dto.value));
    }

    @Override
    GreaterThanConstraint createGreaterThanConstraint(GreaterThanConstraintDTO dto, Field field)
    {
        return null;
    }

    @Override
    GreaterThanOrEqualToConstraint createGreaterThanOrEqualToConstraint(GreaterThanOrEqualToConstraintDTO dto, Field field)
    {
        return null;
    }

    @Override
    LessThanConstraint createLessThanConstraint(LessThanConstraintDTO dto, Field field)
    {
        return null;
    }

    @Override
    LessThanOrEqualToConstraint createLessThanOrEqualToConstraint(LessThanOrEqualToConstraintDTO dto, Field field)
    {
        return null;
    }

    @Override
    AfterConstraint createAfterConstraint(AfterConstraintDTO dto, Field field)
    {
        return null;
    }

    @Override
    AfterOrAtConstraint createAfterOrAtConstraint(AfterOrAtConstraintDTO dto, Field field)
    {
        return null;
    }

    @Override
    BeforeConstraint createBeforeConstraint(BeforeConstraintDTO dto, Field field)
    {
        return null;
    }

    @Override
    BeforeOrAtConstraint createBeforeOrAtConstraint(BeforeOrAtConstraintDTO dto, Field field)
    {
        return null;
    }

    @Override
    AtomicConstraint createGranularToConstraint(GranularToConstraintDTO dto, Field field)
    {
        return null;
    }

    private Pattern createPattern(Object value)
    {
        return value instanceof Pattern ? (Pattern) value : Pattern.compile((String) value);
    }
}


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
import com.scottlogic.deg.common.profile.HelixTime;
import com.scottlogic.deg.common.profile.TimeGranularity;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.dtos.constraints.atomic.GranularToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.integer.LongerThanConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.integer.OfLengthConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.integer.ShorterThanConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.numeric.GreaterThanConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.numeric.GreaterThanOrEqualToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.numeric.LessThanConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.numeric.LessThanOrEqualToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.temporal.AfterConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.temporal.AfterOrAtConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.temporal.BeforeConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.temporal.BeforeOrAtConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.textual.ContainsRegexConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.textual.MatchesRegexConstraintDTO;

public class TimeConstraintFactory extends AtomicConstraintFactory {

    @Override
    AtomicConstraint createAfterOrAtConstraint(AfterOrAtConstraintDTO dto, Field field) {
        return new AfterOrEqualToConstantTimeConstraint(field, HelixTime.create(dto.value));
    }

    @Override
    AtomicConstraint createAfterConstraint(AfterConstraintDTO dto, Field field) {
        return new AfterConstantTimeConstraint(field, HelixTime.create(dto.value));
    }

    @Override
    AtomicConstraint createBeforeOrAtConstraint(BeforeOrAtConstraintDTO dto, Field field) {
        return new BeforeOrEqualToConstantTimeConstraint(field, HelixTime.create(dto.value));
    }

    @Override
    AtomicConstraint createBeforeConstraint(BeforeConstraintDTO dto, Field field) {
        return new BeforeConstantTimeConstraint(field, HelixTime.create(dto.value));
    }

    @Override
    AtomicConstraint createGranularToConstraint(GranularToConstraintDTO dto, Field field) {
        return new GranularToTimeConstraint(field, TimeGranularity.create((String) dto.value));
    }

    @Override
    Object parseValue(Object value) {
        return HelixTime.create((String) value).getValue();
    }

    @Override
    MatchesRegexConstraint createMatchesRegexConstraint(MatchesRegexConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    ContainsRegexConstraint createContainsRegexConstraint(ContainsRegexConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    OfLengthConstraint createOfLengthConstraint(OfLengthConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    ShorterThanConstraint createShorterThanConstraint(ShorterThanConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    LongerThanConstraint createLongerThanConstraint(LongerThanConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    GreaterThanConstraint createGreaterThanConstraint(GreaterThanConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    GreaterThanOrEqualToConstraint createGreaterThanOrEqualToConstraint(GreaterThanOrEqualToConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    LessThanConstraint createLessThanConstraint(LessThanConstraintDTO dto, Field field) {
        return null;
    }

    @Override
    LessThanOrEqualToConstraint createLessThanOrEqualToConstraint(LessThanOrEqualToConstraintDTO dto, Field field) {
        return null;
    }

}

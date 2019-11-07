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


import com.scottlogic.deg.profile.dtos.constraints.atomic.EqualToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.GranularToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.InSetConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.IsNullConstraintDTO;
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
import com.scottlogic.deg.profile.dtos.constraints.grammatical.AllOfConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.AnyOfConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.ConditionalConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.NotConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.relations.*;

import java.util.Arrays;

public enum ConstraintType {
    EQUAL_TO(EqualToConstraintDTO.NAME, EqualToConstraintDTO.class),
    EQUAL_TO_FIELD(EqualToFieldConstraintDTO.NAME, EqualToFieldConstraintDTO.class),
    IN_SET(InSetConstraintDTO.NAME, InSetConstraintDTO.class),
    IN_MAP(InMapConstraintDTO.NAME, InMapConstraintDTO.class),
    IS_NULL(IsNullConstraintDTO.NAME, IsNullConstraintDTO.class),
    GRANULAR_TO(GranularToConstraintDTO.NAME, GranularToConstraintDTO.class),
    MATCHES_REGEX(MatchesRegexConstraintDTO.NAME, MatchesRegexConstraintDTO.class),
    CONTAINS_REGEX(ContainsRegexConstraintDTO.NAME, ContainsRegexConstraintDTO.class),
    OF_LENGTH(OfLengthConstraintDTO.NAME, OfLengthConstraintDTO.class),
    LONGER_THAN(LongerThanConstraintDTO.NAME, LongerThanConstraintDTO.class),
    SHORTER_THAN(ShorterThanConstraintDTO.NAME, ShorterThanConstraintDTO.class),
    GREATER_THAN(GreaterThanConstraintDTO.NAME, GreaterThanConstraintDTO.class),
    GREATER_THAN_FIELD(GreaterThanFieldConstraintDTO.NAME, GreaterThanFieldConstraintDTO.class),
    GREATER_THAN_OR_EQUAL_TO(GreaterThanOrEqualToConstraintDTO.NAME, GreaterThanOrEqualToConstraintDTO.class),
    GREATER_THAN_OR_EQUAL_TO_FIELD(GreaterThanOrEqualToFieldConstraintDTO.NAME, GreaterThanOrEqualToFieldConstraintDTO.class),
    LESS_THAN(LessThanConstraintDTO.NAME, LessThanConstraintDTO.class),
    LESS_THAN_FIELD(LessThanFieldConstraintDTO.NAME, LessThanFieldConstraintDTO.class),
    LESS_THAN_OR_EQUAL_TO(LessThanOrEqualToConstraintDTO.NAME, LessThanOrEqualToConstraintDTO.class),
    LESS_THAN_OR_EQUAL_TO_FIELD(LessThanOrEqualToFieldConstraintDTO.NAME, LessThanOrEqualToFieldConstraintDTO.class),
    AFTER(AfterConstraintDTO.NAME, AfterConstraintDTO.class),
    AFTER_FIELD(AfterFieldConstraintDTO.NAME, AfterFieldConstraintDTO.class),
    AFTER_OR_AT(AfterOrAtConstraintDTO.NAME, AfterOrAtConstraintDTO.class),
    AFTER_OR_AT_FIELD(AfterOrAtFieldConstraintDTO.NAME, AfterOrAtFieldConstraintDTO.class),
    BEFORE(BeforeConstraintDTO.NAME, BeforeConstraintDTO.class),
    BEFORE_FIELD(BeforeFieldConstraintDTO.NAME, BeforeFieldConstraintDTO.class),
    BEFORE_OR_AT(BeforeOrAtConstraintDTO.NAME, BeforeOrAtConstraintDTO.class),
    BEFORE_OR_AT_FIELD(BeforeOrAtFieldConstraintDTO.NAME, BeforeOrAtFieldConstraintDTO.class),
    NOT(NotConstraintDTO.NAME, NotConstraintDTO.class),
    ANY_OF(AnyOfConstraintDTO.NAME, AnyOfConstraintDTO.class),
    ALL_OF(AllOfConstraintDTO.NAME, AllOfConstraintDTO.class),
    IF(ConditionalConstraintDTO.NAME, ConditionalConstraintDTO.class),
    INVALID(InvalidConstraintDTO.NAME, InvalidConstraintDTO.class);


    public final String propertyName;
    public final Class<? extends ConstraintDTO> clazz;

    ConstraintType(String propertyName, Class<? extends ConstraintDTO> clazz) {

        this.propertyName = propertyName;
        this.clazz = clazz;
    }

    public static ConstraintType fromName(String name) {
        return Arrays.stream(values())
            .filter(x->x.propertyName.equalsIgnoreCase(name))
            .findFirst().orElse(null);
    }

    public static ConstraintType fromClass(Class<? extends ConstraintDTO> clazz) {
        return Arrays.stream(values())
            .filter(x->x.clazz.equals(clazz))
            .findFirst().orElse(null);
    }
}


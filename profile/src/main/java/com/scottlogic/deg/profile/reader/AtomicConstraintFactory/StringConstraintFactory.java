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


package com.scottlogic.deg.profile.reader.AtomicConstraintFactory;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.HelixStringLength;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.dtos.constraints.*;
import com.scottlogic.deg.profile.reader.FileReader;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

class StringConstraintFactory extends AtomicConstraintFactory {

    @Inject
    StringConstraintFactory(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    @NotNull
    Constraint getIsLongerThanConstraint(LongerThanConstraintDTO dto, Field field) {
        return new IsStringLongerThanConstraint(field, HelixStringLength.create(dto.value));
    }

    @Override
    @NotNull
    Constraint getShorterThanConstraint(ShorterThanConstraintDTO dto, Field field) {
        return new IsStringShorterThanConstraint(field, HelixStringLength.create(dto.value));
    }

    @Override
    @NotNull
    Constraint getOfLengthConstraint(OfLengthConstraintDTO dto, Field field) {
        return new StringHasLengthConstraint(field, HelixStringLength.create(dto.value));
    }

    @Override
    @NotNull
    Constraint getContainsRegexConstraint(ContainsRegexConstraintDTO dto, Field field) {
        return new ContainsRegexConstraint(field, readPattern(dto.value));
    }

    @Override
    @NotNull
    Constraint getMatchesRegexConstraint(MatchesRegexConstraintDTO dto, Field field) {
        return new MatchesRegexConstraint(field, readPattern(dto.value));
    }

    private Pattern readPattern(Object value) {
        if (value instanceof Pattern) return (Pattern) value;
        try {
            return Pattern.compile((String) value);
        } catch (IllegalArgumentException e) {
            throw new InvalidProfileException(e.getMessage());
        }
    }
}


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
import com.scottlogic.deg.common.profile.HelixNumber;
import com.scottlogic.deg.common.profile.NumericGranularity;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.dtos.constraints.*;
import com.scottlogic.deg.profile.reader.FileReader;
import org.jetbrains.annotations.NotNull;

class NumericConstraintFactory extends AtomicConstraintFactory {

    @Inject
    NumericConstraintFactory(FileReader fileReader) {
        super(fileReader);
    }

    @NotNull
    @Override
    Constraint getGranularToConstraint(GranularToConstraintDTO dto, Field field) {
        return new IsGranularToNumericConstraint(field, NumericGranularity.create(dto.value));
    }

    @NotNull
    @Override
    Constraint getLessThanOrEqualToConstraint(LessThanOrEqualToConstraintDTO dto, Field field) {
        return new IsLessThanOrEqualToConstantConstraint(field, HelixNumber.create(dto.value));
    }

    @NotNull
    @Override
    Constraint getIsLessThanConstraint(LessThanConstraintDTO dto, Field field) {
        return new IsLessThanConstantConstraint(field, HelixNumber.create(dto.value));
    }

    @NotNull
    @Override
    Constraint getIsGreaterThanOrEqualToConstraint(GreaterThanOrEqualToConstraintDTO dto, Field field) {
        return new IsGreaterThanOrEqualToConstantConstraint(field, HelixNumber.create(dto.value));
    }

    @NotNull
    @Override
    Constraint getIsGreaterThanConstraint(GreaterThanConstraintDTO dto, Field field) {
        return new IsGreaterThanConstantConstraint(field, HelixNumber.create(dto.value));
    }
}

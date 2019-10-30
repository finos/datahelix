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
import com.scottlogic.deg.common.profile.DateTimeGranularity;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.HelixDateTime;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.dtos.constraints.*;
import com.scottlogic.deg.profile.reader.FileReader;
import org.jetbrains.annotations.NotNull;

class DateTimeConstraintFactory extends AtomicConstraintFactory {

    @Inject
    DateTimeConstraintFactory(FileReader fileReader) {
        super(fileReader);
    }

    @Override
    Constraint getAfterOrAtConstraint(AfterOrAtConstraintDTO dto, Field field) {
        return new IsAfterOrEqualToConstantDateTimeConstraint(field, HelixDateTime.create(dto.value));
    }

    @Override
    Constraint getAfterConstraint(AfterConstraintDTO dto, Field field) {
        return new IsAfterConstantDateTimeConstraint(field, HelixDateTime.create(dto.value));
    }

    @Override
    Constraint getBeforeOrAtConstraint(BeforeOrAtConstraintDTO dto, Field field) {
        return new IsBeforeOrEqualToConstantDateTimeConstraint(field, HelixDateTime.create(dto.value));
    }

    @Override
    Constraint getBeforeConstraint(BeforeConstraintDTO dto, Field field) {
        return new IsBeforeConstantDateTimeConstraint(field, HelixDateTime.create(dto.value));
    }

    @Override
    Constraint getGranularToConstraint(GranularToConstraintDTO dto, Field field) {
        return new IsGranularToDateConstraint(field, DateTimeGranularity.create((String) dto.value));
    }

}

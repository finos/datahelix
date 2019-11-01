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

package com.scottlogic.deg.profile.reader.services.constraints.atomic;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.profile.dtos.constraints.atomic.AtomicConstraintDTO;
import com.scottlogic.deg.profile.reader.FileReader;

public class AtomicConstraintService
{
    private final FileReader fileReader;

    @Inject
    public AtomicConstraintService(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public AtomicConstraint create(AtomicConstraintDTO dto, Fields fields)
    {
        AtomicConstraintFactory constraintFactory;
        FieldType fieldType = fields.getByName(dto.field).getType();
        switch(fieldType) {
            case STRING:
                constraintFactory = new StringConstraintFactory(fileReader);
                break;
            case DATETIME:
                constraintFactory = new DateTimeConstraintFactory(fileReader);
                break;
            case NUMERIC:
                constraintFactory = new NumericConstraintFactory(fileReader);
                break;
            default:
                throw new UnsupportedOperationException("No constraint factory for type " + fieldType);
        }
        return constraintFactory.createAtomicConstraint(dto, fields);
    }
}

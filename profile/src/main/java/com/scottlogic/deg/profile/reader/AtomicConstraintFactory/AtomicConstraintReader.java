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
import com.scottlogic.deg.common.profile.*;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.generator.fieldspecs.relations.InMapRelation;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.dtos.constraints.*;
import com.scottlogic.deg.profile.reader.AtomicConstraintFactory.AtomicConstraintFactory;
import com.scottlogic.deg.profile.reader.AtomicConstraintFactory.StringConstraintFactory;
import com.scottlogic.deg.profile.reader.FileReader;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import org.jetbrains.annotations.Nullable;

import javax.activation.UnsupportedDataTypeException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AtomicConstraintReader {

    private final FileReader fileReader;

    @Inject
    public AtomicConstraintReader(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public Constraint readAtomicConstraintDto(AtomicConstraintDTO dto, ProfileFields profileFields) {

        AtomicConstraintFactory constraintFactory;

        FieldType fieldType = profileFields.getByName(dto.field).getType();
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
        return constraintFactory.readAtomicConstraintDto(dto, profileFields);
    }
}

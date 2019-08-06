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

package com.scottlogic.deg.profile.reader;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;
import com.scottlogic.deg.profile.v0_1.ConstraintDTO;

import java.util.Map;

public class MainConstraintReader implements ConstraintReader {
    private final Map<AtomicConstraintType, ConstraintReader> constraintReaderMap;

    @Inject
    public MainConstraintReader(AtomicConstraintTypeReaderMap constraintReaderMap) {
        this.constraintReaderMap = constraintReaderMap.getConstraintReaderMapEntries();
    }

    @Override
    public Constraint apply(
        ConstraintDTO dto,
        ProfileFields fields) {

        if (dto == null) {
            throw new InvalidProfileException("Constraint is null");
        }

        if (dto.is == null) {
            throw new InvalidProfileException("Couldn't recognise 'is' property, it must be set to a value");
        }

        if (dto.is != ConstraintDTO.undefined) {
            ConstraintReader subReader = constraintReaderMap.get(AtomicConstraintType.fromText((String) dto.is));

            if (subReader == null) {
                throw new InvalidProfileException("Couldn't recognise constraint type from DTO: " + dto.is);
            }

            try {
                return subReader.apply(dto, fields);
            } catch (IllegalArgumentException e) {
                throw new InvalidProfileException(e.getMessage());
            }
        }

        if (dto.not != null) {
            return this.apply(dto.not, fields).negate();
        }

        if (dto.allOf != null) {
            if (dto.allOf.isEmpty()) {
                throw new InvalidProfileException("AllOf must contain at least one constraint.");
            }
            return new AndConstraint(
                JsonProfileReader.mapDtos(
                    dto.allOf,
                    subConstraintDto -> this.apply(
                        subConstraintDto,
                        fields
                    )));
        }

        if (dto.anyOf != null) {
            return new OrConstraint(
                JsonProfileReader.mapDtos(
                    dto.anyOf,
                    subConstraintDto -> this.apply(
                        subConstraintDto,
                        fields
                    )));
        }

        if (dto.if_ != null) {
            return new ConditionalConstraint(
                this.apply(
                    dto.if_,
                    fields
                ),
                this.apply(
                    dto.then,
                    fields
                ),
                dto.else_ != null
                    ? this.apply(
                        dto.else_,
                        fields
                )
                    : null);
        }

        throw new InvalidProfileException("Couldn't interpret constraint");
    }
}

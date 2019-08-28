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
import com.scottlogic.deg.profile.dto.AtomicConstraintType;
import com.scottlogic.deg.profile.dto.ConstraintDTO;

import java.util.stream.Collectors;

public class MainConstraintReader {

    private final AtomicConstraintTypeReaderMap constraintReaderMap;

    @Inject
    public MainConstraintReader(AtomicConstraintTypeReaderMap constraintReaderMap) {
        this.constraintReaderMap = constraintReaderMap;
    }

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
            AtomicConstraintReader subReader = constraintReaderMap.getConstraintReaderMapEntries()
                .get(AtomicConstraintType.fromText((String) dto.is));

            if (subReader == null) {
                String message = "Couldn't recognise constraint type from DTO: " + dto.is;
                String delayedMessage = constraintReaderMap.isDelayedConstraintsEnabled()
                    ? message
                    : message + ". Relational constraints are disabled for the current chosen walker.";
                throw new InvalidProfileException(delayedMessage);
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
                dto.allOf.stream()
                    .map(subConstraintDto -> this.apply(subConstraintDto, fields))
                    .collect(Collectors.toSet()));
        }

        if (dto.anyOf != null) {
            return new OrConstraint(
                dto.anyOf.stream()
                    .map(subConstraintDto -> this.apply(subConstraintDto, fields))
                    .collect(Collectors.toSet()));
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

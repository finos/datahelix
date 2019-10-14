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
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.relations.InMapRelation;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.common.profile.constraintdetail.AtomicConstraintType;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.profile.dto.ConstraintDTO;
import com.scottlogic.deg.profile.reader.atomic.AtomicConstraintValueReader;
import com.scottlogic.deg.profile.reader.atomic.AtomicConstraintFactory;
import com.scottlogic.deg.profile.reader.atomic.ConstraintValueValidator;
import com.scottlogic.deg.profile.reader.atomic.RelationsFactory;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class MainConstraintReader {

    private final AtomicConstraintValueReader atomicConstraintValueReader;

    @Inject
    public MainConstraintReader(AtomicConstraintValueReader atomicConstraintValueReader) {
        this.atomicConstraintValueReader = atomicConstraintValueReader;
    }

    public Constraint apply(
        ConstraintDTO dto,
        ProfileFields fields) {

        if (dto == null) {
            throw new InvalidProfileException("Constraint is null");
        }

        if (dto.is != null) {

            if (dto.otherField != null){
                return RelationsFactory.create(dto, fields);
            }

            AtomicConstraintType atomicConstraintType = AtomicConstraintType.fromText((String) dto.is);

            Field field = fields.getByName(dto.field);

            Object value = atomicConstraintValueReader.getValue(dto, field.getType());

            ConstraintValueValidator.validate(field, atomicConstraintType, value);

            if (atomicConstraintType == AtomicConstraintType.IS_IN_MAP){
                return createInMapRelation(field, fields.getByName(dto.file), (DistributedList<String>) value);
            }

            return AtomicConstraintFactory.create(atomicConstraintType, field, value);

        }

        if (dto.not != null) {
            return this.apply(dto.not, fields).negate();
        }

        if (dto.allOf != null) {
            if (dto.allOf.isEmpty()) {
                throw new InvalidProfileException("AllOf must contain at least one constraint.");
            }
            Collection<ConstraintDTO> allOf = dto.allOf;
            return new AndConstraint(
                getSubConstraints(fields, allOf));
        }

        if (dto.anyOf != null) {
            return new OrConstraint(
                getSubConstraints(fields, dto.anyOf));
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

        throw new InvalidProfileException("Couldn't recognise 'is' property, it must be set to a value");
    }

    private InMapRelation createInMapRelation(Field field, Field other, DistributedList<String> list) {
        return new InMapRelation(field, other, list);
    }


    Set<Constraint> getSubConstraints(ProfileFields fields, Collection<ConstraintDTO> allOf) {
        return allOf.stream()
            .map(subConstraintDto -> apply(subConstraintDto, fields))
            .collect(Collectors.toSet());
    }
}

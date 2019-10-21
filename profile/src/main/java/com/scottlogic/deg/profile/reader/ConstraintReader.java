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
import com.scottlogic.deg.common.profile.*;
import com.scottlogic.deg.common.util.defaults.DateTimeDefaults;
import com.scottlogic.deg.common.util.defaults.NumericDefaults;
import com.scottlogic.deg.generator.fieldspecs.relations.*;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.generator.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.profile.dtos.constraints.*;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ConstraintReader {

    private final AtomicConstraintReader atomicConstraintReader;

    @Inject
    public ConstraintReader(AtomicConstraintReader atomicConstraintReader) {
        this.atomicConstraintReader = atomicConstraintReader;
    }

    public Set<Constraint> read(Collection<ConstraintDTO> constraints, ProfileFields fields) {
        return constraints.stream()
            .map(subConstraintDto -> read(subConstraintDto, fields))
            .collect(Collectors.toSet());
    }

    public Constraint read(ConstraintDTO dto, ProfileFields profileFields) {
        if (dto == null) {
            throw new InvalidProfileException("Constraint is null");
        } else if (dto instanceof RelationalConstraintDTO) {
            return readRelationalConstraintDto((RelationalConstraintDTO) dto, profileFields);
        } else if (dto instanceof AtomicConstraintDTO)
            return atomicConstraintReader.readAtomicConstraintDto((AtomicConstraintDTO) dto, profileFields);
        else {
            return readGrammaticalConstraintDto(dto, profileFields);
        }
    }

    private Constraint readRelationalConstraintDto(RelationalConstraintDTO dto, ProfileFields fields) {
        Field main = fields.getByName(dto.field);
        Field other = fields.getByName(dto.getOtherField());
        switch (dto.getType()) {
            case EQUAL_TO_FIELD:
                Granularity offsetGranularity = readGranularity(main.getType(), dto.offsetUnit);
                return offsetGranularity != null
                    ? new EqualToOffsetRelation(main, other, offsetGranularity, dto.offset)
                    : new EqualToRelation(main, other);
            case AFTER_FIELD:
                return new AfterRelation(main, other, false, DateTimeDefaults.get());
            case AFTER_OR_AT_FIELD:
                return new AfterRelation(main, other, true, DateTimeDefaults.get());
            case BEFORE_FIELD:
                return new BeforeRelation(main, other, false, DateTimeDefaults.get());
            case BEFORE_OR_AT_FIELD:
                return new BeforeRelation(main, other, true, DateTimeDefaults.get());
            case GREATER_THAN_FIELD:
                return new AfterRelation(main, other, false, NumericDefaults.get());
            case GREATER_THAN_OR_EQUAL_TO_FIELD:
                return new AfterRelation(main, other, true, NumericDefaults.get());
            case LESS_THAN_FIELD:
                return new BeforeRelation(main, other, false, NumericDefaults.get());
            case LESS_THAN_OR_EQUAL_TO_FIELD:
                return new BeforeRelation(main, other, true, NumericDefaults.get());
            default:
                throw new InvalidProfileException("Unexpected relation data type " + dto.getType());
        }
    }

    private Constraint readGrammaticalConstraintDto(ConstraintDTO dto, ProfileFields profileFields) {
        switch (dto.getType()) {
            case ALL_OF:
                return new AndConstraint(read(((AllOfConstraintDTO) dto).constraints, profileFields));
            case ANY_OF:
                return new OrConstraint(read(((AnyOfConstraintDTO) dto).constraints, profileFields));
            case IF:
                IfConstraintDTO conditionalConstraintDTO = (IfConstraintDTO) dto;
                Constraint ifConstraint = read(conditionalConstraintDTO.ifConstraint, profileFields);
                Constraint thenConstraint = read(conditionalConstraintDTO.thenConstraint, profileFields);
                Constraint elseConstraint = conditionalConstraintDTO.elseConstraint == null ? null
                    : read(conditionalConstraintDTO.elseConstraint, profileFields);
                return new ConditionalConstraint(ifConstraint, thenConstraint, elseConstraint);
            case NOT:
                return read(((NotConstraintDTO) dto).constraint, profileFields).negate();
            case NULL:
                return new IsNullConstraint(profileFields.getByName(((NullConstraintDTO) dto).field));
            default:
                throw new InvalidProfileException("Grammatical constraint type not found: " + dto);
        }
    }

    private Granularity readGranularity(FieldType type, String offsetUnit) {
        if (offsetUnit == null) return null;
        switch (type) {
            case NUMERIC:
                return NumericGranularity.create(offsetUnit);
            case DATETIME:
                return DateTimeGranularity.create(offsetUnit);
            default:
                return null;
        }
    }

}

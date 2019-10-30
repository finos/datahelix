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
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.generator.fieldspecs.relations.InMapRelation;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.dtos.constraints.*;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AtomicConstraintReader {

    private final FileReader fileReader;

    @Inject
    public AtomicConstraintReader(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public Constraint readAtomicConstraintDto(AtomicConstraintDTO dto, Fields fields) {
        Field field = fields.getByName(dto.field);
        switch (dto.getType()) {
            case EQUAL_TO:
                return new EqualToConstraint(field, readAnyType(field, ((EqualToConstraintDTO) dto).value));
            case IN_SET:
                InSetConstraintDTO inSetConstraintDTO = (InSetConstraintDTO) dto;
                return new IsInSetConstraint(field, prepareValuesForSet(inSetConstraintDTO, field));
            case IN_MAP:
                InMapConstraintDTO inMapConstraintDTO = (InMapConstraintDTO) dto;
                return new InMapRelation(field, fields.getByName(inMapConstraintDTO.file),
                    DistributedList.uniform(fileReader.listFromMapFile(inMapConstraintDTO.file, inMapConstraintDTO.key).stream()
                        .map(value -> readAnyType(field, value))
                        .collect(Collectors.toList())));
            case MATCHES_REGEX:
                return new MatchesRegexConstraint(field, readPattern(((MatchesRegexConstraintDTO) dto).value));
            case CONTAINS_REGEX:
                return new ContainsRegexConstraint(field, readPattern(((ContainsRegexConstraintDTO) dto).value));
            case OF_LENGTH:
                return new StringHasLengthConstraint(field, HelixStringLength.create(((OfLengthConstraintDTO) dto).value));
            case SHORTER_THAN:
                return new IsStringShorterThanConstraint(field, HelixStringLength.create(((ShorterThanConstraintDTO) dto).value));
            case LONGER_THAN:
                return new IsStringLongerThanConstraint(field, HelixStringLength.create(((LongerThanConstraintDTO) dto).value));
            case GREATER_THAN:
                return new IsGreaterThanConstantConstraint(field, HelixNumber.create(((GreaterThanConstraintDTO) dto).value));
            case GREATER_THAN_OR_EQUAL_TO:
                return new IsGreaterThanOrEqualToConstantConstraint(field, HelixNumber.create(((GreaterThanOrEqualToConstraintDTO) dto).value));
            case LESS_THAN:
                return new IsLessThanConstantConstraint(field, HelixNumber.create(((LessThanConstraintDTO) dto).value));
            case LESS_THAN_OR_EQUAL_TO:
                return new IsLessThanOrEqualToConstantConstraint(field, HelixNumber.create(((LessThanOrEqualToConstraintDTO) dto).value));
            case AFTER:
                return new IsAfterConstantDateTimeConstraint(field, HelixDateTime.create(((AfterConstraintDTO) dto).value));
            case AFTER_OR_AT:
                return new IsAfterOrEqualToConstantDateTimeConstraint(field, HelixDateTime.create(((AfterOrAtConstraintDTO) dto).value));
            case BEFORE:
                return new IsBeforeConstantDateTimeConstraint(field, HelixDateTime.create(((BeforeConstraintDTO) dto).value));
            case BEFORE_OR_AT:
                return new IsBeforeOrEqualToConstantDateTimeConstraint(field, HelixDateTime.create(((BeforeOrAtConstraintDTO) dto).value));
            case GRANULAR_TO:
                GranularToConstraintDTO granularToConstraintDTO = (GranularToConstraintDTO) dto;
                return granularToConstraintDTO.value instanceof Number
                    ? new IsGranularToNumericConstraint(field, NumericGranularity.create(granularToConstraintDTO.value))
                    : new IsGranularToDateConstraint(field, DateTimeGranularity.create((String) granularToConstraintDTO.value));
            case IS_NULL:
                IsNullConstraint isNullConstraint = new IsNullConstraint(fields.getByName(((NullConstraintDTO) dto).field));
                return ((NullConstraintDTO)dto).isNull
                    ? isNullConstraint
                    : isNullConstraint.negate();
            default:
                throw new InvalidProfileException("Atomic constraint type not found: " + dto);
        }
    }

    private DistributedList<Object> prepareValuesForSet(InSetConstraintDTO inSetConstraintDTO, Field field) {
        return (inSetConstraintDTO instanceof InSetFromFileConstraintDTO
            ? fileReader.setFromFile(((InSetFromFileConstraintDTO) inSetConstraintDTO).file)
            : DistributedList.uniform(((InSetOfValuesConstraintDTO) inSetConstraintDTO).values.stream()
            .distinct()
            .map(o -> readAnyType(field, o))
            .collect(Collectors.toList())));
    }

    @Nullable
    private Object readAnyType(Field field, Object value) {
        switch (field.getType()) {
            case DATETIME:
                return HelixDateTime.create((String) value).getValue();
            case NUMERIC:
                return NumberUtils.coerceToBigDecimal(value);
            default:
                return value;
        }
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

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

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.dtos.constraints.atomic.*;
import com.scottlogic.deg.profile.reader.FileReader;
import com.scottlogic.deg.profile.reader.InvalidProfileException;

import java.util.stream.Collectors;

abstract class AtomicConstraintFactory {

    private final FileReader fileReader;

    AtomicConstraintFactory(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    AtomicConstraint createAtomicConstraint(AtomicConstraintDTO dto, Fields fields) {
        Field field = fields.getByName(dto.field);
        switch (dto.getType()) {
            case EQUAL_TO:
                return new EqualToConstraint(field, parseValue(((EqualToConstraintDTO)dto).value));
            case IN_SET:
                return createInSetConstraint((InSetConstraintDTO) dto, field);
            case MATCHES_REGEX:
                return createMatchesRegexConstraint((MatchesRegexConstraintDTO) dto, field);
            case CONTAINS_REGEX:
                return createContainsRegexConstraint((ContainsRegexConstraintDTO) dto, field);
            case OF_LENGTH:
                return createOfLengthConstraint((OfLengthConstraintDTO) dto, field);
            case SHORTER_THAN:
                return createShorterThanConstraint((ShorterThanConstraintDTO) dto, field);
            case LONGER_THAN:
                return createLongerThanConstraint((LongerThanConstraintDTO) dto, field);
            case GREATER_THAN:
                return createGreaterThanConstraint((GreaterThanConstraintDTO) dto, field);
            case GREATER_THAN_OR_EQUAL_TO:
                return createGreaterThanOrEqualToConstraint((GreaterThanOrEqualToConstraintDTO) dto, field);
            case LESS_THAN:
                return createLessThanConstraint((LessThanConstraintDTO) dto, field);
            case LESS_THAN_OR_EQUAL_TO:
                return createLessThanOrEqualToConstraint((LessThanOrEqualToConstraintDTO) dto, field);
            case AFTER:
                return createAfterConstraint((AfterConstraintDTO) dto, field);
            case AFTER_OR_AT:
                return createAfterOrAtConstraint((AfterOrAtConstraintDTO) dto, field);
            case BEFORE:
                return createBeforeConstraint((BeforeConstraintDTO) dto, field);
            case BEFORE_OR_AT:
                return createBeforeOrAtConstraint((BeforeOrAtConstraintDTO) dto, field);
            case GRANULAR_TO:
                return createGranularToConstraint((GranularToConstraintDTO) dto, field);
            case IS_NULL:
                return createIsNullConstraint((IsNullConstraintDTO) dto, fields);
            default:
                throw new InvalidProfileException("Atomic constraint type not found: " + dto);
        }
    }

    abstract Object parseValue(Object value);
    abstract MatchesRegexConstraint createMatchesRegexConstraint(MatchesRegexConstraintDTO dto, Field field);
    abstract ContainsRegexConstraint createContainsRegexConstraint(ContainsRegexConstraintDTO dto, Field field);
    abstract OfLengthConstraint createOfLengthConstraint(OfLengthConstraintDTO dto, Field field);
    abstract ShorterThanConstraint createShorterThanConstraint(ShorterThanConstraintDTO dto, Field field);
    abstract LongerThanConstraint createLongerThanConstraint(LongerThanConstraintDTO dto, Field field);
    abstract GreaterThanConstraint createGreaterThanConstraint(GreaterThanConstraintDTO dto, Field field);
    abstract GreaterThanOrEqualToConstraint createGreaterThanOrEqualToConstraint(GreaterThanOrEqualToConstraintDTO dto, Field field);
    abstract LessThanConstraint createLessThanConstraint(LessThanConstraintDTO dto, Field field);
    abstract LessThanOrEqualToConstraint createLessThanOrEqualToConstraint(LessThanOrEqualToConstraintDTO dto, Field field);
    abstract AfterConstraint createAfterConstraint(AfterConstraintDTO dto, Field field);
    abstract AfterOrAtConstraint createAfterOrAtConstraint(AfterOrAtConstraintDTO dto, Field field);
    abstract BeforeConstraint createBeforeConstraint(BeforeConstraintDTO dto, Field field);
    abstract BeforeOrAtConstraint createBeforeOrAtConstraint(BeforeOrAtConstraintDTO dto, Field field);
    abstract AtomicConstraint createGranularToConstraint(GranularToConstraintDTO dto, Field field);

    private InSetConstraint createInSetConstraint(InSetConstraintDTO dto, Field field)
    {
        if (dto instanceof InSetFromFileConstraintDTO)
        {
            return new InSetConstraint(field, fileReader.setFromFile(((InSetFromFileConstraintDTO) dto).file));
        }
        if (dto instanceof InSetOfValuesConstraintDTO)
        {
            DistributedList<Object> values = DistributedList.uniform(((InSetOfValuesConstraintDTO) dto).values.stream()
                .distinct()
                .map(this::parseValue)
                .collect(Collectors.toList()));

            return new InSetConstraint(field, values);
        }
        throw new IllegalStateException("Unexpected value: " + dto.getType());
    }

    private AtomicConstraint createIsNullConstraint(IsNullConstraintDTO dto, Fields fields)
    {
        IsNullConstraint isNullConstraint = new IsNullConstraint(fields.getByName(dto.field));
        return dto.isNull ? isNullConstraint : isNullConstraint.negate();
    }
}

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
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.*;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.generator.fieldspecs.relations.InMapRelation;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.dtos.constraints.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.scottlogic.deg.common.profile.FieldType.*;
import static com.scottlogic.deg.profile.reader.validation.ConstraintTypeValidator.*;

public class AtomicConstraintReader {

    private final FileReader fileReader;

    @Inject
    public AtomicConstraintReader(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public Constraint readAtomicConstraintDto(AtomicConstraintDTO dto, ProfileFields profileFields) {
        Field field = profileFields.getByName(dto.field);

        switch (dto.getType()) {
            case EQUAL_TO:
                Object value = readAnyType(field, ((EqualToConstraintDTO) dto).value);
                validateAny(field, dto.getType(), value);
                return new EqualToConstraint(field, value);
            case IN_SET:
                InSetConstraintDTO inSetConstraintDTO = (InSetConstraintDTO) dto;
                DistributedList<Object> legalValues = prepareValuesForSet(inSetConstraintDTO, field);
                validateSet(field, dto.getType(), legalValues);
                return new IsInSetConstraint(field, legalValues);
            case IN_MAP:
                InMapConstraintDTO inMapConstraintDTO = (InMapConstraintDTO) dto;
                DistributedList<Object> underlyingList = readMap(field, inMapConstraintDTO);
                validateSet(field, dto.getType(), underlyingList);
                return new InMapRelation(field, profileFields.getByName(inMapConstraintDTO.file), underlyingList);
            case MATCHES_REGEX:
                validateTypeIs(field, dto.getType(), STRING);
                return new MatchesRegexConstraint(field, readPattern(((MatchesRegexConstraintDTO) dto).value));
            case CONTAINS_REGEX:
                validateTypeIs(field, dto.getType(), STRING);
                return new ContainsRegexConstraint(field, readPattern(((ContainsRegexConstraintDTO) dto).value));
            case OF_LENGTH:
                validateTypeIs(field, dto.getType(), STRING);
                return new StringHasLengthConstraint(field, HelixStringLength.create(((OfLengthConstraintDTO) dto).value));
            case SHORTER_THAN:
                validateTypeIs(field, dto.getType(), STRING);
                return new IsStringShorterThanConstraint(field, HelixStringLength.create(((ShorterThanConstraintDTO) dto).value));
            case LONGER_THAN:
                validateTypeIs(field, dto.getType(), STRING);
                return new IsStringLongerThanConstraint(field, HelixStringLength.create(((LongerThanConstraintDTO) dto).value));
            case GREATER_THAN:
                validateTypeIs(field, dto.getType(), NUMERIC);
                return new IsGreaterThanConstantConstraint(field, HelixNumber.create(((GreaterThanConstraintDTO) dto).value));
            case GREATER_THAN_OR_EQUAL_TO:
                validateTypeIs(field, dto.getType(), NUMERIC);
                return new IsGreaterThanOrEqualToConstantConstraint(field, HelixNumber.create(((GreaterThanOrEqualToConstraintDTO) dto).value));
            case LESS_THAN:
                validateTypeIs(field, dto.getType(), NUMERIC);
                return new IsLessThanConstantConstraint(field, HelixNumber.create(((LessThanConstraintDTO) dto).value));
            case LESS_THAN_OR_EQUAL_TO:
                validateTypeIs(field, dto.getType(), NUMERIC);
                return new IsLessThanOrEqualToConstantConstraint(field, HelixNumber.create(((LessThanOrEqualToConstraintDTO) dto).value));
            case AFTER:
                validateTypeIs(field, dto.getType(), DATETIME);
                return new IsAfterConstantDateTimeConstraint(field, HelixDateTime.create(((AfterConstraintDTO) dto).value));
            case AFTER_OR_AT:
                validateTypeIs(field, dto.getType(), DATETIME);
                return new IsAfterOrEqualToConstantDateTimeConstraint(field, HelixDateTime.create(((AfterOrAtConstraintDTO) dto).value));
            case BEFORE:
                validateTypeIs(field, dto.getType(), DATETIME);
                return new IsBeforeConstantDateTimeConstraint(field, HelixDateTime.create(((BeforeConstraintDTO) dto).value));
            case BEFORE_OR_AT:
                validateTypeIs(field, dto.getType(), DATETIME);
                return new IsBeforeOrEqualToConstantDateTimeConstraint(field, HelixDateTime.create(((BeforeOrAtConstraintDTO) dto).value));
            case GRANULAR_TO:
                GranularToConstraintDTO granularToConstraintDTO = (GranularToConstraintDTO) dto;
                if (granularToConstraintDTO.value instanceof Number) {
                    validateTypeIs(field, dto.getType(), NUMERIC);
                    return new IsGranularToNumericConstraint(field, NumericGranularity.create(granularToConstraintDTO.value));
                }
                else {
                    validateTypeIs(field, dto.getType(), DATETIME);
                    return new IsGranularToDateConstraint(field, DateTimeGranularity.create((String) granularToConstraintDTO.value));
                }
            case IS_NULL:
                IsNullConstraint isNullConstraint = new IsNullConstraint(profileFields.getByName(((NullConstraintDTO) dto).field));
                return ((NullConstraintDTO)dto).isNull
                    ? isNullConstraint
                    : isNullConstraint.negate();
            default:
                throw new InvalidProfileException("Atomic constraint type not found: " + dto);
        }
    }

    @NotNull
    private DistributedList<Object> readMap(Field field, InMapConstraintDTO inMapConstraintDTO) {
        return DistributedList.uniform(fileReader.listFromMapFile(inMapConstraintDTO.file, inMapConstraintDTO.key).stream()
            .map(value -> readAnyType(field, value))
            .collect(Collectors.toList()));
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
                if (!(value instanceof String)) {
                    throw new ValidationException("Field " + field.name + " is a datetime but you are trying to assign non datetime value");
                }
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

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
import com.scottlogic.deg.common.util.defaults.DateTimeDefaults;
import com.scottlogic.deg.common.util.defaults.NumericDefaults;
import com.scottlogic.deg.generator.fieldspecs.relations.*;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.generator.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.profile.dtos.constraints.*;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConstraintReader {

    private final FileReader fileReader;

    @Inject
    public ConstraintReader(FileReader fileReader) {
        this.fileReader = fileReader;
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
            return readAtomicConstraintDto((AtomicConstraintDTO) dto, profileFields);
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

    private Constraint readAtomicConstraintDto(AtomicConstraintDTO dto, ProfileFields profileFields) {
        Field field = profileFields.getByName(dto.field);
        switch (dto.getType()) {
            case EQUAL_TO:
                return new EqualToConstraint(field, readAnyType(field, ((EqualToConstraintDTO) dto).value));
            case IN_SET:
                InSetConstraintDTO inSetConstraintDTO = (InSetConstraintDTO) dto;
                return new IsInSetConstraint(field, prepareValuesForSet(inSetConstraintDTO, field));
            case IN_MAP:
                InMapConstraintDTO inMapConstraintDTO = (InMapConstraintDTO) dto;
                return new InMapRelation(field, profileFields.getByName(inMapConstraintDTO.file),
                    DistributedList.uniform(fileReader.listFromMapFile(inMapConstraintDTO.file, inMapConstraintDTO.key).stream()
                        .map(value -> readAnyType(field, value))
                        .collect(Collectors.toList())));
            case MATCHES_REGEX:
                return new MatchesRegexConstraint(profileFields.getByName(dto.field), readPattern(((MatchesRegexConstraintDTO) dto).value));
            case CONTAINS_REGEX:
                return new ContainsRegexConstraint(profileFields.getByName(dto.field), readPattern(((ContainsRegexConstraintDTO) dto).value));
            case OF_LENGTH:
                return new StringHasLengthConstraint(profileFields.getByName(dto.field), HelixStringLength.create(((OfLengthConstraintDTO) dto).value));
            case SHORTER_THAN:
                return new IsStringShorterThanConstraint(profileFields.getByName(dto.field), HelixStringLength.create(((ShorterThanConstraintDTO) dto).value));
            case LONGER_THAN:
                return new IsStringLongerThanConstraint(profileFields.getByName(dto.field), HelixStringLength.create(((LongerThanConstraintDTO) dto).value));
            case GREATER_THAN:
                return new IsGreaterThanConstantConstraint(profileFields.getByName(dto.field), HelixNumber.create(((GreaterThanConstraintDTO) dto).value));
            case GREATER_THAN_OR_EQUAL_TO:
                return new IsGreaterThanOrEqualToConstantConstraint(profileFields.getByName(dto.field), HelixNumber.create(((GreaterThanOrEqualToConstraintDTO) dto).value));
            case LESS_THAN:
                return new IsLessThanConstantConstraint(profileFields.getByName(dto.field), HelixNumber.create(((LessThanConstraintDTO) dto).value));
            case LESS_THAN_OR_EQUAL_TO:
                return new IsLessThanOrEqualToConstantConstraint(profileFields.getByName(dto.field), HelixNumber.create(((LessThanOrEqualToConstraintDTO) dto).value));
            case AFTER:
                return new IsAfterConstantDateTimeConstraint(profileFields.getByName(dto.field), HelixDateTime.create(((AfterConstraintDTO) dto).value));
            case AFTER_OR_AT:
                return new IsAfterOrEqualToConstantDateTimeConstraint(profileFields.getByName(dto.field), HelixDateTime.create(((AfterOrAtConstraintDTO) dto).value));
            case BEFORE:
                return new IsBeforeConstantDateTimeConstraint(profileFields.getByName(dto.field), HelixDateTime.create(((BeforeConstraintDTO) dto).value));
            case BEFORE_OR_AT:
                return new IsBeforeOrEqualToConstantDateTimeConstraint(profileFields.getByName(dto.field), HelixDateTime.create(((BeforeOrAtConstraintDTO) dto).value));
            case GRANULAR_TO:
                GranularToConstraintDTO granularToConstraintDTO = (GranularToConstraintDTO) dto;
                return granularToConstraintDTO.value instanceof Number
                    ? new IsGranularToNumericConstraint(profileFields.getByName(dto.field), NumericGranularity.create(granularToConstraintDTO.value))
                    : new IsGranularToDateConstraint(profileFields.getByName(dto.field), DateTimeGranularity.create((String) granularToConstraintDTO.value));
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

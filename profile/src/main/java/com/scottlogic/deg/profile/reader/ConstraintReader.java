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
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraintdetail.DateTimeGranularity;
import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularityFactory;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.generator.fieldspecs.relations.InMapRelation;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.generator.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.AtomicConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.relatable.*;
import com.scottlogic.deg.profile.dtos.constraints.atomic.unrelatable.*;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.*;
import com.scottlogic.deg.profile.reader.atomic.RelationsFactory;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConstraintReader
{
    private static final OffsetDateTime NOW = OffsetDateTime.now();
    private final FileReader fileReader;

    @Inject
    public ConstraintReader(FileReader fileReader)
    {
        this.fileReader = fileReader;
    }

    Set<Constraint> read(Collection<ConstraintDTO> constraints, ProfileFields fields)
    {
        return constraints.stream()
                .map(subConstraintDto -> read(subConstraintDto, fields))
                .collect(Collectors.toSet());
    }

    public Constraint read(ConstraintDTO dto, ProfileFields profileFields)
    {
        if (dto == null) throw new InvalidProfileException("Constraint is null");
        if (dto instanceof AtomicConstraintDTO)
        {
            AtomicConstraintDTO atomicConstraintDTO = (AtomicConstraintDTO) dto;
            Field field = profileFields.getByName(atomicConstraintDTO.field);
            if (atomicConstraintDTO.hasRelation()) return RelationsFactory.create((RelatableConstraintDTO)atomicConstraintDTO, profileFields);
            switch (atomicConstraintDTO.getType())
            {
                case EQUAL_TO:
                    EqualToConstraintDTO equalToConstraintDTO = (EqualToConstraintDTO) atomicConstraintDTO;
                    switch (field.getType())
                    {
                        case DATETIME:
                            return new EqualToConstraint(field, parseDate((String) equalToConstraintDTO.value));
                        case NUMERIC:
                            return new EqualToConstraint(field, NumberUtils.coerceToBigDecimal(equalToConstraintDTO.value));
                        default:
                            return new EqualToConstraint(field, equalToConstraintDTO.value);
                    }
                case IN_SET:
                    InSetConstraintDTO inSetConstraintDTO = (InSetConstraintDTO) atomicConstraintDTO;
                    return new IsInSetConstraint(field, inSetConstraintDTO.isFromFile()
                            ? fileReader.setFromFile(((InSetFromFileConstraintDTO)inSetConstraintDTO).file)
                            : DistributedList.uniform(((InSetOfValuesConstraintDTO)inSetConstraintDTO).values.stream().distinct()
                            .map(value ->
                            {
                                switch (field.getType())
                                {
                                    case DATETIME:
                                        return parseDate((String) value);
                                    case NUMERIC:
                                        return NumberUtils.coerceToBigDecimal(value);
                                    default:
                                        return value;
                                }
                            })
                            .collect(Collectors.toList())));
                case IN_MAP:
                    InMapConstraintDTO inMapConstraintDTO = (InMapConstraintDTO) atomicConstraintDTO;
                    return new InMapRelation(field, profileFields.getByName(inMapConstraintDTO.file), fileReader.listFromMapFile(inMapConstraintDTO.file, inMapConstraintDTO.key));
                case NULL:
                    return new IsNullConstraint(profileFields.getByName(atomicConstraintDTO.field));
                case MATCHES_REGEX:
                    return new MatchesRegexConstraint(profileFields.getByName(atomicConstraintDTO.field), pattern(((MatchesRegexConstraintDTO) atomicConstraintDTO).value));
                case CONTAINS_REGEX:
                    return new ContainsRegexConstraint(profileFields.getByName(atomicConstraintDTO.field), pattern(((ContainsRegexConstraintDTO) atomicConstraintDTO).value));
                case OF_LENGTH:
                    return new StringHasLengthConstraint(profileFields.getByName(atomicConstraintDTO.field), integer(((OfLengthConstraintDTO) atomicConstraintDTO).value));
                case SHORTER_THAN:
                    return new IsStringShorterThanConstraint(profileFields.getByName(atomicConstraintDTO.field), integer(((ShorterThanConstraintDTO) atomicConstraintDTO).value));
                case LONGER_THAN:
                    return new IsStringLongerThanConstraint(profileFields.getByName(atomicConstraintDTO.field), integer(((LongerThanConstraintDTO) atomicConstraintDTO).value));
                case GREATER_THAN:
                    return new IsGreaterThanConstantConstraint(profileFields.getByName(atomicConstraintDTO.field), NumberUtils.coerceToBigDecimal(((GreaterThanConstraintDTO) atomicConstraintDTO).value));
                case GREATER_THAN_OR_EQUAL_TO:
                    return new IsGreaterThanOrEqualToConstantConstraint(profileFields.getByName(atomicConstraintDTO.field), NumberUtils.coerceToBigDecimal(((GreaterThanOrEqualToConstraintDTO) atomicConstraintDTO).value));
                case LESS_THAN:
                    return new IsLessThanConstantConstraint(profileFields.getByName(atomicConstraintDTO.field), NumberUtils.coerceToBigDecimal(((LessThanConstraintDTO) atomicConstraintDTO).value));
                case LESS_THAN_OR_EQUAL_TO:
                    return new IsLessThanOrEqualToConstantConstraint(profileFields.getByName(atomicConstraintDTO.field), NumberUtils.coerceToBigDecimal(((LessThanOrEqualToConstraintDTO) atomicConstraintDTO).value));
                case AFTER:
                    return new IsAfterConstantDateTimeConstraint(profileFields.getByName(atomicConstraintDTO.field), parseDate(((AfterConstraintDTO) atomicConstraintDTO).value));
                case AFTER_OR_AT:
                    return new IsAfterOrEqualToConstantDateTimeConstraint(profileFields.getByName(atomicConstraintDTO.field), parseDate(((AfterOrAtConstraintDTO) atomicConstraintDTO).value));
                case BEFORE:
                    return new IsBeforeConstantDateTimeConstraint(profileFields.getByName(atomicConstraintDTO.field), parseDate(((BeforeConstraintDTO) atomicConstraintDTO).value));
                case BEFORE_OR_AT:
                    return new IsBeforeOrEqualToConstantDateTimeConstraint(profileFields.getByName(atomicConstraintDTO.field), parseDate(((BeforeOrAtConstraintDTO) atomicConstraintDTO).value));
                case GRANULAR_TO:
                    GranularToConstraintDTO granularToConstraintDTO = (GranularToConstraintDTO) atomicConstraintDTO;
                    return granularToConstraintDTO.value instanceof Number
                            ? new IsGranularToNumericConstraint(profileFields.getByName(atomicConstraintDTO.field), NumericGranularityFactory.create(granularToConstraintDTO.value))
                            : new IsGranularToDateConstraint(profileFields.getByName(atomicConstraintDTO.field), getDateTimeGranularity((String) granularToConstraintDTO.value));
                default:
                    throw new InvalidProfileException("Atomic constraint type not found: " + atomicConstraintDTO);
            }
        }
        if (dto instanceof GrammaticalConstraintDTO)
        {
            GrammaticalConstraintDTO grammaticalConstraintDTO = (GrammaticalConstraintDTO) dto;
            switch (grammaticalConstraintDTO.getType())
            {
                case ALL_OF:
                    return new AndConstraint(read(((AllOfConstraintDTO) grammaticalConstraintDTO).constraints, profileFields));
                case ANY_OF:
                    return new OrConstraint(read(((AnyOfConstraintDTO) grammaticalConstraintDTO).constraints, profileFields));
                case IF:
                    IfConstraintDTO conditionalConstraintDTO = (IfConstraintDTO) grammaticalConstraintDTO;
                    Constraint ifConstraint = read(conditionalConstraintDTO.ifConstraint, profileFields);
                    Constraint thenConstraint = read(conditionalConstraintDTO.thenConstraint, profileFields);
                    Constraint elseConstraint = conditionalConstraintDTO.elseConstraint == null ? null
                            : read(conditionalConstraintDTO.elseConstraint, profileFields);
                    return new ConditionalConstraint(ifConstraint, thenConstraint, elseConstraint);case NOT:
                return read(((NotConstraintDTO) grammaticalConstraintDTO).constraint, profileFields).negate();
                default:
                    throw new InvalidProfileException("Grammatical constraint type not found: " + grammaticalConstraintDTO);
            }
        }
        throw new InvalidProfileException("Constraint type not found: " + dto);
    }

    private DateTimeGranularity getDateTimeGranularity(String granularity) {
        String offsetUnitUpperCase = granularity.toUpperCase();
        boolean workingDay = offsetUnitUpperCase.equals("WORKING DAYS");
        return new DateTimeGranularity(Enum.valueOf(ChronoUnit.class, workingDay ? "DAYS" : offsetUnitUpperCase), workingDay);
    }

    private static OffsetDateTime parseDate(String value)
    {
        if (value.equalsIgnoreCase("NOW")) return NOW;

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ofPattern("u-MM-dd'T'HH:mm:ss'.'SSS"))
                .optionalStart()
                .appendOffset("+HH", "Z")
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);
        try
        {
            TemporalAccessor temporalAccessor = formatter.parse(value);
            return temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS)
                    ? OffsetDateTime.from(temporalAccessor)
                    : LocalDateTime.from(temporalAccessor).atOffset(ZoneOffset.UTC);
        } catch (DateTimeParseException exception)
        {
            throw new InvalidProfileException(String.format(
                    "Date string '%s' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS[Z] between (inclusive) " +
                            "0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z", value));
        }
    }

    private static int integer(Object value)
    {
        return NumberUtils.coerceToBigDecimal(value).intValueExact();
    }

    private static Pattern pattern(Object value)
    {
        return value instanceof Pattern ? (Pattern) value : Pattern.compile((String) value);
    }
}

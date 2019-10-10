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
import com.scottlogic.deg.profile.dtos.constraints.atomic.chronological.AfterConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.chronological.AfterOrAtConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.chronological.BeforeConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.chronological.BeforeOrAtConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.general.EqualToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.general.GranularToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.general.InMapConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.general.InSetConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.numerical.GreaterThanConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.numerical.GreaterThanOrEqualToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.numerical.LessThanConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.numerical.LessThanOrEqualToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.texual.*;
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
            AtomicConstraintDTO AtomicConstraintDTO = (AtomicConstraintDTO) dto;
            Field field = profileFields.getByName(AtomicConstraintDTO.field);
            if (AtomicConstraintDTO.getDependency() != null) return RelationsFactory.create(AtomicConstraintDTO, profileFields);
            switch (AtomicConstraintDTO.getType())
            {
                case EQUAL_TO:
                    EqualToConstraintDTO equalToConstraintDTO = (EqualToConstraintDTO) AtomicConstraintDTO;
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
                    InSetConstraintDTO inSetConstraintDTO = (InSetConstraintDTO) AtomicConstraintDTO;
                    return new IsInSetConstraint(field, inSetConstraintDTO.file != null
                            ? fileReader.setFromFile(inSetConstraintDTO.file)
                            : DistributedList.uniform(inSetConstraintDTO.values.stream().distinct()
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
                    InMapConstraintDTO inMapConstraintDTO = (InMapConstraintDTO) AtomicConstraintDTO;
                    return new InMapRelation(field, profileFields.getByName(inMapConstraintDTO.file), fileReader.listFromMapFile(inMapConstraintDTO.file, inMapConstraintDTO.key));
                case NULL:
                    return new IsNullConstraint(profileFields.getByName(AtomicConstraintDTO.field));
                case MATCHES_REGEX:
                    return new MatchesRegexConstraint(profileFields.getByName(AtomicConstraintDTO.field), pattern(((MatchesRegexConstraintDTO) AtomicConstraintDTO).value));
                case CONTAINS_REGEX:
                    return new ContainsRegexConstraint(profileFields.getByName(AtomicConstraintDTO.field), pattern(((ContainsRegexConstraintDTO) AtomicConstraintDTO).value));
                case OF_LENGTH:
                    return new StringHasLengthConstraint(profileFields.getByName(AtomicConstraintDTO.field), integer(((OfLengthConstraintDTO) AtomicConstraintDTO).value));
                case SHORTER_THAN:
                    return new IsStringShorterThanConstraint(profileFields.getByName(AtomicConstraintDTO.field), integer(((ShorterThanConstraintDTO) AtomicConstraintDTO).value));
                case LONGER_THAN:
                    return new IsStringLongerThanConstraint(profileFields.getByName(AtomicConstraintDTO.field), integer(((LongerThanConstraintDTO) AtomicConstraintDTO).value));
                case GREATER_THAN:
                    return new IsGreaterThanConstantConstraint(profileFields.getByName(AtomicConstraintDTO.field), NumberUtils.coerceToBigDecimal(((GreaterThanConstraintDTO) AtomicConstraintDTO).value));
                case GREATER_THAN_OR_EQUAL_TO:
                    return new IsGreaterThanOrEqualToConstantConstraint(profileFields.getByName(AtomicConstraintDTO.field), NumberUtils.coerceToBigDecimal(((GreaterThanOrEqualToConstraintDTO) AtomicConstraintDTO).value));
                case LESS_THAN:
                    return new IsLessThanConstantConstraint(profileFields.getByName(AtomicConstraintDTO.field), NumberUtils.coerceToBigDecimal(((LessThanConstraintDTO) AtomicConstraintDTO).value));
                case LESS_THAN_OR_EQUAL_TO:
                    return new IsLessThanOrEqualToConstantConstraint(profileFields.getByName(AtomicConstraintDTO.field), NumberUtils.coerceToBigDecimal(((LessThanOrEqualToConstraintDTO) AtomicConstraintDTO).value));
                case AFTER:
                    return new IsAfterConstantDateTimeConstraint(profileFields.getByName(AtomicConstraintDTO.field), parseDate(((AfterConstraintDTO) AtomicConstraintDTO).value));
                case AFTER_OR_AT:
                    return new IsAfterOrEqualToConstantDateTimeConstraint(profileFields.getByName(AtomicConstraintDTO.field), parseDate(((AfterOrAtConstraintDTO) AtomicConstraintDTO).value));
                case BEFORE:
                    return new IsBeforeConstantDateTimeConstraint(profileFields.getByName(AtomicConstraintDTO.field), parseDate(((BeforeConstraintDTO) AtomicConstraintDTO).value));
                case BEFORE_OR_AT:
                    return new IsBeforeOrEqualToConstantDateTimeConstraint(profileFields.getByName(AtomicConstraintDTO.field), parseDate(((BeforeOrAtConstraintDTO) AtomicConstraintDTO).value));
                case GRANULAR_TO:
                    GranularToConstraintDTO granularToConstraintDTO = (GranularToConstraintDTO) AtomicConstraintDTO;
                    return granularToConstraintDTO.value instanceof Number
                            ? new IsGranularToNumericConstraint(profileFields.getByName(AtomicConstraintDTO.field), NumericGranularityFactory.create(granularToConstraintDTO.value))
                            : new IsGranularToDateConstraint(profileFields.getByName(AtomicConstraintDTO.field), getDateTimeGranularity((String) granularToConstraintDTO.value));
                default:
                    throw new InvalidProfileException("Atomic constraint type not found: " + AtomicConstraintDTO);
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
        return new DateTimeGranularity(ChronoUnit.valueOf(ChronoUnit.class, workingDay ? "DAYS" : offsetUnitUpperCase), workingDay);
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

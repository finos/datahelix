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
import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraintdetail.DateTimeGranularity;
import com.scottlogic.deg.common.profile.constraintdetail.Granularity;
import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularityFactory;
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

    public Set<Constraint> read(Collection<ConstraintDTO> constraints, ProfileFields fields)
    {
        return constraints.stream()
                .map(subConstraintDto -> read(subConstraintDto, fields))
                .collect(Collectors.toSet());
    }

    public Constraint read(ConstraintDTO dto, ProfileFields profileFields)
    {
        if (dto == null) throw new InvalidProfileException("Constraint is null");
        if (dto instanceof RelationalConstraintDTO) return readRelationalConstraintDto((RelationalConstraintDTO)dto, profileFields);
        return dto instanceof AtomicConstraintDTO
                ? readAtomicConstraintDto((AtomicConstraintDTO) dto, profileFields)
                : readGrammaticalConstraintDto(dto, profileFields);
    }

    private Constraint readRelationalConstraintDto(RelationalConstraintDTO dto, ProfileFields fields)
    {
        Field main = fields.getByName(dto.field);
        Field other = fields.getByName(dto.getOtherField());
        switch (dto.getType())
        {
            case EQUAL_TO_FIELD:
                Granularity offsetGranularity = getOffsetUnit(main.getType(), dto.offsetUnit);
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

    private Constraint readAtomicConstraintDto(AtomicConstraintDTO dto, ProfileFields profileFields)
    {
        Field field = profileFields.getByName(dto.field);
        switch (dto.getType())
        {
            case EQUAL_TO:
                EqualToConstraintDTO equalToConstraintDTO = (EqualToConstraintDTO) dto;
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
                InSetConstraintDTO inSetConstraintDTO = (InSetConstraintDTO) dto;
                return new IsInSetConstraint(field, inSetConstraintDTO instanceof InSetFromFileConstraintDTO
                        ? fileReader.setFromFile(((InSetFromFileConstraintDTO) inSetConstraintDTO).file)
                        : DistributedList.uniform(((InSetOfValuesConstraintDTO) inSetConstraintDTO).values.stream().distinct()
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
                InMapConstraintDTO inMapConstraintDTO = (InMapConstraintDTO) dto;
                return new InMapRelation(field, profileFields.getByName(inMapConstraintDTO.file), fileReader.listFromMapFile(inMapConstraintDTO.file, inMapConstraintDTO.key));
            case MATCHES_REGEX:
                return new MatchesRegexConstraint(profileFields.getByName(dto.field), pattern(((MatchesRegexConstraintDTO) dto).value));
            case CONTAINS_REGEX:
                return new ContainsRegexConstraint(profileFields.getByName(dto.field), pattern(((ContainsRegexConstraintDTO) dto).value));
            case OF_LENGTH:
                return new StringHasLengthConstraint(profileFields.getByName(dto.field), integer(((OfLengthConstraintDTO) dto).value));
            case SHORTER_THAN:
                return new IsStringShorterThanConstraint(profileFields.getByName(dto.field), integer(((ShorterThanConstraintDTO) dto).value));
            case LONGER_THAN:
                return new IsStringLongerThanConstraint(profileFields.getByName(dto.field), integer(((LongerThanConstraintDTO) dto).value));
            case GREATER_THAN:
                return new IsGreaterThanConstantConstraint(profileFields.getByName(dto.field), NumberUtils.coerceToBigDecimal(((GreaterThanConstraintDTO) dto).value));
            case GREATER_THAN_OR_EQUAL_TO:
                return new IsGreaterThanOrEqualToConstantConstraint(profileFields.getByName(dto.field), NumberUtils.coerceToBigDecimal(((GreaterThanOrEqualToConstraintDTO) dto).value));
            case LESS_THAN:
                return new IsLessThanConstantConstraint(profileFields.getByName(dto.field), NumberUtils.coerceToBigDecimal(((LessThanConstraintDTO) dto).value));
            case LESS_THAN_OR_EQUAL_TO:
                return new IsLessThanOrEqualToConstantConstraint(profileFields.getByName(dto.field), NumberUtils.coerceToBigDecimal(((LessThanOrEqualToConstraintDTO) dto).value));
            case AFTER:
                return new IsAfterConstantDateTimeConstraint(profileFields.getByName(dto.field), parseDate(((AfterConstraintDTO) dto).value));
            case AFTER_OR_AT:
                return new IsAfterOrEqualToConstantDateTimeConstraint(profileFields.getByName(dto.field), parseDate(((AfterOrAtConstraintDTO) dto).value));
            case BEFORE:
                return new IsBeforeConstantDateTimeConstraint(profileFields.getByName(dto.field), parseDate(((BeforeConstraintDTO) dto).value));
            case BEFORE_OR_AT:
                return new IsBeforeOrEqualToConstantDateTimeConstraint(profileFields.getByName(dto.field), parseDate(((BeforeOrAtConstraintDTO) dto).value));
            case GRANULAR_TO:
                GranularToConstraintDTO granularToConstraintDTO = (GranularToConstraintDTO) dto;
                return granularToConstraintDTO.value instanceof Number
                        ? new IsGranularToNumericConstraint(profileFields.getByName(dto.field), NumericGranularityFactory.create(granularToConstraintDTO.value))
                        : new IsGranularToDateConstraint(profileFields.getByName(dto.field), getDateTimeGranularity((String) granularToConstraintDTO.value));
            default:
                throw new InvalidProfileException("Atomic constraint type not found: " + dto);
        }
    }

    private Constraint readGrammaticalConstraintDto(ConstraintDTO dto, ProfileFields profileFields)
    {
        switch (dto.getType())
        {
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
                return new IsNullConstraint(profileFields.getByName(((NullConstraintDTO)dto).field));
            default:
                throw new InvalidProfileException("Grammatical constraint type not found: " + dto);
        }
    }

    private int integer(Object value)
    {
        return NumberUtils.coerceToBigDecimal(value).intValueExact();
    }

    private Pattern pattern(Object value)
    {
        return value instanceof Pattern ? (Pattern) value : Pattern.compile((String) value);
    }

    private OffsetDateTime parseDate(String value) {
        if (value.toUpperCase().equals("NOW")) return NOW;
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
        }
        catch (DateTimeParseException dtpe)
        {
            throw new InvalidProfileException(String.format(
                    "Date string '%s' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS[Z] between (inclusive) " +
                            "0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z",
                    value
            ));
        }
    }

    private Granularity getOffsetUnit(FieldType type, String offsetUnit) {
        if (offsetUnit == null) return null;
        switch (type) {
            case NUMERIC:
                return NumericGranularityFactory.create(offsetUnit);
            case DATETIME:
                return getDateTimeGranularity(offsetUnit);
            default:
                return null;
        }
    }

    private DateTimeGranularity getDateTimeGranularity(String granularity)
    {
        String offsetUnitUpperCase = granularity.toUpperCase();
        boolean workingDay = offsetUnitUpperCase.equals("WORKING DAYS");
        return new DateTimeGranularity(Enum.valueOf(ChronoUnit.class, workingDay ? "DAYS" : offsetUnitUpperCase), workingDay);
    }
}

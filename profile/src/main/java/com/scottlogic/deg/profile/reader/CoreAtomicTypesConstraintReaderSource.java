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
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedDateGranularity;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.generator.fieldspecs.whitelist.ElementFrequency;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyWhitelist;
import com.scottlogic.deg.generator.fieldspecs.whitelist.Whitelist;
import com.scottlogic.deg.profile.reader.file.CsvInputStreamReader;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;

import java.io.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoreAtomicTypesConstraintReaderSource implements ConstraintReaderMapEntrySource {

    private final String fromFilePath;

    @Inject
    public CoreAtomicTypesConstraintReaderSource(final String fromFilePath) {
        this.fromFilePath = fromFilePath;
    }

    public Stream<ConstraintReaderMapEntry> getConstraintReaderMapEntries() {
        BigDecimal maxStringLength = BigDecimal.valueOf(Defaults.MAX_STRING_LENGTH);

        // This function handles the core types, apart from "integer" which is treated as a numeric
        // type with integral granularity.
        ConstraintReader standardOfTypeReader = (dto, fields, rules) ->
        {
            String typeString = ConstraintReaderHelpers.getValidatedValue(dto, String.class);
            final IsOfTypeConstraint.Types type;
            switch (typeString) {
                case "decimal":
                    type = IsOfTypeConstraint.Types.NUMERIC;
                    break;

                case "string":
                    type = IsOfTypeConstraint.Types.STRING;
                    break;

                case "datetime":
                    type = IsOfTypeConstraint.Types.DATETIME;
                    break;

                case "numeric":
                    throw new InvalidProfileException("Numeric type is no longer supported. " +
                        "Please use one of \"decimal\" or \"integer\"");

                default:
                    throw new InvalidProfileException("Unrecognised type in type constraint: " + dto.value);
            }

            return new IsOfTypeConstraint(fields.getByName(dto.field), type);
        };

        return Stream.of(
            new ConstraintReaderMapEntry(
                AtomicConstraintType.FORMATTED_AS.getText(),
                ".*",
                (dto, fields, rules) ->
                    new FormatConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, String.class)
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_EQUAL_TO_CONSTANT.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsInSetConstraint(
                        fields.getByName(dto.field),
                        new FrequencyWhitelist<>(
                        Collections.singleton(
                            new ElementFrequency<Object>(ConstraintReaderHelpers.getValidatedValue(dto), 1.0F)
                        )
                        )
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_IN_SET.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsInSetConstraint(
                        fields.getByName(dto.field),
                        new FrequencyWhitelist<>(
                        ConstraintReaderHelpers.getValidatedValues(dto)
                        )
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.CONTAINS_REGEX.getText(),
                ".*",
                (dto, fields, rules) ->
                    new ContainsRegexConstraint(
                        fields.getByName(dto.field),
                        Pattern.compile(ConstraintReaderHelpers.getValidatedValue(dto, String.class))
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.MATCHES_REGEX.getText(),
                ".*",
                (dto, fields, rules) ->
                    new MatchesRegexConstraint(
                        fields.getByName(dto.field),
                        Pattern.compile(ConstraintReaderHelpers.getValidatedValue(dto, String.class))
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_GREATER_THAN_CONSTANT.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsGreaterThanConstantConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, Number.class)
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsGreaterThanOrEqualToConstantConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, Number.class)
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_LESS_THAN_CONSTANT.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsLessThanConstantConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, Number.class)
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_LESS_THAN_OR_EQUAL_TO_CONSTANT.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsLessThanOrEqualToConstantConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, Number.class)
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_BEFORE_CONSTANT_DATE_TIME.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsBeforeConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, OffsetDateTime.class)
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_BEFORE_OR_EQUAL_TO_CONSTANT_DATE_TIME.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsBeforeOrEqualToConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, OffsetDateTime.class)
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_AFTER_CONSTANT_DATE_TIME.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsAfterConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, OffsetDateTime.class)
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_AFTER_OR_EQUAL_TO_CONSTANT_DATE_TIME.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsAfterOrEqualToConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, OffsetDateTime.class)
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_GRANULAR_TO.getText(),
                ".*",
                (dto, fields, rules) ->
                {
                    Optional<Number> numberValidatedValue =
                        ConstraintReaderHelpers.tryGetValidatedValue(dto, Number.class);
                    Optional<String> stringValidatedValue =
                        ConstraintReaderHelpers.tryGetValidatedValue(dto, String.class);

                    if (numberValidatedValue.isPresent()) {
                        Optional<ParsedGranularity> parsedNumericGranularity =
                            ParsedGranularity.tryParse(numberValidatedValue.get());
                        if (parsedNumericGranularity.isPresent()) {
                            return new IsGranularToNumericConstraint(
                                fields.getByName(dto.field),
                                parsedNumericGranularity.get()
                            );
                        }
                    } else if (stringValidatedValue.isPresent()) {
                        Optional<ParsedDateGranularity> parsedDateGranularity =
                            ParsedDateGranularity.tryParse(stringValidatedValue.get());
                        if (parsedDateGranularity.isPresent()) {
                            return new IsGranularToDateConstraint(
                                fields.getByName(dto.field),
                                parsedDateGranularity.get()
                            );
                        }
                    }
                    throw new InvalidProfileException(String.format(
                        "Field [%s]: Couldn't recognise granularity value, it must be either a negative power of ten or one of the supported datetime units.",
                        dto.field
                    ));
                }
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_NULL.getText(),
                ".*",
                (dto, fields, rules) -> new IsNullConstraint(fields.getByName(dto.field))
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                "integer",
                (dto, fields, rules) -> new AndConstraint(
                    new IsOfTypeConstraint(
                        fields.getByName(dto.field),
                        IsOfTypeConstraint.Types.NUMERIC
                    ),
                    new IsGranularToNumericConstraint(
                        fields.getByName(dto.field),
                        new ParsedGranularity(BigDecimal.ONE)
                    )
                )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                "decimal",
                standardOfTypeReader
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                "string",
                standardOfTypeReader
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                "datetime",
                standardOfTypeReader
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                "numeric",
                standardOfTypeReader
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_STRING_LONGER_THAN.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsStringLongerThanConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.ensureValueBetween(
                            dto,
                            Integer.class,
                            BigDecimal.ZERO,
                            maxStringLength.subtract(BigDecimal.ONE)
                        )
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_STRING_SHORTER_THAN.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsStringShorterThanConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.ensureValueBetween(
                            dto,
                            Integer.class,
                            BigDecimal.ONE,
                            maxStringLength.add(BigDecimal.ONE)
                        )
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.HAS_LENGTH.getText(),
                ".*",
                (dto, fields, rules) ->
                    new StringHasLengthConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.ensureValueBetween(
                            dto,
                            Integer.class,
                            BigDecimal.ZERO,
                            maxStringLength
                        )
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_FROM_FILE.getText(),
                ".*",
                (dto, fields, rules) -> {
                    String value = ConstraintReaderHelpers.getValidatedValue(dto, String.class);

                    InputStream streamFromPath = createStreamFromPath(appendPath(value));
                    Whitelist<String> names = CsvInputStreamReader.retrieveLines(streamFromPath);
                    closeStream(streamFromPath);

                   Whitelist<Object> downcastedNames = new FrequencyWhitelist<>(
                        names.distributedSet().stream()
                        .map(holder -> new ElementFrequency<>((Object) holder.element(), holder.frequency()))
                        .collect(Collectors.toSet()));
                    Field field = fields.getByName(dto.field);

                    return new IsInSetConstraint(field, downcastedNames);
                }
            )
        );
    }

    private String appendPath(String path) {
        return fromFilePath != null ? fromFilePath + path : path;
    }

    private static InputStream createStreamFromPath(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    private static void closeStream(InputStream stream) {
        try {
            stream.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}

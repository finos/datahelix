package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedDateGranularity;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.profile.reader.file.CsvInputStreamReader;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CoreAtomicTypesConstraintReaderSource implements ConstraintReaderMapEntrySource {
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

            return new IsOfTypeConstraint(fields.getByName(dto.field), type, rules);
        };

        return Stream.of(
            new ConstraintReaderMapEntry(
                AtomicConstraintType.FORMATTED_AS.getText(),
                ".*",
                (dto, fields, rules) ->
                    new FormatConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, String.class),
                        rules
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_EQUAL_TO_CONSTANT.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsInSetConstraint(
                        fields.getByName(dto.field),
                        Collections.singleton(ConstraintReaderHelpers.getValidatedValue(dto)),
                        rules
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_IN_SET.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsInSetConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValues(dto),
                        rules
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.CONTAINS_REGEX.getText(),
                ".*",
                (dto, fields, rules) ->
                    new ContainsRegexConstraint(
                        fields.getByName(dto.field),
                        Pattern.compile(ConstraintReaderHelpers.getValidatedValue(dto, String.class)),
                        rules
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.MATCHES_REGEX.getText(),
                ".*",
                (dto, fields, rules) ->
                    new MatchesRegexConstraint(
                        fields.getByName(dto.field),
                        Pattern.compile(ConstraintReaderHelpers.getValidatedValue(dto, String.class)),
                        rules
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_GREATER_THAN_CONSTANT.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsGreaterThanConstantConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, Number.class),
                        rules
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsGreaterThanOrEqualToConstantConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, Number.class),
                        rules
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_LESS_THAN_CONSTANT.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsLessThanConstantConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, Number.class),
                        rules
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_LESS_THAN_OR_EQUAL_TO_CONSTANT.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsLessThanOrEqualToConstantConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, Number.class),
                        rules
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_BEFORE_CONSTANT_DATE_TIME.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsBeforeConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, OffsetDateTime.class),
                        rules
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_BEFORE_OR_EQUAL_TO_CONSTANT_DATE_TIME.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsBeforeOrEqualToConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, OffsetDateTime.class),
                        rules
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_AFTER_CONSTANT_DATE_TIME.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsAfterConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, OffsetDateTime.class),
                        rules
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_AFTER_OR_EQUAL_TO_CONSTANT_DATE_TIME.getText(),
                ".*",
                (dto, fields, rules) ->
                    new IsAfterOrEqualToConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        ConstraintReaderHelpers.getValidatedValue(dto, OffsetDateTime.class),
                        rules
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
                                parsedNumericGranularity.get(),
                                rules
                            );
                        }
                    } else if (stringValidatedValue.isPresent()) {
                        Optional<ParsedDateGranularity> parsedDateGranularity =
                            ParsedDateGranularity.tryParse(stringValidatedValue.get());
                        if (parsedDateGranularity.isPresent()) {
                            return new IsGranularToDateConstraint(
                                fields.getByName(dto.field),
                                parsedDateGranularity.get(),
                                rules
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
                (dto, fields, rules) -> new IsNullConstraint(fields.getByName(dto.field), rules)
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                "integer",
                (dto, fields, rules) -> new AndConstraint(
                    new IsOfTypeConstraint(
                        fields.getByName(dto.field),
                        IsOfTypeConstraint.Types.NUMERIC,
                        rules
                    ),
                    new IsGranularToNumericConstraint(
                        fields.getByName(dto.field),
                        new ParsedGranularity(BigDecimal.ONE),
                        rules
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
                        ),
                        rules
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
                        ),
                        rules
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
                        ),
                        rules
                    )
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_FROM_FILE.getText(),
                ".*",
                (dto, fields, rules) -> {
                    String value = ConstraintReaderHelpers.getValidatedValue(dto, String.class);

                    InputStream streamFromPath = createStreamFromPath(value);
                    Set<String> names = CsvInputStreamReader.retrieveLines(streamFromPath);

                    Set<Object> downcastedNames = new HashSet<>(names);
                    Field field = fields.getByName(dto.field);

                    return new IsInSetConstraint(field, downcastedNames, rules);
                }
            )
        );
    }

    private static InputStream createStreamFromPath(String path) {
        try {
            return new FileInputStream(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}

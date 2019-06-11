package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedDateGranularity;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.common.util.HeterogeneousTypeContainer;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.profile.reader.names.NameCSVPopulator;
import com.scottlogic.deg.profile.reader.names.NameHolder;
import com.scottlogic.deg.profile.reader.names.NameRetrievalService;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;
import com.scottlogic.deg.profile.v0_1.ConstraintDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BaseAtomicConstraintReaderLookup implements AtomicConstraintReaderLookup {
    private static final Map<String, ConstraintReader> typeCodeToSpecificReader;
    private static final HeterogeneousTypeContainer<CatalogService<?, ?>> catalogServices;

    static {
        catalogServices = new HeterogeneousTypeContainer<CatalogService<?, ?>>()
            .put(NameRetrievalService.class, new NameRetrievalService(new NameCSVPopulator()));

        typeCodeToSpecificReader = new HashMap<>();

        add(AtomicConstraintType.FORMATTED_AS.getText(),
            (dto, fields, rules) ->
                new FormatConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, String.class),
                    rules));

        add(AtomicConstraintType.IS_EQUAL_TO_CONSTANT.getText(),
            (dto, fields, rules) ->
                new IsInSetConstraint(
                    fields.getByName(dto.field),
                    Collections.singleton(getValidatedValue(dto)),
                    rules));

        add(AtomicConstraintType.IS_IN_SET.getText(),
            (dto, fields, rules) ->
                new IsInSetConstraint(
                    fields.getByName(dto.field),
                    getValidatedValues(dto),
                    rules));

        add(AtomicConstraintType.CONTAINS_REGEX.getText(),
            (dto, fields, rules) ->
                new ContainsRegexConstraint(
                    fields.getByName(dto.field),
                    Pattern.compile(getValidatedValue(dto, String.class)),
                    rules));

        add(AtomicConstraintType.MATCHES_REGEX.getText(),
            (dto, fields, rules) ->
                new MatchesRegexConstraint(
                    fields.getByName(dto.field),
                    Pattern.compile(getValidatedValue(dto, String.class)),
                    rules));

        add(AtomicConstraintType.A_VALID.getText(),
            (dto, fields, rules) ->
            {
                StandardConstraintTypes standardType =
                    StandardConstraintTypes.valueOf(getValidatedValue(dto, String.class));
                Field field = fields.getByName(dto.field);
                switch (standardType) {
                    case ISIN:
                    case SEDOL:
                    case CUSIP:
                    case RIC:
                        return new AndConstraint(
                            new MatchesStandardConstraint(field, standardType, rules),
                            new IsOfTypeConstraint(field, IsOfTypeConstraint.Types.STRING, rules)
                        );
                    default:
                    return new MatchesStandardConstraint(field, standardType, rules);
                }
            });

        add(AtomicConstraintType.IS_GREATER_THAN_CONSTANT.getText(),
            (dto, fields, rules) ->
                new IsGreaterThanConstantConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, Number.class),
                    rules)
        );

        add(AtomicConstraintType.IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT.getText(),
            (dto, fields, rules) ->
                new IsGreaterThanOrEqualToConstantConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, Number.class),
                    rules));

        add(AtomicConstraintType.IS_LESS_THAN_CONSTANT.getText(),
            (dto, fields, rules) ->
                new IsLessThanConstantConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, Number.class),
                    rules));

        add(AtomicConstraintType.IS_LESS_THAN_OR_EQUAL_TO_CONSTANT.getText(),
            (dto, fields, rules) ->
                new IsLessThanOrEqualToConstantConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, Number.class),
                    rules));

        add(AtomicConstraintType.IS_BEFORE_CONSTANT_DATE_TIME.getText(),
            (dto, fields, rules) ->
                new IsBeforeConstantDateTimeConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, OffsetDateTime.class),
                    rules));

        add(AtomicConstraintType.IS_BEFORE_OR_EQUAL_TO_CONSTANT_DATE_TIME.getText(),
            (dto, fields, rules) ->
                new IsBeforeOrEqualToConstantDateTimeConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, OffsetDateTime.class),
                    rules));

        add(AtomicConstraintType.IS_AFTER_CONSTANT_DATE_TIME.getText(),
            (dto, fields, rules) ->
                new IsAfterConstantDateTimeConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, OffsetDateTime.class),
                    rules));

        add(AtomicConstraintType.IS_AFTER_OR_EQUAL_TO_CONSTANT_DATE_TIME.getText(),
            (dto, fields, rules) ->
                new IsAfterOrEqualToConstantDateTimeConstraint(
                    fields.getByName(dto.field),
                    getValidatedValue(dto, OffsetDateTime.class),
                    rules));

        add(AtomicConstraintType.IS_GRANULAR_TO.getText(),
            (dto, fields, rules) ->
            {
                Optional <Number> numberValidatedValue = tryGetValidatedValue(dto, Number.class);
                Optional <String> stringValidatedValue = tryGetValidatedValue(dto, String.class);

                if(numberValidatedValue.isPresent()){
                    Optional<ParsedGranularity> parsedNumericGranularity = ParsedGranularity.tryParse(numberValidatedValue.get());
                    if(parsedNumericGranularity.isPresent()){
                        return new IsGranularToNumericConstraint(
                            fields.getByName(dto.field),
                            parsedNumericGranularity.get(),
                            rules);
                    }
                }
                else if(stringValidatedValue.isPresent()){
                    Optional<ParsedDateGranularity> parsedDateGranularity = ParsedDateGranularity.tryParse(stringValidatedValue.get());
                    if(parsedDateGranularity.isPresent()) {
                        return new IsGranularToDateConstraint(
                            fields.getByName(dto.field),
                            parsedDateGranularity.get(),
                            rules);
                    }
                }
                throw new InvalidProfileException(String.format("Field [%s]: Couldn't recognise granularity value, it must be either a negative power of ten or one of the supported datetime units.", dto.field));
            }
        );


        add(AtomicConstraintType.IS_NULL.getText(),
            (dto, fields, rules) ->
                new IsNullConstraint(fields.getByName(dto.field), rules));

        add(AtomicConstraintType.IS_OF_TYPE.getText(),
            (dto, fields, rules) ->
            {
                String typeString = getValidatedValue(dto, String.class);
                if (typeString.equals("integer")) {
                    return new AndConstraint(
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
                    );
                }
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

                return new IsOfTypeConstraint(
                    fields.getByName(dto.field),
                    type,
                    rules);
            });

        BigDecimal maxStringLength = BigDecimal.valueOf(Defaults.MAX_STRING_LENGTH);

        // String constraints
        add(AtomicConstraintType.IS_STRING_LONGER_THAN.getText(),
                (dto, fields, rules) ->
                    new IsStringLongerThanConstraint(
                        fields.getByName(dto.field),
                        ensureValueBetween(dto, Integer.class, BigDecimal.ZERO, maxStringLength.subtract(BigDecimal.ONE)),
                        rules));

        add(AtomicConstraintType.IS_STRING_SHORTER_THAN.getText(),
                (dto, fields, rules) ->
                    new IsStringShorterThanConstraint(
                        fields.getByName(dto.field),
                        ensureValueBetween(dto, Integer.class, BigDecimal.ONE, maxStringLength.add(BigDecimal.ONE)),
                        rules));

        add(AtomicConstraintType.HAS_LENGTH.getText(),
                (dto, fields, rules) ->
                    new StringHasLengthConstraint(
                        fields.getByName(dto.field),
                        ensureValueBetween(dto, Integer.class, BigDecimal.ZERO, maxStringLength),
                        rules));

        add(AtomicConstraintType.A_TYPICAL.getText(),
            (dto, fields, rules) -> {
                NameConstraintTypes type = NameConstraintTypes.lookupProfileText(getValidatedValue(dto, String.class));
                Field field = fields.getByName(dto.field);

                NameRetrievalService service = catalogServices.get(NameRetrievalService.class)
                    .orElseThrow(() -> new UnsupportedOperationException("No name retrieval service set!"));

                Set<Object> objects = service.retrieveValues(type)
                    .stream()
                    .map(NameHolder::getName)
                    .map(Object.class::cast)
                    .collect(Collectors.toSet());

                return new AndConstraint(
                    new IsInNameSetConstraint(field, objects, rules),
                    new IsOfTypeConstraint(field, IsOfTypeConstraint.Types.STRING, rules)
                );
            });
    }

    private static Object getValidatedValue(ConstraintDTO dto) {
        return getValidatedValue(dto, dto.value, Object.class);
    }

    private static <T> T getValidatedValue(ConstraintDTO dto, Class<T> requiredType) {
        return getValidatedValue(dto, dto.value, requiredType);
    }

    private static <T> Optional<T>  tryGetValidatedValue(ConstraintDTO dto, Class<T> requiredType) {
        try{
            return Optional.of(getValidatedValue(dto, dto.value, requiredType));
        }
        catch(Exception exp){
            return Optional.empty();
        }
    }

    /**
     * @param dto          The ConstraintDTO instance
     * @param requiredType the type of value required, pass Object.class if any type is acceptable
     * @return the value in the ConstraintDTO cast as T
     * @throws InvalidProfileException if the value is null, not of type T, or (when a number) outside of the allowed range
     */
    private static <T> T getValidatedValue(ConstraintDTO dto, Object value, Class<T> requiredType) {
        if (value == null) {
            throw new InvalidProfileException(
                String.format("Field [%s]: Couldn't recognise 'value' property, it must be set to a value", dto.field));
        }

        if (requiredType == OffsetDateTime.class || value instanceof Map) {
            //the only value that is currently permitted to be a Map is a DateObject
            value = getValueAsDate(dto, value);
        }

        if (requiredType == Integer.class && value instanceof BigDecimal) {
            BigDecimal valueAsBigDecimal = (BigDecimal) value;
            if (valueAsBigDecimal.stripTrailingZeros().scale() > 0) {
                throw new InvalidProfileException(
                    String.format(
                        "Field [%s]: Couldn't recognise 'value' property, it must be an integer but was a decimal with value `%s`",
                        dto.field,
                        value));
            }

            value = valueAsBigDecimal.intValueExact();
        }

        if (!requiredType.isInstance(value)) {
            throw new InvalidProfileException(
                String.format(
                    "Field [%s]: Couldn't recognise 'value' property, it must be a %s but was a %s with value `%s`",
                    dto.field,
                    requiredType.getSimpleName(),
                    value.getClass().getSimpleName(),
                    value));
        }

        if (value instanceof Number) {
            return requiredType.cast(validateNumber(dto, (Number) value));
        } else if (value instanceof String) {
            return requiredType.cast(validateString(dto, (String) value));
        }

        return requiredType.cast(value);
    }

    private static String validateString(ConstraintDTO dto, String value) {
        if (value.length() > Defaults.MAX_STRING_LENGTH) {
            throw new InvalidProfileException(String.format(
                "Field [%s]: set contains a string longer than maximum permitted length, was: %d, max-length: %d",
                dto.field,
                value.length(),
                Defaults.MAX_STRING_LENGTH));
        }

        return value;
    }

    private static Number validateNumber(ConstraintDTO dto, Number value) {
        return ensureValueBetween(
            dto,
            value,
            Defaults.NUMERIC_MIN,
            Defaults.NUMERIC_MAX);
    }

    private static <T> T ensureValueBetween(
        ConstraintDTO dto,
        @SuppressWarnings("SameParameterValue") Class<T> requiredType,
        BigDecimal min,
        BigDecimal max) {

        T value = getValidatedValue(dto, dto.value, requiredType);
        return ensureValueBetween(dto, value, min, max);
    }

    private static <T> T ensureValueBetween(ConstraintDTO dto, T value, BigDecimal min, BigDecimal max) {
        BigDecimal valueAsBigDecimal = NumberUtils.coerceToBigDecimal(value);
        if (valueAsBigDecimal.compareTo(min) < 0) {
            throw new InvalidProfileException(String.format(
                "Field [%s]: %s constraint must have an operand/value >= %s, currently is %s",
                dto.field,
                dto.is,
                min.toPlainString(),
                valueAsBigDecimal.toPlainString()));
        }

        if (valueAsBigDecimal.compareTo(max) > 0) {
            throw new InvalidProfileException(String.format(
                "Field [%s]: %s constraint must have an operand/value <= %s, currently is %s",
                dto.field,
                dto.is,
                max.toPlainString(),
                valueAsBigDecimal.toPlainString()));
        }

        return value;
    }

    private static Set<Object> getValidatedValues(ConstraintDTO dto) {
        Set<Object> mappedValues = new HashSet<>();

        if (dto.values == null) {
            throw new InvalidProfileException(String.format("Field [%s]: Couldn't recognise 'values' property, it must not contain 'null'", dto.field));
        }

        for (Object value : dto.values) {
            if (value == null) {
                throw new InvalidProfileException(String.format("Field [%s]: Set must not contain null", dto.field));
            }

            mappedValues.add(getValidatedValue(dto, value, Object.class));
        }

        return mappedValues;
    }

    private static void add(String typeCode, ConstraintReader func) {
        typeCodeToSpecificReader.put(typeCode, func);
    }

    private static OffsetDateTime getValueAsDate(ConstraintDTO dto, Object value) {
        if (!(value instanceof Map)) {
            throw new InvalidProfileException(String.format("Field [%s]: Dates should be expressed in object format e.g. { \"date\": \"%s\" }", dto.field, value));
        }

        Map objectMap = (Map) value;
        if (!objectMap.containsKey("date"))
            throw new InvalidProfileException(String.format("Field [%s]: Object found but no 'date' property exists, found %s", dto.field, Objects.toString(objectMap.keySet())));

        Object date = objectMap.get("date");
        if (!(date instanceof String))
            throw new InvalidProfileException(String.format("Field [%s]: Date on date object must be a string, found %s", dto.field, date));

        OffsetDateTime offsetDateTime = parseDate((String) date, dto);
        if (offsetDateTime != null && (offsetDateTime.getYear() > 9999 || offsetDateTime.getYear() < 1)) {
            throwDateTimeError((String) date, dto);
        }

        return offsetDateTime;
    }

    private static OffsetDateTime parseDate(String value, ConstraintDTO dto) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("u-MM-dd'T'HH:mm:ss'.'SSS"))
            .optionalStart()
            .appendOffset("+HH", "Z")
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);

        try {
            TemporalAccessor temporalAccessor = formatter.parse(value);

            return temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS)
                ? OffsetDateTime.from(temporalAccessor)
                : LocalDateTime.from(temporalAccessor).atOffset(ZoneOffset.UTC);
        } catch (DateTimeParseException dtpe) {
            throwDateTimeError(value, dto);
            return null;
        }
    }

    private static void throwDateTimeError(String profileDate, ConstraintDTO dto) {
        throw new InvalidProfileException(String.format(
            "Field [%s]: Date string '%s' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS[Z] between (inclusive) " +
                "0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z",
            dto.field,
            profileDate));
    }

    @Override
    public ConstraintReader getByTypeCode(String typeCode) {
        return typeCodeToSpecificReader.get(typeCode);
    }
}

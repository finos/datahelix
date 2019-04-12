package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.restrictions.ParsedGranularity;
import com.scottlogic.deg.generator.utils.NumberUtils;
import com.scottlogic.deg.schemas.v0_1.AtomicConstraintType;
import com.scottlogic.deg.schemas.v0_1.ConstraintDTO;

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

class AtomicConstraintReaderLookup {
    private static final Map<String, ConstraintReader> typeCodeToSpecificReader;

    static {
        typeCodeToSpecificReader = new HashMap<>();

        add(AtomicConstraintType.FORMATTEDAS.toString(),
                (dto, fields, rules) ->
                    new FormatConstraint(
                        fields.getByName(dto.field),
                        throwIfValueInvalid(dto, String.class),
                        rules));

        add(AtomicConstraintType.ISEQUALTOCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsInSetConstraint(
                        fields.getByName(dto.field),
                        Collections.singleton(unwrapDateValueIfDateObject(
                            throwIfValueInvalid(dto, Object.class),
                            dto.field)),
                        rules));

        add(AtomicConstraintType.ISINSET.toString(),
                (dto, fields, rules) ->
                    new IsInSetConstraint(
                        fields.getByName(dto.field),
                        mapValues(dto),
                        rules));

        add(AtomicConstraintType.CONTAINSREGEX.toString(),
            (dto, fields, rules) ->
                new ContainsRegexConstraint(
                    fields.getByName(dto.field),
                    Pattern.compile(throwIfValueInvalid(dto, String.class)),
                    rules));

        add(AtomicConstraintType.MATCHESREGEX.toString(),
                (dto, fields, rules) ->
                    new MatchesRegexConstraint(
                        fields.getByName(dto.field),
                        Pattern.compile(throwIfValueInvalid(dto, String.class)),
                        rules));

        add(AtomicConstraintType.AVALID.toString(),
                (dto, fields, rules) ->
                    new MatchesStandardConstraint(
                        fields.getByName(dto.field),
                        StandardConstraintTypes.valueOf(throwIfValueInvalid(dto, String.class)),
                        rules
                    ));

        add(AtomicConstraintType.ISGREATERTHANCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsGreaterThanConstantConstraint(
                        fields.getByName(dto.field),
                        throwIfValueInvalid(dto, Number.class),
                        rules)
        );

        add(AtomicConstraintType.ISGREATERTHANOREQUALTOCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsGreaterThanOrEqualToConstantConstraint(
                        fields.getByName(dto.field),
                        throwIfValueInvalid(dto, Number.class),
                        rules));

        add(AtomicConstraintType.ISLESSTHANCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsLessThanConstantConstraint(
                        fields.getByName(dto.field),
                        throwIfValueInvalid(dto, Number.class),
                        rules));

        add(AtomicConstraintType.ISLESSTHANOREQUALTOCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsLessThanOrEqualToConstantConstraint(
                        fields.getByName(dto.field),
                        throwIfValueInvalid(dto, Number.class),
                        rules));

        add(AtomicConstraintType.ISBEFORECONSTANTDATETIME.toString(),
                (dto, fields, rules) ->
                    new IsBeforeConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(dto),
                        rules));

        add(AtomicConstraintType.ISBEFOREOREQUALTOCONSTANTDATETIME.toString(),
                (dto, fields, rules) ->
                    new IsBeforeOrEqualToConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(dto),
                        rules));

        add(AtomicConstraintType.ISAFTERCONSTANTDATETIME.toString(),
                (dto, fields, rules) ->
                    new IsAfterConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(dto),
                        rules));

        add(AtomicConstraintType.ISAFTEROREQUALTOCONSTANTDATETIME.toString(),
                (dto, fields, rules) ->
                    new IsAfterOrEqualToConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(dto),
                        rules));

        add(AtomicConstraintType.ISGRANULARTO.toString(),
                (dto, fields, rules) ->
                    new IsGranularToConstraint(
                        fields.getByName(dto.field),
                        ParsedGranularity.parse(throwIfValueInvalid(dto, Number.class)),
                        rules));

        add(AtomicConstraintType.ISNULL.toString(),
                (dto, fields, rules) ->
                    new IsNullConstraint(fields.getByName(dto.field), rules));

        add(AtomicConstraintType.ISOFTYPE.toString(),
                (dto, fields, rules) ->
                {
                    String typeString = throwIfValueInvalid(dto, String.class);
                    if (typeString.equals("integer")) {
                        return new AndConstraint(
                            new IsOfTypeConstraint(
                                fields.getByName(dto.field),
                                IsOfTypeConstraint.Types.NUMERIC,
                                rules
                            ),
                            new IsGranularToConstraint(
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

        // String constraints
        add(AtomicConstraintType.ISSTRINGLONGERTHAN.toString(),
                (dto, fields, rules) -> {

                    int length = getIntegerLength(dto, 0, false);
                    return new IsStringLongerThanConstraint(
                        fields.getByName(dto.field),
                        length,
                        rules);
                });

        add(AtomicConstraintType.ISSTRINGSHORTERTHAN.toString(),
                (dto, fields, rules) -> {

                    int length = getIntegerLength(dto, 1, true);
                    return new IsStringShorterThanConstraint(
                        fields.getByName(dto.field),
                        length,
                        rules);
                });

        add(AtomicConstraintType.HASLENGTH.toString(),
                (dto, fields, rules) -> {

                    int length = getIntegerLength(dto, 0, true);
                    return new StringHasLengthConstraint(
                        fields.getByName(dto.field),
                        length,
                        rules);
                });
    }

    /**
     * @param dto The ConstraintDTO instance
     * @param requiredType the type of value required, pass Object.class if any type is acceptable
     * @param <T>
     * @return the value in the ConstraintDTO cast as T
     * @throws InvalidProfileException if the value is null, not of type T, or (when a number) outside of the allowed range
     */
    private static <T> T throwIfValueInvalid(ConstraintDTO dto, Class<T> requiredType) throws InvalidProfileException {
        Object value = dto.value;

        if (value == null) {
            throw new InvalidProfileException(
                String.format("Field [%s]: Couldn't recognise 'value' property, it must be set to a value", dto.field));
        }

        if (!requiredType.isInstance(value)){
            throw new InvalidProfileException(
                String.format(
                    "Field [%s]: Couldn't recognise 'value' property, it must be a %s but was a %s with value `%s`",
                    dto.field,
                    requiredType.getSimpleName(),
                    value.getClass().getSimpleName(),
                    value));
        }

        if(value instanceof Number) {
            BigDecimal valueAsBigDecimal = NumberUtils.coerceToBigDecimal(value);
            if (GenerationConfig.Constants.NUMERIC_MAX.compareTo(valueAsBigDecimal) < 0) {
                throw new InvalidProfileException(String.format("Field [%s]: 'value' property is out of upper bound", dto.field));
            }
            if (GenerationConfig.Constants.NUMERIC_MIN.compareTo(valueAsBigDecimal) > 0) {
                throw new InvalidProfileException(String.format("Field [%s]: 'value' property is out of lower bound", dto.field));
            }
        }

        return requiredType.cast(value);
    }

    private static int getIntegerLength(ConstraintDTO dto, int minimumInclusive, boolean maxInclusive) throws InvalidProfileException {
        Number value = throwIfValueInvalid(dto, Number.class);
        BigDecimal valueAsBigDecimal = NumberUtils
            .coerceToBigDecimal(value)
            .stripTrailingZeros();

        if (valueAsBigDecimal.scale() > 0){
            throw new InvalidProfileException(
                String.format(
                    "Field [%s]: String-length operator must contain a integer value for its operand found (%s <%s>)",
                    dto.field,
                    dto.value,
                    dto.value.getClass().getSimpleName()));
        }

        BigDecimal max = BigDecimal.valueOf(Integer.MAX_VALUE);
        if (!maxInclusive){
            max = max.subtract(BigDecimal.ONE);
        }

        if (valueAsBigDecimal.compareTo(max) > 0){
            //length is greater than the largest possible int
            throw new InvalidProfileException(
                String.format(
                    "Field [%s]: %s constraint must have an operand/value <= %s, currently is %s",
                    dto.field,
                    dto.is,
                    max.toPlainString(),
                    valueAsBigDecimal.toPlainString()));
        }

        int length = valueAsBigDecimal.intValue();

        if (length >= minimumInclusive) {
            return length;
        }

        throw new InvalidProfileException(
            String.format(
                "Field [%s]: %s constraint must have an operand/value >= %d, currently is %d",
                dto.field,
                dto.is,
                minimumInclusive,
                length));
    }

    private static Set<Object> mapValues(ConstraintDTO dto) throws InvalidProfileException {
        HashSet<Object> mappedValues = new HashSet<>();

        if (dto.values == null) {
            throw new InvalidProfileException(String.format("Field [%s]: Couldn't recognise 'values' property, it must not contain 'null'", dto.field));
        }

        for (Object value: dto.values){
            mappedValues.add(unwrapDateValueIfDateObject(value, dto.field));
        }

        return mappedValues;
    }

    private static void add(String typeCode, ConstraintReader func) {
        typeCodeToSpecificReader.put(typeCode, func);
    }

    private static Object unwrapDateValueIfDateObject(Object value, String field) throws InvalidProfileException {
        if (!(value instanceof Map))
            return value;

        Map objectMap = (Map) value;
        if (!objectMap.containsKey("date"))
            throw new InvalidProfileException(String.format("Field [%s]: Object found but no 'date' property exists, found %s", field, Objects.toString(objectMap.keySet())));

        Object date = objectMap.get("date");
        if (!(date instanceof String))
            throw new InvalidProfileException(String.format("Field [%s]: Date on date object must be a string, found %s", field, date));

        return parseDate((String)date, field);
    }

    private static OffsetDateTime unwrapDate(ConstraintDTO dto) throws InvalidProfileException {
        Object date = unwrapDateValueIfDateObject(throwIfValueInvalid(dto, Object.class), dto.field);
        if (date instanceof OffsetDateTime) {
            return (OffsetDateTime) date;
        }

        throw new InvalidProfileException(String.format("Field [%s]: Dates should be expressed in object format e.g. { \"date\": \"%s\" }", dto.field, dto.value));
    }

    private static OffsetDateTime parseDate(String value, String field) throws InvalidProfileException {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("u-MM-dd'T'HH:mm:ss'.'SSS"))
            .optionalStart()
            .appendOffset("+HH", "Z")
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);

        try {
            TemporalAccessor temporalAccessor = formatter.parse(value);

            OffsetDateTime parsedDateTime =
                temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS)
                    ? OffsetDateTime.from(temporalAccessor)
                    : LocalDateTime.from(temporalAccessor).atOffset(ZoneOffset.UTC);

            if (parsedDateTime.getYear() > 9999 || parsedDateTime.getYear() < 1)
                throwDateTimeError(value, field);

            return parsedDateTime;
        } catch (DateTimeParseException dtpe) {
            throwDateTimeError(value, field);
            return null;
        }
    }

    private static void throwDateTimeError(String profileDate, String field) throws InvalidProfileException {
        throw new InvalidProfileException(String.format(
            "Field [%s]: Date string '%s' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS[Z] between (inclusive) " +
                "0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z",
            field,
            profileDate));
    }

    ConstraintReader getByTypeCode(String typeCode) {
        return typeCodeToSpecificReader.get(typeCode);
    }
}

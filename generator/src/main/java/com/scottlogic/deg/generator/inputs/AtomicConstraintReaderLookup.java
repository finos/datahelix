package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.restrictions.ParsedGranularity;
import com.scottlogic.deg.generator.utils.NumberUtils;
import com.scottlogic.deg.schemas.v0_1.AtomicConstraintType;
import com.scottlogic.deg.schemas.v0_1.ConstraintDTO;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
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
                        (String) throwIfValueInvalid(dto.value),
                        rules));

        add(AtomicConstraintType.ISEQUALTOCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsInSetConstraint(
                        fields.getByName(dto.field),
                        mapValues(Collections.singleton(throwIfValueInvalid(dto.value))),
                        rules));

        add(AtomicConstraintType.ISINSET.toString(),
                (dto, fields, rules) ->
                    new IsInSetConstraint(
                        fields.getByName(dto.field),
                        mapValues(throwIfValuesNull(dto.values)),
                        rules));

        //noinspection RedundantCast
        add(AtomicConstraintType.CONTAINSREGEX.toString(),
            (dto, fields, rules) ->
                new ContainsRegexConstraint(
                    fields.getByName(dto.field),
                    Pattern.compile((String) throwIfValueInvalid(dto.value)),
                    rules));

        //noinspection RedundantCast
        add(AtomicConstraintType.MATCHESREGEX.toString(),
                (dto, fields, rules) ->
                    new MatchesRegexConstraint(
                        fields.getByName(dto.field),
                        Pattern.compile((String) throwIfValueInvalid(dto.value)),
                        rules));

        //noinspection RedundantCast
        add(AtomicConstraintType.AVALID.toString(),
                (dto, fields, rules) ->
                    new MatchesStandardConstraint(
                        fields.getByName(dto.field),
                        StandardConstraintTypes.valueOf((String) throwIfValueInvalid(dto.value)),
                        rules
                    ));

        add(AtomicConstraintType.ISGREATERTHANCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsGreaterThanConstantConstraint(
                        fields.getByName(dto.field),
                        (Number) throwIfValueInvalid(dto.value),
                        rules)
        );

        add(AtomicConstraintType.ISGREATERTHANOREQUALTOCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsGreaterThanOrEqualToConstantConstraint(
                        fields.getByName(dto.field),
                        (Number) throwIfValueInvalid(dto.value),
                        rules));

        add(AtomicConstraintType.ISLESSTHANCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsLessThanConstantConstraint(
                        fields.getByName(dto.field),
                        (Number) throwIfValueInvalid(dto.value),
                        rules));

        add(AtomicConstraintType.ISLESSTHANOREQUALTOCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsLessThanOrEqualToConstantConstraint(
                        fields.getByName(dto.field),
                        (Number) throwIfValueInvalid(dto.value),
                        rules));

        add(AtomicConstraintType.ISBEFORECONSTANTDATETIME.toString(),
                (dto, fields, rules) ->
                    new IsBeforeConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(throwIfValueInvalid(dto.value)),
                        rules));

        add(AtomicConstraintType.ISBEFOREOREQUALTOCONSTANTDATETIME.toString(),
                (dto, fields, rules) ->
                    new IsBeforeOrEqualToConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(throwIfValueInvalid(dto.value)),
                        rules));

        add(AtomicConstraintType.ISAFTERCONSTANTDATETIME.toString(),
                (dto, fields, rules) ->
                    new IsAfterConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(throwIfValueInvalid(dto.value)),
                        rules));

        add(AtomicConstraintType.ISAFTEROREQUALTOCONSTANTDATETIME.toString(),
                (dto, fields, rules) ->
                    new IsAfterOrEqualToConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(throwIfValueInvalid(dto.value)),
                        rules));

        add(AtomicConstraintType.ISGRANULARTO.toString(),
                (dto, fields, rules) ->
                    new IsGranularToConstraint(
                        fields.getByName(dto.field),
                        ParsedGranularity.parse(throwIfValueInvalid(dto.value)),
                        rules));

        add(AtomicConstraintType.ISNULL.toString(),
                (dto, fields, rules) ->
                    new IsNullConstraint(fields.getByName(dto.field), rules));

        add(AtomicConstraintType.ISOFTYPE.toString(),
                (dto, fields, rules) ->
                {
                    String typeString = (String) throwIfValueInvalid(dto.value);
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

                    int length = getIntegerLength(dto);
                    return new IsStringLongerThanConstraint(
                        fields.getByName(dto.field),
                        length,
                        rules);
                });

        add(AtomicConstraintType.ISSTRINGSHORTERTHAN.toString(),
                (dto, fields, rules) -> {

                    int length = getIntegerLength(dto);
                    return new IsStringShorterThanConstraint(
                        fields.getByName(dto.field),
                        length,
                        rules);
                });

        add(AtomicConstraintType.HASLENGTH.toString(),
                (dto, fields, rules) -> {

                    int length = getIntegerLength(dto);
                    return new StringHasLengthConstraint(
                        fields.getByName(dto.field),
                        length,
                        rules);
                });
    }

    private static <T> T throwIfValueInvalid(T value) throws InvalidProfileException {
        if (value == null) {
            throw new InvalidProfileException("Couldn't recognise 'value' property, it must be set to a value");
        }
        if(value instanceof Number) {
            BigDecimal valueAsBigDecimal = NumberUtils.coerceToBigDecimal(value);
            if (GenerationConfig.Constants.NUMERIC_MAX.compareTo(valueAsBigDecimal) < 0) {
                throw new InvalidProfileException("'value' property is out of upper bound");
            }
            if (GenerationConfig.Constants.NUMERIC_MIN.compareTo(valueAsBigDecimal) > 0) {
                throw new InvalidProfileException("'value' property is out of lower bound");
            }
        }
        return value;
    }

    private static <T> Collection<T> throwIfValuesNull(Collection<T> values) throws InvalidProfileException {
        if (values == null) {
            throw new InvalidProfileException("Couldn't recognise 'values' property, it must not contain 'null'");
        }
        return values;
    }

    private static int getIntegerLength(ConstraintDTO dto) throws InvalidProfileException {
        Object value = throwIfValueInvalid(dto.value);
        BigDecimal decimal;

        if (value instanceof Integer){
            value = BigDecimal.valueOf((Integer)value);
        }

        //the profile reader (jackson) has been told to deserialise all numbers as BigDecimal's
        if (value instanceof BigDecimal){
            decimal = (BigDecimal)value;
        } else {
            throw new InvalidProfileException(
                String.format(
                    "String-length operator must contain a numeric value for its operand found (%s) for field [%s]",
                    dto.value == null ? "<null>" : dto.value.toString(),
                    dto.field));
        }

        decimal = decimal.stripTrailingZeros();

        if (decimal.scale() > 0){
            throw new InvalidProfileException(
                String.format(
                    "String-length operator must contain a integer value for its operand found (%s <%s>) for field [%s]",
                    dto.value,
                    dto.value.getClass().getSimpleName(),
                    dto.field));
        }

        return decimal.intValue();
    }

    private static Set<Object> mapValues(Collection<Object> values) throws InvalidProfileException {
        HashSet<Object> mappedValues = new HashSet<>();

        for (Object value: values){
            mappedValues.add(AtomicConstraintReaderLookup.potentialUnwrapDate(value));
        }

        return mappedValues;
    }

    private static void add(String typeCode, ConstraintReader func) {
        typeCodeToSpecificReader.put(typeCode, func);
    }

    private static Object potentialUnwrapDate(Object value) throws InvalidProfileException {
        if (!(value instanceof Map))
            return value;

        Map objectMap = (Map) value;
        if (!objectMap.containsKey("date"))
            throw new InvalidProfileException(String.format("Object found but no 'date' property exists, found %s", Objects.toString(objectMap.keySet())));

        Object date = objectMap.get("date");
        if (!(date instanceof String))
            throw new InvalidProfileException(String.format("Date on date object must be a string, found %s", date));

        return parseDate((String)date);
    }

    private static OffsetDateTime unwrapDate(Object value) throws InvalidProfileException {
        Object date = potentialUnwrapDate(value);
        if (date instanceof OffsetDateTime)
            return (OffsetDateTime) date;

        throw new InvalidProfileException(String.format("Dates should be expressed in object format e.g. { \"date\": \"%s\" }", value));
    }

    private static OffsetDateTime parseDate(String value) throws InvalidProfileException {
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
                throwDateTimeError(value);

            return parsedDateTime;
        } catch (DateTimeParseException dtpe) {
            throwDateTimeError(value);
            return null;
        }
    }

    private static void throwDateTimeError(String profileDate) throws InvalidProfileException {
        throw new InvalidProfileException(String.format("Date string '%s' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS[Z] between (inclusive) 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z", profileDate));
    }

    ConstraintReader getByTypeCode(String typeCode) {
        return typeCodeToSpecificReader.get(typeCode);
    }
}

package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.generation.StringGenerator;
import com.scottlogic.deg.generator.generation.IsinStringGenerator;
import com.scottlogic.deg.generator.restrictions.NumericRestrictions;
import com.scottlogic.deg.generator.restrictions.ParsedGranularity;
import com.scottlogic.deg.schemas.v3.AtomicConstraintType;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

public class AtomicConstraintReaderLookup {
    private static final Map<String, ConstraintReader> typeCodeToSpecificReader;

    static {
        typeCodeToSpecificReader = new HashMap<>();

        add(AtomicConstraintType.FORMATTEDAS.toString(),
                (dto, fields, rules) ->
                    new FormatConstraint(
                        fields.getByName(dto.field),
                        (String) throwIfValueNull(dto.value),
                        rules));

        add(AtomicConstraintType.ISEQUALTOCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsInSetConstraint(
                        fields.getByName(dto.field),
                        mapValues(Collections.singleton(throwIfValueNull(dto.value))),
                        rules));

        add(AtomicConstraintType.ISINSET.toString(),
                (dto, fields, rules) ->
                    new IsInSetConstraint(
                        fields.getByName(dto.field),
                        mapValues(throwIfValuesNull(dto.values)),
                        rules));

        add(AtomicConstraintType.CONTAINSREGEX.toString(),
            (dto, fields, rules) ->
                new ContainsRegexConstraint(
                    fields.getByName(dto.field),
                    Pattern.compile((String) throwIfValueNull(dto.value)),
                    rules));

        add(AtomicConstraintType.MATCHESREGEX.toString(),
                (dto, fields, rules) ->
                    new MatchesRegexConstraint(
                        fields.getByName(dto.field),
                        Pattern.compile((String) throwIfValueNull(dto.value)),
                        rules));

        add(AtomicConstraintType.AVALID.toString(),
                (dto, fields, rules) ->
                    new MatchesStandardConstraint(
                        fields.getByName(dto.field),
                        StandardConstraintTypes.valueOf((String) throwIfValueNull(dto.value)),
                        rules
                    ));

        add(AtomicConstraintType.ISGREATERTHANCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsGreaterThanConstantConstraint(
                        fields.getByName(dto.field),
                        (Number) throwIfValueNull(dto.value),
                        rules));

        add(AtomicConstraintType.ISGREATERTHANOREQUALTOCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsGreaterThanOrEqualToConstantConstraint(
                        fields.getByName(dto.field),
                        (Number) throwIfValueNull(dto.value),
                        rules));

        add(AtomicConstraintType.ISLESSTHANCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsLessThanConstantConstraint(
                        fields.getByName(dto.field),
                        (Number) throwIfValueNull(dto.value),
                        rules));

        add(AtomicConstraintType.ISLESSTHANOREQUALTOCONSTANT.toString(),
                (dto, fields, rules) ->
                    new IsLessThanOrEqualToConstantConstraint(
                        fields.getByName(dto.field),
                        (Number) throwIfValueNull(dto.value),
                        rules));

        add(AtomicConstraintType.ISBEFORECONSTANTDATETIME.toString(),
                (dto, fields, rules) ->
                    new IsBeforeConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(throwIfValueNull(dto.value)),
                        rules));

        add(AtomicConstraintType.ISBEFOREOREQUALTOCONSTANTDATETIME.toString(),
                (dto, fields, rules) ->
                    new IsBeforeOrEqualToConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(throwIfValueNull(dto.value)),
                        rules));

        add(AtomicConstraintType.ISAFTERCONSTANTDATETIME.toString(),
                (dto, fields, rules) ->
                    new IsAfterConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(throwIfValueNull(dto.value)),
                        rules));

        add(AtomicConstraintType.ISAFTEROREQUALTOCONSTANTDATETIME.toString(),
                (dto, fields, rules) ->
                    new IsAfterOrEqualToConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(throwIfValueNull(dto.value)),
                        rules));

        add(AtomicConstraintType.ISGRANULARTO.toString(),
                (dto, fields, rules) ->
                    new IsGranularToConstraint(
                        fields.getByName(dto.field),
                        ParsedGranularity.parse(throwIfValueNull(dto.value)),
                        rules));

        add(AtomicConstraintType.ISNULL.toString(),
                (dto, fields, rules) ->
                    new IsNullConstraint(fields.getByName(dto.field), rules));

        add(AtomicConstraintType.ISOFTYPE.toString(),
                (dto, fields, rules) ->
                {
                    final IsOfTypeConstraint.Types type;
                    switch ((String) throwIfValueNull(dto.value)) {
                        case "numeric":
                            type = IsOfTypeConstraint.Types.NUMERIC;
                            break;

                        case "string":
                            type = IsOfTypeConstraint.Types.STRING;
                            break;

                        case "temporal":
                            type = IsOfTypeConstraint.Types.TEMPORAL;
                            break;

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

    private static <T> T throwIfValueNull(T value) throws InvalidProfileException {
        if (value == null) {
            throw new InvalidProfileException("Couldn't recognise 'value' property, it must be set to a value");
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
        Object value = throwIfValueNull(dto.value);
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
        if (value == null || !(value instanceof Map))
            return value;

        Map objectMap = (Map) value;
        if (!objectMap.containsKey("date"))
            throw new InvalidProfileException(String.format("Object found but no 'date' property exists, found %s", Objects.toString(objectMap.keySet())));

        Object date = objectMap.get("date");
        if (!(date instanceof String))
            throw new InvalidProfileException(String.format("Date on date object must be a string, found %s", date));

        return parseDate((String)date);
    }

    private static LocalDateTime unwrapDate(Object value) throws InvalidProfileException {
        Object date = potentialUnwrapDate(value);
        if (date instanceof LocalDateTime)
            return (LocalDateTime) date;

        throw new InvalidProfileException(String.format("Dates should be expressed in object format e.g. { \"date\": \"%s\" }", value));
    }

    private static LocalDateTime parseDate(String value) throws InvalidProfileException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'.'SSS");
        try {
            return LocalDateTime.parse(value, formatter);
        } catch (DateTimeParseException dtpe){
            throw new InvalidProfileException(String.format("Date string '%s' must be in ISO-8601 format: yyyy-MM-ddTHH:mm:ss.SSS", value));
        }
    }

    public ConstraintReader getByTypeCode(String typeCode) {
        return typeCodeToSpecificReader.get(typeCode);
    }
}

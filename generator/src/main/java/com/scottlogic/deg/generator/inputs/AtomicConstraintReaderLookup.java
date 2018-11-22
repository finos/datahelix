package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.constraints.*;
import com.scottlogic.deg.generator.generation.IStringGenerator;
import com.scottlogic.deg.generator.generation.IsinStringGenerator;
import com.scottlogic.deg.generator.restrictions.ParsedGranularity;
import com.scottlogic.deg.schemas.v3.AtomicConstraintType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class AtomicConstraintReaderLookup {
    private static final Map<String, IConstraintReader> typeCodeToSpecificReader;

    static {
        Map<String, IStringGenerator> standardNameToStringGenerator = new HashMap<>();
        standardNameToStringGenerator.put("ISIN", new IsinStringGenerator());

        typeCodeToSpecificReader = new HashMap<>();

        add(AtomicConstraintType.FORMATTEDAS.toString(),
                (dto, fields) ->
                        new FormatConstraint(
                                fields.getByName(dto.field),
                                (String) dto.value));

        add(AtomicConstraintType.ISEQUALTOCONSTANT.toString(),
                (dto, fields) ->
                        new IsEqualToConstantConstraint(
                                fields.getByName(dto.field),
                                potentialUnwrapDate(dto.value)));

        add(AtomicConstraintType.ISINSET.toString(),
                (dto, fields) ->
                        new IsInSetConstraint(
                                fields.getByName(dto.field),
                                new HashSet<>(dto.values)));

        add(AtomicConstraintType.CONTAINSREGEX.toString(),
            (dto, fields) ->
                new ContainsRegexConstraint(
                    fields.getByName(dto.field),
                    Pattern.compile((String) dto.value)));

        add(AtomicConstraintType.MATCHESREGEX.toString(),
                (dto, fields) ->
                        new MatchesRegexConstraint(
                                fields.getByName(dto.field),
                                Pattern.compile((String) dto.value)));

        add(AtomicConstraintType.AVALID.toString(),
                (dto, fields) ->
                        new MatchesStandardConstraint(
                                fields.getByName(dto.field),
                                standardNameToStringGenerator.get((String) dto.value)
                        ));

        add(AtomicConstraintType.ISGREATERTHANCONSTANT.toString(),
                (dto, fields) ->
                        new IsGreaterThanConstantConstraint(
                                fields.getByName(dto.field),
                                (Number) dto.value));

        add(AtomicConstraintType.ISGREATERTHANOREQUALTOCONSTANT.toString(),
                (dto, fields) ->
                        new IsGreaterThanOrEqualToConstantConstraint(
                                fields.getByName(dto.field),
                                (Number) dto.value));

        add(AtomicConstraintType.ISLESSTHANCONSTANT.toString(),
                (dto, fields) ->
                        new IsLessThanConstantConstraint(
                                fields.getByName(dto.field),
                                (Number) dto.value));

        add(AtomicConstraintType.ISLESSTHANOREQUALTOCONSTANT.toString(),
                (dto, fields) ->
                        new IsLessThanOrEqualToConstantConstraint(
                                fields.getByName(dto.field),
                                (Number) dto.value));

        add(AtomicConstraintType.ISBEFORECONSTANTDATETIME.toString(),
                (dto, fields) ->
                        new IsBeforeConstantDateTimeConstraint(
                                fields.getByName(dto.field),
                                unwrapDate(dto.value)));

        add(AtomicConstraintType.ISBEFOREOREQUALTOCONSTANTDATETIME.toString(),
                (dto, fields) ->
                        new IsBeforeOrEqualToConstantDateTimeConstraint(
                                fields.getByName(dto.field),
                                unwrapDate(dto.value)));

        add(AtomicConstraintType.ISAFTERCONSTANTDATETIME.toString(),
                (dto, fields) ->
                        new IsAfterConstantDateTimeConstraint(
                                fields.getByName(dto.field),
                                unwrapDate(dto.value)));

        add(AtomicConstraintType.ISAFTEROREQUALTOCONSTANTDATETIME.toString(),
                (dto, fields) ->
                        new IsAfterOrEqualToConstantDateTimeConstraint(
                                fields.getByName(dto.field),
                                unwrapDate(dto.value)));

        add(AtomicConstraintType.ISGRANULARTO.toString(),
                (dto, fields) ->
                        new IsGranularToConstraint(
                                fields.getByName(dto.field),
                                ParsedGranularity.parse(dto.value)));

        add(AtomicConstraintType.ISNULL.toString(),
                (dto, fields) ->
                        new IsNullConstraint(fields.getByName(dto.field)));

        add(AtomicConstraintType.ISOFTYPE.toString(),
                (dto, fields) ->
                {
                    final IsOfTypeConstraint.Types type;
                    switch ((String) dto.value) {
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
                            type);
                });

        // String constraints
        add(AtomicConstraintType.ISSTRINGLONGERTHAN.toString(),
                (dto, fields) -> {

                    int length = ((Number) dto.value).intValue();
                    return new IsStringLongerThanConstraint(
                            fields.getByName(dto.field),
                            length);
                });

        add(AtomicConstraintType.ISSTRINGSHORTERTHAN.toString(),
                (dto, fields) -> {

                    int length = ((Number) dto.value).intValue();
                    return new IsStringShorterThanConstraint(
                            fields.getByName(dto.field),
                            length);
                });

        add(AtomicConstraintType.HASLENGTH.toString(),
                (dto, fields) -> {

                    int length = ((Number) dto.value).intValue();
                    return new StringHasLengthConstraint(
                            fields.getByName(dto.field),
                            length);
                });
    }

    private static void add(String typeCode, IConstraintReader func) {
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

    public IConstraintReader getByTypeCode(String typeCode) {
        return typeCodeToSpecificReader.get(typeCode);
    }
}

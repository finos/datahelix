package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.generation.IStringGenerator;
import com.scottlogic.deg.generator.generation.IsinStringGenerator;
import com.scottlogic.deg.generator.restrictions.ParsedGranularity;
import com.scottlogic.deg.schemas.v3.AtomicConstraintType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

public class AtomicConstraintReaderLookup {
    private static final Map<String, IConstraintReader> typeCodeToSpecificReader;

    static {
        Map<String, IStringGenerator> standardNameToStringGenerator = new HashMap<>();
        standardNameToStringGenerator.put("ISIN", new IsinStringGenerator());

        typeCodeToSpecificReader = new HashMap<>();

        add(AtomicConstraintType.FORMATTEDAS.toString(),
                (dto, fields, rule) ->
                    new FormatConstraint(
                        fields.getByName(dto.field),
                        (String) dto.value,
                        rule));

        add(AtomicConstraintType.ISEQUALTOCONSTANT.toString(),
                (dto, fields, rule) ->
                    new IsEqualToConstantConstraint(
                        fields.getByName(dto.field),
                        potentialUnwrapDate(dto.value),
                        rule));

        add(AtomicConstraintType.ISINSET.toString(),
                (dto, fields, rule) ->
                    new IsInSetConstraint(
                        fields.getByName(dto.field),
                        mapValues(dto.values),
                        rule));

        add(AtomicConstraintType.CONTAINSREGEX.toString(),
            (dto, fields, rule) ->
                new ContainsRegexConstraint(
                    fields.getByName(dto.field),
                    Pattern.compile((String) dto.value),
                    rule));

        add(AtomicConstraintType.MATCHESREGEX.toString(),
                (dto, fields, rule) ->
                    new MatchesRegexConstraint(
                        fields.getByName(dto.field),
                        Pattern.compile((String) dto.value),
                        rule));

        add(AtomicConstraintType.AVALID.toString(),
                (dto, fields, rule) ->
                    new MatchesStandardConstraint(
                        fields.getByName(dto.field),
                        standardNameToStringGenerator.get((String) dto.value),
                        rule
                    ));

        add(AtomicConstraintType.ISGREATERTHANCONSTANT.toString(),
                (dto, fields, rule) ->
                    new IsGreaterThanConstantConstraint(
                        fields.getByName(dto.field),
                        (Number) dto.value,
                        rule));

        add(AtomicConstraintType.ISGREATERTHANOREQUALTOCONSTANT.toString(),
                (dto, fields, rule) ->
                    new IsGreaterThanOrEqualToConstantConstraint(
                        fields.getByName(dto.field),
                        (Number) dto.value,
                        rule));

        add(AtomicConstraintType.ISLESSTHANCONSTANT.toString(),
                (dto, fields, rule) ->
                    new IsLessThanConstantConstraint(
                        fields.getByName(dto.field),
                        (Number) dto.value,
                        rule));

        add(AtomicConstraintType.ISLESSTHANOREQUALTOCONSTANT.toString(),
                (dto, fields, rule) ->
                    new IsLessThanOrEqualToConstantConstraint(
                        fields.getByName(dto.field),
                        (Number) dto.value,
                        rule));

        add(AtomicConstraintType.ISBEFORECONSTANTDATETIME.toString(),
                (dto, fields, rule) ->
                    new IsBeforeConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(dto.value),
                        rule));

        add(AtomicConstraintType.ISBEFOREOREQUALTOCONSTANTDATETIME.toString(),
                (dto, fields, rule) ->
                    new IsBeforeOrEqualToConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(dto.value),
                        rule));

        add(AtomicConstraintType.ISAFTERCONSTANTDATETIME.toString(),
                (dto, fields, rule) ->
                    new IsAfterConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(dto.value),
                        rule));

        add(AtomicConstraintType.ISAFTEROREQUALTOCONSTANTDATETIME.toString(),
                (dto, fields, rule) ->
                    new IsAfterOrEqualToConstantDateTimeConstraint(
                        fields.getByName(dto.field),
                        unwrapDate(dto.value),
                        rule));

        add(AtomicConstraintType.ISGRANULARTO.toString(),
                (dto, fields, rule) ->
                    new IsGranularToConstraint(
                        fields.getByName(dto.field),
                        ParsedGranularity.parse(dto.value),
                        rule));

        add(AtomicConstraintType.ISNULL.toString(),
                (dto, fields, rule) ->
                    new IsNullConstraint(fields.getByName(dto.field), rule));

        add(AtomicConstraintType.ISOFTYPE.toString(),
                (dto, fields, rule) ->
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
                        type,
                        rule);
                });

        // String constraints
        add(AtomicConstraintType.ISSTRINGLONGERTHAN.toString(),
                (dto, fields, rule) -> {

                    int length = ((Number) dto.value).intValue();
                    return new IsStringLongerThanConstraint(
                        fields.getByName(dto.field),
                        length,
                        rule);
                });

        add(AtomicConstraintType.ISSTRINGSHORTERTHAN.toString(),
                (dto, fields, rule) -> {

                    int length = ((Number) dto.value).intValue();
                    return new IsStringShorterThanConstraint(
                        fields.getByName(dto.field),
                        length,
                        rule);
                });

        add(AtomicConstraintType.HASLENGTH.toString(),
                (dto, fields, rule) -> {

                    int length = ((Number) dto.value).intValue();
                    return new StringHasLengthConstraint(
                        fields.getByName(dto.field),
                        length,
                        rule);
                });
    }

    private static Set<Object> mapValues(Collection<Object> values) throws InvalidProfileException {
        HashSet<Object> mappedValues = new HashSet<>();

        for (Object value: values){
            mappedValues.add(AtomicConstraintReaderLookup.potentialUnwrapDate(value));
        }

        return mappedValues;
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

package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.constraints.*;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

public class AtomicConstraintReaderLookup
{
    private static final Map<String, IConstraintReader> typeCodeToSpecificReader;

    static {
        typeCodeToSpecificReader = new HashMap<>();

        add("isEqualTo",
            (dto, fields) ->
                new IsEqualToConstantConstraint(
                    fields.byId(dto.field),
                    dto.value));

        add("isInSet",
            (dto, fields) ->
                new IsInSetConstraint(
                    fields.byId(dto.field),
                    new HashSet<>(dto.values)));

        add("matchesRegex",
            (dto, fields) ->
                new MatchesRegexConstraint(
                    fields.byId(dto.field),
                    Pattern.compile((String) dto.value)));

        add("isGreaterThan",
            (dto, fields) ->
                new IsGreaterThanConstantConstraint(
                    fields.byId(dto.field),
                    (Number)dto.value));

        add("isGreaterThanOrEqualTo",
            (dto, fields) ->
                new IsGreaterThanOrEqualToConstantConstraint(
                    fields.byId(dto.field),
                    (Number)dto.value));

        add("isNull",
            (dto, fields) ->
                new IsNullConstraint(fields.byId(dto.field)));

        add("isOfType",
            (dto, fields) ->
            {
                final IsOfTypeConstraint.Types type;
                switch ((String)dto.value) {
                    case "numeric":
                        type = IsOfTypeConstraint.Types.Numeric;
                        break;

                    case "string":
                        type = IsOfTypeConstraint.Types.String;
                        break;

                    case "temporal":
                        type = IsOfTypeConstraint.Types.Temporal;
                        break;

                    default:
                       throw new InvalidProfileException("Unrecognised type in type constraint: " + dto.value);
                }

                return new IsOfTypeConstraint(
                    fields.byId(dto.field),
                    type);
            });
    }

    private static void add(String typeCode, IConstraintReader func)
    {
        typeCodeToSpecificReader.put(typeCode, func);
    }

    IConstraintReader getByTypeCode(String typeCode)
    {
        return typeCodeToSpecificReader.get(typeCode);
    }
}

package com.scottlogic.deg.profile.reader.constraintreaders;

import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedDateGranularity;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsGranularToDateConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsGranularToNumericConstraint;
import com.scottlogic.deg.profile.reader.ConstraintReader;
import com.scottlogic.deg.profile.reader.ConstraintReaderHelpers;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import com.scottlogic.deg.profile.v0_1.ConstraintDTO;

import java.util.Optional;

public class GranularToReader implements ConstraintReader {
    @Override
    public Constraint apply(ConstraintDTO dto, ProfileFields fields) {
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
}

package com.scottlogic.deg.profile.reader.constraintreaders;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.profile.reader.ConstraintReader;
import com.scottlogic.deg.profile.reader.ConstraintReaderHelpers;
import com.scottlogic.deg.profile.reader.file.names.NameRetriever;
import com.scottlogic.deg.profile.v0_1.ConstraintDTO;

import java.math.BigDecimal;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.*;

public class OfTypeReader implements ConstraintReader {

    @Override
    public Constraint apply(ConstraintDTO dto, ProfileFields fields) {
        Field field = fields.getByName(dto.field);

        String value = ConstraintReaderHelpers.getValidatedValue(dto, String.class);
        switch (value) {
            case "decimal":
                return new IsOfTypeConstraint(field, NUMERIC);

            case "string":
                return new IsOfTypeConstraint(field, STRING);

            case "datetime":
                return new IsOfTypeConstraint(field, DATETIME);

            case "integer":
                return new AndConstraint(
                    new IsOfTypeConstraint(field, NUMERIC),
                    new IsGranularToNumericConstraint(field, new ParsedGranularity(BigDecimal.ONE)));

            case "ISIN":
            case "SEDOL":
            case "CUSIP":
            case "RIC":
                return new AndConstraint(
                    new MatchesStandardConstraint(field, StandardConstraintTypes.valueOf(value)),
                    new IsOfTypeConstraint(field, IsOfTypeConstraint.Types.STRING)
                );
        }

        try {
            NameConstraintTypes nameType = NameConstraintTypes.lookupProfileText(value);
            DistributedSet<Object> objectDistributedSet = NameRetriever.loadNamesFromFile(nameType);
            return new IsInSetConstraint(field, objectDistributedSet);
        } catch (UnsupportedOperationException e){
            throw new ValidationException("Profile is invalid: no constraints known for \"is\": \"ofType\", \"value\": \"" + value + "\"");
        }
    }
}

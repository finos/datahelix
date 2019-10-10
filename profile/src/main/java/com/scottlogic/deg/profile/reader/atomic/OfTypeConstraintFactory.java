package com.scottlogic.deg.profile.reader.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularityFactory;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import com.scottlogic.deg.profile.reader.file.names.NameRetriever;

import java.math.BigDecimal;
import java.util.Optional;


public class OfTypeConstraintFactory {
    public static Optional<Constraint> create(Field field, String value){
        switch (value) {
            case "decimal":
            case "string":
            case "datetime":
                return Optional.empty();
            case "integer":
                return Optional.of(new IsGranularToNumericConstraint(field,
                    NumericGranularityFactory.create(BigDecimal.ONE))
                );
            case "ISIN":
            case "SEDOL":
            case "CUSIP":
            case "RIC":
                return Optional.of(new MatchesStandardConstraint(
                    field,
                    StandardConstraintTypes.valueOf(value.toUpperCase()))
                );
            case "firstname":
            case "lastname":
            case "fullname":
                return Optional.of(new IsInSetConstraint(
                    field,
                    NameRetriever.loadNamesFromFile(NameConstraintTypes.lookupProfileText(value.toLowerCase())))
                );
        }

        throw new InvalidProfileException("Profile is invalid: no constraints known for \"is\": \"ofType\", \"value\": \"" + value + "\"");
    }
}

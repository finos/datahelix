package com.scottlogic.deg.profile.reader.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.SpecificFieldType;
import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularityFactory;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.reader.file.names.NameRetriever;

import java.math.BigDecimal;
import java.util.Optional;

public class OfTypeConstraintFactory {
    public static Optional<Constraint> create(Field field, SpecificFieldType specificFieldType){
        String type = specificFieldType.getType();
        switch (specificFieldType) {
            case INTEGER:
                return Optional.of(new IsGranularToNumericConstraint(field, NumericGranularityFactory.create(BigDecimal.ONE)));
            case ISIN:
            case SEDOL:
            case CUSIP:
            case RIC:
                return Optional.of(new MatchesStandardConstraint(field, StandardConstraintTypes.valueOf(type.toUpperCase())));
            case FIRST_NAME:
            case LAST_NAME:
            case FULL_NAME:
                return Optional.of(new IsInSetConstraint(field, NameRetriever.loadNamesFromFile(NameConstraintTypes.lookupProfileText(type.toLowerCase()))));
            default:
                return Optional.empty();
        }
    }
}

package com.scottlogic.deg.profile.reader.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.SpecificFieldType;
import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularityFactory;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.reader.file.names.NameRetriever;

import java.math.BigDecimal;
import java.util.Optional;

public class FieldReader
{
    public static Optional<Constraint> read(Field field, SpecificFieldType type)
    {
        switch (type) {
            case INTEGER:
                return Optional.of(new IsGranularToNumericConstraint(field, NumericGranularityFactory.create(BigDecimal.ONE)));
            case ISIN:
                return Optional.of(new MatchesStandardConstraint(field, StandardConstraintTypes.ISIN));
            case SEDOL:
                return Optional.of(new MatchesStandardConstraint(field, StandardConstraintTypes.SEDOL));
            case CUSIP:
                return Optional.of(new MatchesStandardConstraint(field, StandardConstraintTypes.CUSIP));
            case RIC:
                return Optional.of(new MatchesStandardConstraint(field, StandardConstraintTypes.RIC));
            case FIRST_NAME:
                return Optional.of(new IsInSetConstraint(field, NameRetriever.loadNamesFromFile(NameConstraintTypes.FIRST)));
            case LAST_NAME:
                return Optional.of(new IsInSetConstraint(field, NameRetriever.loadNamesFromFile(NameConstraintTypes.LAST)));
            case FULL_NAME:
                return Optional.of(new IsInSetConstraint(field, NameRetriever.loadNamesFromFile(NameConstraintTypes.FULL)));
            default:
                return Optional.empty();
        }
    }
}

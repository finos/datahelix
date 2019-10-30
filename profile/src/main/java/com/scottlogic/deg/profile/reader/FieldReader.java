package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.profile.DateTimeGranularity;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.NumericGranularity;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class FieldReader {
    public static Optional<Constraint> read(Field field) {
        switch (field.getSpecificType()) {
            case DATE:
                return Optional.of(new IsGranularToDateConstraint(field, new DateTimeGranularity(ChronoUnit.DAYS)));
            case INTEGER:
                return Optional.of(new IsGranularToNumericConstraint(field, NumericGranularity.create(BigDecimal.ONE)));
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

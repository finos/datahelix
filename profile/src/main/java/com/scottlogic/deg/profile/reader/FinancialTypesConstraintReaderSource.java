package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.MatchesStandardConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.StandardConstraintTypes;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;

import java.util.stream.Stream;

public class FinancialTypesConstraintReaderSource implements ConstraintReaderMapEntrySource {
    public Stream<ConstraintReaderMapEntry> getConstraintReaderMapEntries() {
        ConstraintReader financialCodesReader = (dto, fields, rules) -> {
            StandardConstraintTypes standardType =
                StandardConstraintTypes.valueOf(
                    ConstraintReaderHelpers.getValidatedValue(dto, String.class)
                );
            Field field = fields.getByName(dto.field);
            switch (standardType) {
                case ISIN:
                case SEDOL:
                case CUSIP:
                case RIC:
                    return new AndConstraint(
                        new MatchesStandardConstraint(field, standardType, rules),
                        new IsOfTypeConstraint(field, IsOfTypeConstraint.Types.STRING, rules)
                    );
                default:
                    return new MatchesStandardConstraint(field, standardType, rules);
            }
        };

        return Stream.of(
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                "ISIN",
                financialCodesReader
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                "SEDOL",
                financialCodesReader
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                "CUSIP",
                financialCodesReader
            ),
            new ConstraintReaderMapEntry(
                AtomicConstraintType.IS_OF_TYPE.getText(),
                "RIC",
                financialCodesReader
            )
        );
    }
}

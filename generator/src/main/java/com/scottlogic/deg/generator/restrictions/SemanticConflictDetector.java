package com.scottlogic.deg.generator.restrictions;

import java.util.Objects;
import java.util.stream.Stream;

interface IFieldSpecSatisfiabilityChecker {
    boolean isSatisfiable(FieldSpec fieldSpec);
}

class ConflictingTypesSatisfiabilityChecker implements IFieldSpecSatisfiabilityChecker {
    @Override
    public boolean isSatisfiable(FieldSpec fieldSpec) {
        return Stream.of(
            fieldSpec.getDateTimeRestrictions(),
            fieldSpec.getNumericRestrictions(),
            fieldSpec.getStringRestrictions())
            .filter(Objects::nonNull)
            .count() <= 1;
    }
}

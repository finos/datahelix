package com.scottlogic.deg.generator.restrictions;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.generator.constraints.IsOfTypeConstraint.*;

class SemanticConflictDetector {
    boolean detectSemanticConflict(FieldSpec left, FieldSpec right) {
        final Iterable<SemanticEnabledTuple> leftRestrictions = detect(left);
        final Iterator<SemanticEnabledTuple> rightRestrictions = detect(right).iterator();

        Types semanticWanted = null;
        for (SemanticEnabledTuple leftRestriction : leftRestrictions) {
            final SemanticEnabledTuple rightRestriction = rightRestrictions.next();

            if (leftRestriction.present || rightRestriction.present) {
                if (semanticWanted != null) {
                    // TODO: need branch `satisfiability` to be able to throw an exception here
                    return true;
                }
                semanticWanted = leftRestriction.semantic;
            }
        }

        return false;
    }

    private Iterable<SemanticEnabledTuple> detect(FieldSpec fieldSpec) {
        return Stream.of(
                new SemanticEnabledTuple(Types.Numeric, fieldSpec.getNumericRestrictions() != null),
                new SemanticEnabledTuple(Types.String, fieldSpec.getStringRestrictions() != null),
                new SemanticEnabledTuple(Types.Temporal, fieldSpec.getDateTimeRestrictions() != null)
        ).collect(Collectors.toList());
    }

    private static class SemanticEnabledTuple {
        private final Types semantic;
        private final boolean present;

        private SemanticEnabledTuple(
                Types semantic,
                boolean present
        ) {
            this.semantic = semantic;
            this.present = present;
        }
    }
}

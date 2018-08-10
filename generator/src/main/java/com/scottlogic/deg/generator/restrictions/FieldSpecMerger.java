package com.scottlogic.deg.generator.restrictions;

/**
 * Returns a FieldSpec that permits only data permitted by all of its inputs
 */
public class FieldSpecMerger {
    private final SetRestrictionsMerger setRestrictionsMerger = new SetRestrictionsMerger();
    private final NumericRestrictionsMerger numericRestrictionsMerger = new NumericRestrictionsMerger();
    private final StringRestrictionsMerger stringRestrictionsMerger = new StringRestrictionsMerger();
    private final NullRestrictionsMerger nullRestrictionsMerger = new NullRestrictionsMerger();
    private final DateTimeRestrictionsMerger dateTimeRestrictionsMerger = new DateTimeRestrictionsMerger();
    private final SemanticConflictDetector semanticConflictDetector = new SemanticConflictDetector();

    public FieldSpec merge(FieldSpec left, FieldSpec right) {
        if (semanticConflictDetector.detectSemanticConflict(left, right)) {
            // TODO: need branch `satisfiability` to be able to throw an exception here
        }

        final FieldSpec merged = new FieldSpec();
        merged.setSetRestrictions(setRestrictionsMerger.merge(left.getSetRestrictions(), right.getSetRestrictions()));
        merged.setNumericRestrictions(numericRestrictionsMerger.merge(left.getNumericRestrictions(),
                right.getNumericRestrictions()));
        merged.setStringRestrictions(stringRestrictionsMerger.merge(left.getStringRestrictions(),
                right.getStringRestrictions()));
        merged.setNullRestrictions(nullRestrictionsMerger.merge(left.getNullRestrictions(), right.getNullRestrictions()));
        merged.setDateTimeRestrictions(dateTimeRestrictionsMerger.merge(left.getDateTimeRestrictions(),
                right.getDateTimeRestrictions()));
        return merged;
    }
}

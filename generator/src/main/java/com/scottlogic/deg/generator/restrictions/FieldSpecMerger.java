package com.scottlogic.deg.generator.restrictions;

import java.util.Optional;

/**
 * Returns a FieldSpec that permits only data permitted by all of its inputs
 */
public class FieldSpecMerger {
    private final SetRestrictionsMerger setRestrictionsMerger = new SetRestrictionsMerger();
    private final NumericRestrictionsMerger numericRestrictionsMerger = new NumericRestrictionsMerger();
    private final StringRestrictionsMerger stringRestrictionsMerger = new StringRestrictionsMerger();
    private final NullRestrictionsMerger nullRestrictionsMerger = new NullRestrictionsMerger();
    private final DateTimeRestrictionsMerger dateTimeRestrictionsMerger = new DateTimeRestrictionsMerger();
    private final FormatRestrictionsMerger formatRestrictionMerger = new FormatRestrictionsMerger();

    private final IFieldSpecSatisfiabilityChecker satisfiabilityChecker = new ConflictingTypesSatisfiabilityChecker();

    /**
     * Null parameters are permitted, and are synonymous with an empty FieldSpec
     *
     * Returning an empty Optional conveys that the fields were unmergeable.
     */
    public Optional<FieldSpec> merge(FieldSpec left, FieldSpec right) {
        if (left == null && right == null) {
            return Optional.of(new FieldSpec());
        }
        if (left == null) {
            return Optional.of(right);
        }
        if (right == null) {
            return Optional.of(left);
        }
        final FieldSpec merged = new FieldSpec();
        try {
            merged.setSetRestrictions(setRestrictionsMerger.merge(left.getSetRestrictions(), right.getSetRestrictions()));
            merged.setNumericRestrictions(numericRestrictionsMerger.merge(left.getNumericRestrictions(),
                    right.getNumericRestrictions()));
            merged.setStringRestrictions(stringRestrictionsMerger.merge(left.getStringRestrictions(),
                    right.getStringRestrictions()));
            merged.setNullRestrictions(nullRestrictionsMerger.merge(left.getNullRestrictions(), right.getNullRestrictions()));
            merged.setDateTimeRestrictions(dateTimeRestrictionsMerger.merge(left.getDateTimeRestrictions(),
                    right.getDateTimeRestrictions()));
            merged.setFormatRestrictions(formatRestrictionMerger.merge(left.getFormatRestrictions(), right.getFormatRestrictions()));
        } catch (UnmergeableRestrictionException e) {
            return Optional.empty();
        }

        if (!satisfiabilityChecker.isSatisfiable(merged)) {
            return Optional.empty();
        }

        return Optional.of(merged);
    }
}

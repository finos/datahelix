package com.scottlogic.deg.restriction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class FieldSpecMerger {
    private final SetRestrictionsMerger setRestrictionsMerger = new SetRestrictionsMerger();
    private final NumericRestrictionsMerger numericRestrictionsMerger = new NumericRestrictionsMerger();

    public FieldSpec merge(FieldSpec left, FieldSpec right) {
        final FieldSpec merged = new FieldSpec(getMergedName(left.getName(), right.getName()));
        merged.setSetRestrictions(setRestrictionsMerger.merge(left.getSetRestrictions(), right.getSetRestrictions()));
        merged.setNumericRestrictions(numericRestrictionsMerger.merge(left.getNumericRestrictions(), right.getNumericRestrictions()));
        return merged;
    }

    private String getMergedName(String left, String right) {
        if (!left.equals(right)) {
            throw new IllegalStateException();
        }
        return left;
    }
}

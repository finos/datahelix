package com.scottlogic.deg.generator.restrictions;

import java.util.Optional;

/**
 * Returns a FieldSpec that permits only data permitted by all of its inputs
 */
public class FieldSpecMerger {
    private static final RestrictionMergeOperation initialMergeOperation = new TypesRestrictionMergeOperation();

    private static final RestrictionMergeOperation[] mergeOperations = new RestrictionMergeOperation[]{
        new StringRestrictionsMergeOperation(),
        new NumericRestrictionsMergeOperation(),
        new DateTimeRestrictionsMergeOperation(),
        new NullRestrictionsMergeOperation(),
        new FormatRestrictionsMergeOperation(),
        new GranularityRestrictionsMergeOperation()
    };

    private static final RestrictionMergeOperation finalMergeOperation = new SetRestrictionsMergeOperation();

    /**
     * Null parameters are permitted, and are synonymous with an empty FieldSpec
     * <p>
     * Returning an empty Optional conveys that the fields were unmergeable.
     */
    public Optional<FieldSpec> merge(FieldSpec left, FieldSpec right) {
        if (left == null && right == null) {
            return Optional.of(FieldSpec.Empty);
        }
        if (left == null) {
            return Optional.of(right);
        }
        if (right == null) {
            return Optional.of(left);
        }

        Optional<FieldSpec> merged = Optional.of(FieldSpec.Empty);

        //operation/s that must happen first
        merged = initialMergeOperation.applyMergeOperation(left, right, merged.get());
        if (!merged.isPresent()){
            return merged;
        }

        //operations that can happen in any order
        for (RestrictionMergeOperation operation : mergeOperations){
            merged = operation.applyMergeOperation(left, right, merged.get());
            if (!merged.isPresent()){
                return merged;
            }
        }

        //operation/s that must happen last
        merged = finalMergeOperation.applyMergeOperation(left, right, merged.get());
        if (!merged.isPresent()){
            return merged;
        }

        return merged;
    }
}

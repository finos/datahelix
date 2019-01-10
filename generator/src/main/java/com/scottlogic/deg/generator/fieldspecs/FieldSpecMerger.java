package com.scottlogic.deg.generator.fieldspecs;

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
        new GranularityRestrictionsMergeOperation(),
        new MustContainRestrictionMergeOperation()
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

        Optional<FieldSpec> merging = Optional.of(FieldSpec.Empty);

        //operation/s that must happen first
        merging = initialMergeOperation.applyMergeOperation(left, right, merging.get());
        if (!merging.isPresent()){
            return Optional.empty();
        }

        //operations that can happen in any order
        for (RestrictionMergeOperation operation : mergeOperations){
            merging = operation.applyMergeOperation(left, right, merging.get());
            if (!merging.isPresent()){
                return Optional.empty();
            }
        }

        //operation/s that must happen last
        return finalMergeOperation.applyMergeOperation(left, right, merging.get());
    }
}

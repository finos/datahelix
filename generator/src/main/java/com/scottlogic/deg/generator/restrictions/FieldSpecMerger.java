package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            return Optional.of(new FieldSpec());
        }
        if (left == null) {
            return Optional.of(right);
        }
        if (right == null) {
            return Optional.of(left);
        }
        final FieldSpec merged = new FieldSpec();

        //operation/s that must happen first
        if (!initialMergeOperation.successful(left, right, merged)){
            return Optional.empty();
        }

        //operations that can happen in any order
        for (RestrictionMergeOperation operation : mergeOperations){
            if (!operation.successful(left, right, merged)){
                return Optional.empty();
            }
        }

        //operation/s that must happen last
        if (!finalMergeOperation.successful(left, right, merged)){
            return Optional.empty();
        }

        return Optional.of(merged);
    }
}

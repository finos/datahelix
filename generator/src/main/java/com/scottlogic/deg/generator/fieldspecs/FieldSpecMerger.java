package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Returns a FieldSpec that permits only data permitted by all of its inputs
 */
public class FieldSpecMerger {
    private static final RestrictionMergeOperation initialMergeOperation = new TypesRestrictionMergeOperation();

    private static final RestrictionMergeOperation[] mergeOperations = new RestrictionMergeOperation[]{
        initialMergeOperation,
        new StringRestrictionsMergeOperation(),
        new NumericRestrictionsMergeOperation(new NumericRestrictionsMerger()),
        new DateTimeRestrictionsMergeOperation(new DateTimeRestrictionsMerger()),
        new NullRestrictionsMergeOperation(),
        new BlacklistRestictionsMergeOperation()
    };

    /**
     * Null parameters are permitted, and are synonymous with an empty FieldSpec
     * <p>
     * Returning an empty Optional conveys that the fields were unmergeable.
     */
    public Optional<FieldSpec> merge(FieldSpec left, FieldSpec right) {
        if (hasSet(left) && hasSet(right)) {
            return mergeSets(left, right);
        }
        if (hasSet(left)) {
            return combineSetWithRestrictions(left, right);
        }
        if (hasSet(right)) {
            return combineSetWithRestrictions(right, left);
        }
        return combineRestrictions(left, right);
    }

    private Optional<FieldSpec> mergeSets(FieldSpec left, FieldSpec right) {
        Set<Object> set = SetUtils.intersect(
            left.getSetRestrictions().getWhitelist(),
            right.getSetRestrictions().getWhitelist());

        return addNullable(left, right, set);
    }

    private Optional<FieldSpec> combineSetWithRestrictions(FieldSpec set, FieldSpec restrictions) {
        Set<Object> newSet = set.getSetRestrictions().getWhitelist().stream()
            .filter(restrictions::permits)
            .collect(Collectors.toSet());

        return addNullable(set, restrictions, newSet);
    }

    private Optional<FieldSpec> addNullable(FieldSpec left, FieldSpec right, Set<Object> set) {
        FieldSpec newFieldSpec = FieldSpec.Empty.withSetRestrictions(new SetRestrictions(set));

        newFieldSpec = addFormatting(left, right, newFieldSpec);

        if (isNullable(left, right)){
            return Optional.of(newFieldSpec);
        }

        if (set.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(newFieldSpec.withNotNull());
    }

    private boolean hasSet(FieldSpec fieldSpec) {
        return fieldSpec.getSetRestrictions() != null;
    }

    private boolean isNullable(FieldSpec left, FieldSpec right) {
        return left.isNullable() && right.isNullable();
    }

    private FieldSpec addFormatting(FieldSpec left, FieldSpec right, FieldSpec newFieldSpec) {
        if (left.getFormatting() != null){
            return newFieldSpec.withFormatting(left.getFormatting());
        }
        if (right.getFormatting() != null){
            return newFieldSpec.withFormatting(right.getFormatting());
        }
        return newFieldSpec;
    }

    private Optional<FieldSpec> combineRestrictions(FieldSpec left, FieldSpec right) {
        Optional<FieldSpec> merging = Optional.of(FieldSpec.Empty);

        for (RestrictionMergeOperation operation : mergeOperations){
            merging = operation.applyMergeOperation(left, right, merging.get());
            if (!merging.isPresent()){
                return Optional.empty();
            }
        }

        if (cannotEmitAnyData(merging.get())){
            return Optional.empty();
        }

        return Optional.of(addFormatting(left, right, merging.get()));
    }

    private boolean cannotEmitAnyData(FieldSpec fieldSpec){
        return !fieldSpec.isNullable() && fieldSpec.getTypeRestrictions().getAllowedTypes().isEmpty();

    }
}

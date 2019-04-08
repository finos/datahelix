package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.constraints.StringConstraintsCollection;
import com.scottlogic.deg.generator.generation.RegexStringGenerator;
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictionsMerger;
import com.scottlogic.deg.generator.restrictions.Nullness;
import com.scottlogic.deg.generator.restrictions.NumericRestrictionsMerger;
import com.scottlogic.deg.generator.restrictions.StringRestrictions;

import java.util.Collections;
import java.util.Optional;

/**
 * Returns a FieldSpec that permits only data permitted by all of its inputs
 */
public class FieldSpecMerger {
    private static final RestrictionMergeOperation initialMergeOperation = new TypesRestrictionMergeOperation();

    private static final RestrictionMergeOperation[] mergeOperations = new RestrictionMergeOperation[]{
        new StringRestrictionsMergeOperation(),
        new NumericRestrictionsMergeOperation(new NumericRestrictionsMerger()),
        new DateTimeRestrictionsMergeOperation(new DateTimeRestrictionsMerger()),
        new NullRestrictionsMergeOperation(),
        new FormatRestrictionsMergeOperation(),
        new GranularityRestrictionsMergeOperation(),
        new MustContainRestrictionMergeOperation()
    };

    private static final StringRestrictions maxLengthRestriction = createMaxLengthStringRestriction(1000);

    private static StringRestrictions createMaxLengthStringRestriction(@SuppressWarnings("SameParameterValue") int maxLength) {
        StringRestrictions restrictions = new StringRestrictions(
            new StringConstraintsCollection(Collections.emptySet()));

        restrictions.stringGenerator = new RegexStringGenerator(".{0," + maxLength + "}", true);

        return restrictions;
    }

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

        Optional<FieldSpec> merging = Optional.of(
            FieldSpec.Empty
                .withStringRestrictions(maxLengthRestriction, FieldSpecSource.Empty));

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
        Optional<FieldSpec> fieldSpec = finalMergeOperation.applyMergeOperation(left, right, merging.get());

        if (!fieldSpec.isPresent() || cannotEmitAnyData(fieldSpec.get())){
            return Optional.empty();
        }

        return fieldSpec;
    }

    private boolean cannotEmitAnyData(FieldSpec fieldSpec){
        if (fieldSpec.getNullRestrictions() == null || fieldSpec.getNullRestrictions().nullness.equals(Nullness.MUST_BE_NULL)){
            return false;
        }

        if (fieldSpec.getTypeRestrictions().getAllowedTypes().isEmpty()){
            return true;
        }

        if (!(fieldSpec.getSetRestrictions() == null) &&  fieldSpec.getSetRestrictions().getWhitelist() != null && fieldSpec.getSetRestrictions().getWhitelist().isEmpty()){
            return true;
        }

        return false;
    }
}

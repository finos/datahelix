package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.restrictions.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.scottlogic.deg.generator.restrictions.DateTimeRestrictions.isDateTime;
import static com.scottlogic.deg.generator.restrictions.NumericRestrictions.isNumeric;
import static com.scottlogic.deg.generator.restrictions.StringRestrictions.isString;

public class SetRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final SetRestrictionsMerger setRestrictionsMerger = new SetRestrictionsMerger();

    /**
     * Create a new FieldSpec that accepts only values accepted by all input FieldSpecs
     *
     * @param left One of the two FieldSpecs that are being merged together
     * @param right One of the two FieldSpecs that are being merged together
     * @param partiallyMerged The result of merging some of the properties of the FieldSpec.
     *     This FieldSpec is used to filter the whitelist/blacklist, so it must be complete in terms of any
     *     other non-set-related value-validity restrictions. For example, it must have the merged NumericRestrictions
     *     of left and right, so that numbers in the whitelist can be checked for consistency with those
     *     restrictions.
     */
    @Override
    public Optional<FieldSpec> applyMergeOperation(
        @NotNull FieldSpec left,
        @NotNull FieldSpec right,
        @NotNull FieldSpec partiallyMerged) {

        // some boilerplate here; the set-specific logic is in getNewSetRestrictions
        return Optional.of(
            partiallyMerged.withSetRestrictions(
                getNewSetRestrictions(
                    left.getSetRestrictions(),
                    right.getSetRestrictions(),
                    getValueValidityPredicate(partiallyMerged)),
                FieldSpecSource.fromFieldSpecs(left, right)));
    }

    private SetRestrictions getNewSetRestrictions(
        @Nullable SetRestrictions setRestrictionsA,
        @Nullable SetRestrictions setRestrictionsB,
        @NotNull Predicate<Object> valueIsValid) {

        MergeResult<SetRestrictions> mergeResult =
            setRestrictionsMerger.merge(
                setRestrictionsA,
                setRestrictionsB);

        // if the merge result was not successful, it means both mergees had set restrictions and there are no mutually
        // satisfactory values
        // (this could happen if we merged, eg, "X in [1, 2]" with either "X in [3, 4]" or "NOT(X in [1, 2])")
        if (!mergeResult.successful){
            return SetRestrictions.allowNoValues();
        }

        SetRestrictions mergedSetRestrictions = mergeResult.restrictions;

        // a null merge result means neither of the mergees had any set restrictions, so just return null here
        if (mergedSetRestrictions == null) {
            return null;
        }

        // filter down whitelist/blacklist to remove values excluded by other restrictions (eg NumericRestrictions)
        if (mergedSetRestrictions.getWhitelist() != null) {
            return SetRestrictions.fromWhitelist(
                filterSet(
                    mergedSetRestrictions.getWhitelist(),
                    valueIsValid));
        } else {
            return SetRestrictions.fromBlacklist(
                filterSet(
                    mergedSetRestrictions.getBlacklist(),
                    valueIsValid));
        }
    }

    /** If input is non-null, return set of all values that match the given predicate */
    private static Set<Object> filterSet(Set<Object> input, Predicate<Object> shouldKeepItem) {
        if (input == null) {
            return null;
        }

        return input.stream()
            .filter(shouldKeepItem)
            .collect(Collectors.toCollection(HashSet::new));
    }

    /** Create a predicate that returns TRUE for value X iff the provided fieldspec permits X */
    private static Predicate<Object> getValueValidityPredicate(FieldSpec fieldSpec) {
        TypeRestrictions typeRestrictions = fieldSpec.getTypeRestrictions();

        List<Predicate<Object>> predicates = new ArrayList<>();

        if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.NUMERIC)) {
            predicates.add(x -> !isNumeric(x));
        }

        if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.STRING)) {
            predicates.add(x -> !isString(x));
        }

        if (!typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.DATETIME)) {
            predicates.add(x -> !isDateTime(x));
        }

        if (fieldSpec.getStringRestrictions() != null) {
            predicates.add(x -> !isString(x) || fieldSpec.getStringRestrictions().match(x));
        }

        if (fieldSpec.getNumericRestrictions() != null) {
            predicates.add(x -> !isNumeric(x) || fieldSpec.getNumericRestrictions().match(x));
        }

        if (fieldSpec.getDateTimeRestrictions() != null) {
            predicates.add(x -> !isDateTime(x) || fieldSpec.getDateTimeRestrictions().match(x));
        }

        return x -> predicates.stream().allMatch(p -> p.test(x));
    }
}

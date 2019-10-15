/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.fieldspecs.whitelist.WeightedElement;
import com.scottlogic.deg.generator.generation.fieldvaluesources.NullOnlySource;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsMerger;
import com.scottlogic.deg.generator.restrictions.TypedRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsMerger;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Returns a FieldSpec that permits only data permitted by all of its inputs
 */
public class FieldSpecMerger {
    private final RestrictionsMergeOperation restrictionMergeOperation =
        new RestrictionsMergeOperation(new LinearRestrictionsMerger(), new StringRestrictionsMerger());

    /**
     * Null parameters are permitted, and are synonymous with an empty FieldSpec
     * <p>
     * Returning an empty Optional conveys that the fields were unmergeable.
     */
    public Optional<FieldSpec> merge(FieldSpec left, FieldSpec right) {
        if (nullOnly(left) || nullOnly(right)){
            return nullOnlyOrEmpty(isNullable(left, right));
        }
        if (hasSet(left) && hasSet(right)) {
            return mergeSets((WhitelistFieldSpec) left, (WhitelistFieldSpec)right);
        }
        if (hasSet(left)) {
            return combineSetWithRestrictions((WhitelistFieldSpec)left, right);
        }
        if (hasSet(right)) {
            return combineSetWithRestrictions((WhitelistFieldSpec)right, left);
        }
        return combineRestrictions((RestrictionsFieldSpec)left, (RestrictionsFieldSpec)right);
    }

    private static WeightedElement<Object> mergeElements(WeightedElement<Object> left,
                                                         WeightedElement<Object> right) {
        return new WeightedElement<>(left.element(), left.weight() + right.weight());
    }

    //TODO try a performance test with this replaced with combineSetWithRestrictions()
    private Optional<FieldSpec> mergeSets(WhitelistFieldSpec left, WhitelistFieldSpec right) {
        DistributedList<Object> set = new DistributedList<>(left.getWhitelist().distributedList().stream()
            .flatMap(leftHolder -> right.getWhitelist().distributedList().stream()
                .filter(rightHolder -> elementsEqual(leftHolder, rightHolder))
                .map(rightHolder -> mergeElements(leftHolder, rightHolder)))
            .distinct()
            .collect(Collectors.toList()));

        FieldSpec newFieldSpec = set.isEmpty() ? FieldSpecFactory.nullOnly() : FieldSpecFactory.fromList(set);
        return addNullable(left, right, newFieldSpec);
    }

    private static <T> boolean elementsEqual(WeightedElement<T> left, WeightedElement<T> right) {
        return left.element().equals(right.element());
    }

    private Optional<FieldSpec> combineSetWithRestrictions(WhitelistFieldSpec set, FieldSpec restrictions) {
        DistributedList<Object> newSet = new DistributedList<>(
            set.getWhitelist().distributedList().stream()
                .filter(holder -> restrictions.permits(holder.element()))
                .distinct()
                .collect(Collectors.toList()));

        FieldSpec newSpec = newSet.isEmpty() ? FieldSpecFactory.nullOnly() : FieldSpecFactory.fromList(newSet);
        return addNullable(set, restrictions, newSpec);
    }

    private Optional<FieldSpec> addNullable(FieldSpec left, FieldSpec right, FieldSpec newFieldSpec) {
        if (isNullable(left, right)) {
            return Optional.of(newFieldSpec);
        }

        if (nullOnly(newFieldSpec)) {
            return Optional.empty();
        }

        return Optional.of(newFieldSpec.withNotNull());
    }

    private boolean nullOnly(FieldSpec fieldSpec) {
        return (fieldSpec instanceof NullOnlyFieldSpec);
    }

    private boolean hasSet(FieldSpec fieldSpec) {
        return fieldSpec instanceof WhitelistFieldSpec;
    }

    private boolean isNullable(FieldSpec left, FieldSpec right) {
        return left.isNullable() && right.isNullable();
    }

    private Optional<FieldSpec> combineRestrictions(RestrictionsFieldSpec left, RestrictionsFieldSpec right) {
        Optional<TypedRestrictions> restrictions = restrictionMergeOperation.applyMergeOperation(left.getRestrictions(), right.getRestrictions());

        if (!restrictions.isPresent()){
            return nullOnlyOrEmpty(isNullable(left, right));
        }

        RestrictionsFieldSpec merged = FieldSpecFactory.fromRestriction(restrictions.get());
        merged = merged.withBlacklist(SetUtils.union(left.getBlacklist(), right.getBlacklist()));

        return addNullable(left, right, merged);
    }

    private Optional<FieldSpec> nullOnlyOrEmpty(boolean nullable) {
        return nullable ? Optional.of(FieldSpecFactory.nullOnly()) : Optional.empty();
    }
}

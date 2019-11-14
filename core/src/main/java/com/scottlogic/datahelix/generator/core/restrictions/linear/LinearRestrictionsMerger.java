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

package com.scottlogic.datahelix.generator.core.restrictions.linear;

import com.scottlogic.datahelix.generator.common.profile.Granularity;
import com.scottlogic.datahelix.generator.core.restrictions.RestrictionsMerger;
import com.scottlogic.datahelix.generator.core.restrictions.TypedRestrictions;

import java.util.Optional;

public class LinearRestrictionsMerger<T extends Comparable<T>> implements RestrictionsMerger {
    public Optional<LinearRestrictions> merge(TypedRestrictions left, TypedRestrictions right, boolean useFinestGranularityAvailable) {
        LinearRestrictions<T> leftCast = (LinearRestrictions<T>) left;
        LinearRestrictions<T> rightCast = (LinearRestrictions<T>) right;

        if(leftCast.isContradictory() || rightCast.isContradictory()) {
            return Optional.empty();
        }

        /*
         * If the restrictions are related, e.g. because field1 is before field2, then use the finest granularity possible
         * This is required to find the true minimum or maximum allowed in the restrictions.
         * Coarser granularities can be applied to the FieldSpec using the merged restriction if required.
         */
        Granularity<T> granularity = useFinestGranularityAvailable
            ? leftCast.getGranularity().getFinestGranularity()
            : leftCast.getGranularity().merge(rightCast.getGranularity());

        T mergedMin = getHighest(leftCast.getMin(), rightCast.getMin(), granularity);
        T mergedMax = getLowest(leftCast.getMax(), rightCast.getMax(), granularity);

        LinearRestrictions<T> mergedRestriction = new LinearRestrictions<>(mergedMin, mergedMax, granularity);

        if (mergedRestriction.isContradictory()) {
            return Optional.empty();
        }

        return Optional.of(mergedRestriction);
    }

    private T getHighest(T left, T right, Granularity<T> granularity) {
        return roundUpToNextValidValue(
            (left.compareTo(right) >= 0) ? left : right,
            granularity
        );
    }

    private T getLowest(T left, T right, Granularity<T> granularity) {
        return roundDownToNextValidValue(
            (left.compareTo(right) <= 0) ? left : right,
            granularity
        );
    }

    private T roundUpToNextValidValue(T limit, Granularity<T> granularity) {
        if (granularity.isCorrectScale(limit)) {
            return limit;
        } else {
            return granularity.getNext(granularity.trimToGranularity(limit));
        }
    }

    private T roundDownToNextValidValue(T limit, Granularity<T> granularity) {
        if (granularity.isCorrectScale(limit)) {
            return limit;
        } else {
            return granularity.trimToGranularity(limit);
        }
    }
}

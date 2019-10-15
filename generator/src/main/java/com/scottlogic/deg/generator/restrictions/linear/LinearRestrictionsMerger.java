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

package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.common.profile.constraintdetail.Granularity;
import com.scottlogic.deg.generator.restrictions.RestrictionsMerger;
import com.scottlogic.deg.generator.restrictions.TypedRestrictions;

import java.util.Optional;

public class LinearRestrictionsMerger<T extends Comparable<T>> implements RestrictionsMerger {

    @Override
    public Optional<LinearRestrictions> merge(TypedRestrictions left, TypedRestrictions right){
        LinearRestrictions<T> leftCast = (LinearRestrictions<T>) left;
        LinearRestrictions<T> rightCast = (LinearRestrictions<T>) right;

        Granularity<T> mergedGranularity = leftCast.getGranularity().merge(rightCast.getGranularity());

        T mergedMin = getHighest(leftCast.getMin(), rightCast.getMin(), mergedGranularity);
        T mergedMax = getLowest(leftCast.getMax(), rightCast.getMax(), mergedGranularity);

        LinearRestrictions<T> mergedRestriction = new LinearRestrictions<>(mergedMin, mergedMax, mergedGranularity);

        if (isContradictory(mergedRestriction)) {
            return Optional.empty();
        }

        return Optional.of(mergedRestriction);
    }

    private boolean isContradictory(LinearRestrictions<T> restictions) {
        return restictions.getMax().compareTo(restictions.getMin()) < 0;
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
        if (granularity.isCorrectScale(limit)){
            return limit;
        }
        else {
            return granularity.getNext(granularity.trimToGranularity(limit));
        }
    }

    private T roundDownToNextValidValue(T limit, Granularity<T> granularity) {
        if (granularity.isCorrectScale(limit)){
            return limit;
        }
        else {
            return granularity.trimToGranularity(limit);
        }
    }
}

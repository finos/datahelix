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

import com.scottlogic.deg.generator.restrictions.RestrictionsMerger;
import com.scottlogic.deg.generator.restrictions.TypedRestrictions;

import java.util.Optional;

public class LinearRestrictionsMerger<T extends Comparable<T>> implements RestrictionsMerger {

    @Override
    public Optional merge(TypedRestrictions left, TypedRestrictions right){
        LinearRestrictions<T> leftCast = (LinearRestrictions<T>) left;
        LinearRestrictions<T> rightCast = (LinearRestrictions<T>) right;

        Granularity<T> mergedGranularity = leftCast.getGranularity().merge(rightCast.getGranularity());

        Limit<T> mergedMin = getHighest(leftCast.getMin(), rightCast.getMin(), mergedGranularity);
        Limit<T> mergedMax = getLowest(leftCast.getMax(), rightCast.getMax(), mergedGranularity);

        LinearRestrictions<T> mergedRestriction = new LinearRestrictions<>(mergedMin, mergedMax, mergedGranularity, leftCast.getConverter());

        if (isContradictory(mergedRestriction)) {
            return Optional.empty();
        }

        return Optional.of(mergedRestriction);
    }

    private boolean isContradictory(LinearRestrictions<T> restictions) {
        T inclusiveMinValue = restictions.getMin().isInclusive()
            ? restictions.getMin().getValue()
            : restictions.getGranularity().getNext(restictions.getMin().getValue());

        return !restictions.getMax().isAfter(inclusiveMinValue);
    }

    private Limit<T> getHighest(Limit<T> left, Limit<T> right, Granularity<T> granularity) {
        if (left.getValue().equals(right.getValue())) {
            return getLeastInclusive(left, right);
        }

        return roundUpToNextValidValue(
            left.isAfter(right.getValue()) ? left : right,
            granularity
        );
    }

    private Limit<T> getLowest(Limit<T> left, Limit<T> right, Granularity<T> granularity) {
        if (left.getValue().equals(right.getValue())) {
            return getLeastInclusive(left, right);
        }
        return roundDownToNextValidValue(
            left.isBefore(right.getValue()) ? left : right,
            granularity
        );
    }

    private Limit<T> roundUpToNextValidValue(Limit<T> limit, Granularity<T> granularity) {
        if (granularity.isCorrectScale(limit.getValue())){
            return limit;
        }
        else {
            return new Limit<>(granularity.getNext(granularity.trimToGranularity(limit.getValue())), true);
        }
    }

    private Limit<T> roundDownToNextValidValue(Limit<T> limit, Granularity<T> granularity) {
        if (granularity.isCorrectScale(limit.getValue())){
            return limit;
        }
        else {
            return new Limit<>(granularity.trimToGranularity(limit.getValue()), true);
        }
    }

    private Limit<T> getLeastInclusive(Limit<T> left, Limit<T> right) {
        return left.isInclusive() ? right : left;
    }
}

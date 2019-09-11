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

import com.scottlogic.deg.common.profile.constraintdetail.Timescale;
import com.scottlogic.deg.generator.restrictions.MergeResult;

import java.time.OffsetDateTime;

public class DateTimeRestrictionsMerger {
    private enum MergeLimit {
        MIN, MAX
    }

    public MergeResult<DateTimeRestrictions> merge(DateTimeRestrictions left, DateTimeRestrictions right) {
        if (left == null && right == null)
            return new MergeResult<>(null);
        if (left == null)
            return new MergeResult<>(right);
        if (right == null)
            return new MergeResult<>(left);

        Timescale granularity = Timescale.getMostCoarse(left.getGranularity(), right.getGranularity());
        final DateTimeRestrictions merged = new DateTimeRestrictions(granularity);

        merged.min = granulate(MergeLimit.MIN, granularity, getMergedLimitStructure(MergeLimit.MIN, left.min, right.min, granularity));
        merged.max = granulate(MergeLimit.MAX, granularity, getMergedLimitStructure(MergeLimit.MAX, left.max, right.max, granularity));

        if (merged.min == null || merged.max == null) {
            return new MergeResult<>(merged);
        }

        if (merged.min.isAfter(merged.max.getValue())) {
            return MergeResult.unsuccessful();
        }

        return new MergeResult<>(merged);
    }

    private DateTimeLimit granulate(MergeLimit mergeLimit, Timescale granularity, DateTimeLimit limitHolder) {
        if (limitHolder == null) {
            return null;
        }

        OffsetDateTime limit = limitHolder.getValue();
        boolean inclusive = limitHolder.isInclusive();
        OffsetDateTime adjusted = granularity.getGranularityFunction().apply(limit);
        switch (mergeLimit) {
            case MIN:
                if (adjusted.equals(limit)) {
                    return new DateTimeLimit(adjusted, inclusive);
                }
                return new DateTimeLimit(granularity.getNext().apply(adjusted), inclusive);
            case MAX:
                return new DateTimeLimit(adjusted, inclusive);
            default:
                throw new UnsupportedOperationException("Unsupported MergeLimit. Only MergeLimit.MIN, and MergeLimit.MAX are supported");
        }
    }

    private DateTimeLimit getMergedLimitStructure(
        MergeLimit mergeLimit,
        DateTimeLimit left,
        DateTimeLimit right,
        Timescale granularity) {

        if (left == null && right == null) {
            return null;
        }
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        boolean leftIsInclusive = left.isInclusive();
        boolean rightIsInclusive = right.isInclusive();
        boolean inclusiveOverride = false;

        // if both exclusive
        if (!leftIsInclusive && !rightIsInclusive) {
            // if both datetime's are the same after granularity is applied
            if (granularity.getGranularityFunction().apply(left.getValue())
                .equals(granularity.getGranularityFunction().apply(right.getValue()))) {
                inclusiveOverride = true;
            }
        }

        // if left and right are identical, return new object with same values
        if (left.getValue().compareTo(right.getValue()) == 0)
            return new DateTimeLimit(left.getValue(), leftIsInclusive && rightIsInclusive);

        inclusiveOverride = inclusiveOverride || leftIsInclusive || rightIsInclusive;

        switch (mergeLimit) {
            case MIN:
                if (left.getValue().compareTo(right.getValue()) > 0) {
                    return new DateTimeLimit(left.getValue(), inclusiveOverride);
                }
                return new DateTimeLimit(right.getValue(), inclusiveOverride);
            case MAX:
                if (left.getValue().compareTo(right.getValue()) < 0) {
                    return new DateTimeLimit(left.getValue(), inclusiveOverride);
                }
                return new DateTimeLimit(right.getValue(), inclusiveOverride);
            default:
                throw new UnsupportedOperationException();
        }
    }
}

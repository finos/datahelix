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

package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.restrictions.linear.NumericLimit;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class NumericRestrictionsMerger {
    private enum MergeLimit {
        MIN, MAX
    }

    public MergeResult<NumericRestrictions> merge(NumericRestrictions left, NumericRestrictions right) {
        if (left == null && right == null)
            return new MergeResult<>(null);
        if (left == null)
            return new MergeResult<>(right);
        if (right == null)
            return new MergeResult<>(left);

        int granularity = Math.min(left.getNumericScale(), right.getNumericScale());

        NumericLimit min = getMergedLimitStructure(MergeLimit.MIN, left.getMin(), right.getMin());
        NumericLimit max = getMergedLimitStructure(MergeLimit.MAX, left.getMax(), right.getMax());

        final NumericRestrictions merged = new NumericRestrictions(min, max, granularity);

        if (!canEmitSomeNumericValues(merged)){
            return MergeResult.unsuccessful();
        }

        return new MergeResult<>(merged);
    }

    private boolean canEmitSomeNumericValues(NumericRestrictions merged) {
        NumericLimit min = merged.getMin();
        NumericLimit max = merged.getMax();

        if (min == null || max == null){
            return true; //no constraints
        }


        if (min.isInclusive() && max.isInclusive()){
            return isLessThanOrEqualTo(min, max);
        }

        if (!granularityIsWithinRange(merged)){
            return false;
        }

        return isLessThan(min, max);
    }

    private NumericLimit getMergedLimitStructure(MergeLimit mergeLimit, NumericLimit left, NumericLimit right) {
        if (left == null && right == null) {
            return null;
        }
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        if (left.getValue().compareTo(right.getValue()) == 0)
            return new NumericLimit(
                left.getValue(),
                left.isInclusive() && right.isInclusive());
        switch (mergeLimit) {
            case MIN:
                if (!isLessThan(left, right))
                    return left;
                return right;
            case MAX:
                if (isLessThan(left, right))
                    return left;
                return right;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private boolean isLessThan(NumericLimit min, NumericLimit max) {
        return min.getValue().compareTo(max.getValue()) < 0;
    }

    private boolean isLessThanOrEqualTo(NumericLimit min, NumericLimit max) {
        return min.getValue().compareTo(max.getValue()) <= 0;
    }

    private boolean granularityIsWithinRange(NumericRestrictions merged) {
        if (!merged.getMin().isInclusive()){
            NumericLimit nextNumber = new NumericLimit(
                merged.getMin().getValue().add(merged.getStepSize()), true);

            if (merged.getMax().isInclusive()){
                return isLessThanOrEqualTo(nextNumber, merged.getMax());
            }
            return isLessThan(nextNumber, merged.getMax());
        }


        if (!merged.getMax().isInclusive()){
            NumericLimit nextNumber = new NumericLimit(
                merged.getMax().getValue().subtract(merged.getStepSize()), true);

            if (merged.getMin().isInclusive()){
                return isLessThanOrEqualTo(merged.getMin(), nextNumber);
            }
            return isLessThan(merged.getMin(), nextNumber);
        }

        return false;
    }

}

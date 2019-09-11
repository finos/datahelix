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

import com.google.inject.Inject;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.restrictions.linear.*;

import java.math.BigDecimal;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.NUMERIC;

public class NumericRestrictionsMergeOperation implements RestrictionMergeOperation {
    private final LinearRestrictionsMerger merger;

    @Inject
    public NumericRestrictionsMergeOperation(LinearRestrictionsMerger merger) {
        this.merger = merger;
    }

    @Override
    public FieldSpec applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        if (!merging.isTypeAllowed(NUMERIC)){
            return merging;
        }
        NumericRestrictions leftRestriction = left.getNumericRestrictions();
        NumericRestrictions rightRestriction = right.getNumericRestrictions();

        MergeResult<LinearRestrictions<BigDecimal>> mergeResult = merger.merge(
            leftRestriction, rightRestriction);

        if (!mergeResult.successful) {
            return merging.withoutType(NUMERIC);
        }

        NumericRestrictions numericRestrictions;
        if (mergeResult.restrictions == null){
            numericRestrictions = null;
        }
        else if (mergeResult.restrictions == leftRestriction){
            numericRestrictions = leftRestriction;
        }
        else if (mergeResult.restrictions == rightRestriction){
            numericRestrictions = rightRestriction;
        }
        else {
            numericRestrictions = new NumericRestrictions(mergeResult.restrictions.getMin(), mergeResult.restrictions.getMax(), ((NumericGranularity)mergeResult.restrictions.getGranularity()).decimalPlaces);
        }

        return merging.withNumericRestrictions(numericRestrictions);
    }
}


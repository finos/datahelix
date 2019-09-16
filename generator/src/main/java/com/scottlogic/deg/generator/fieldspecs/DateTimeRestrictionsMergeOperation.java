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
import com.scottlogic.deg.generator.restrictions.linear.DateTimeGranularity;
import com.scottlogic.deg.generator.restrictions.linear.DateTimeRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsMerger;

import java.time.OffsetDateTime;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.DATETIME;

public class DateTimeRestrictionsMergeOperation implements RestrictionMergeOperation {
    private final LinearRestrictionsMerger merger;

    @Inject
    public DateTimeRestrictionsMergeOperation(LinearRestrictionsMerger merger) {
        this.merger = merger;
    }

    @Override
    public FieldSpec applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        if (!merging.isTypeAllowed(DATETIME)){
            return merging;
        }

        DateTimeRestrictions leftRestriction = left.getDateTimeRestrictions();
        DateTimeRestrictions rightRestriction = right.getDateTimeRestrictions();

        MergeResult<LinearRestrictions<OffsetDateTime>> mergeResult = merger.merge(
            leftRestriction, rightRestriction);

        if (!mergeResult.successful){
            return merging.withoutType(DATETIME);
        }

        DateTimeRestrictions dateTimeRestrictions;
        if (mergeResult.restrictions == null){
            dateTimeRestrictions = null;
        }
        else if (mergeResult.restrictions == leftRestriction){
            dateTimeRestrictions = leftRestriction;
        }
        else if (mergeResult.restrictions == rightRestriction){
            dateTimeRestrictions = rightRestriction;
        }
        else {
            dateTimeRestrictions = new DateTimeRestrictions(
                mergeResult.restrictions.getMin(),
                mergeResult.restrictions.getMax(),
                ((DateTimeGranularity)mergeResult.restrictions.getGranularity()).getTimeScale());
        }

        return merging.withDateTimeRestrictions(dateTimeRestrictions);
    }
}

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

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.DATETIME;
import static com.scottlogic.deg.generator.fieldspecs.FieldSpec.NullOnly;

public class DateTimeRestrictionsMergeOperation implements RestrictionMergeOperation {
    private final DateTimeRestrictionsMerger merger;

    @Inject
    public DateTimeRestrictionsMergeOperation(DateTimeRestrictionsMerger merger) {
        this.merger = merger;
    }

    @Override
    public FieldSpec applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        if (!merging.isType(DATETIME)){
            return merging;
        }

        MergeResult<DateTimeRestrictions> mergeResult = merger.merge(
            left.getDateTimeRestrictions(), right.getDateTimeRestrictions());

        if (!mergeResult.successful){
            return NullOnly;
        }

        return merging.withDateTimeRestrictions(mergeResult.restrictions);
    }
}

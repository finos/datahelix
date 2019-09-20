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

import com.scottlogic.deg.generator.restrictions.*;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.STRING;
import static com.scottlogic.deg.generator.fieldspecs.FieldSpec.NullOnly;

public class StringRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final StringRestrictionsMerger stringRestrictionsMerger = new StringRestrictionsMerger();

    @Override
    public FieldSpec applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        if (!merging.isType(STRING)){
            return merging;
        }

        MergeResult<StringRestrictions> mergeResult = stringRestrictionsMerger.merge(
            left.getStringRestrictions(), right.getStringRestrictions());

        if (!mergeResult.successful) {
            return NullOnly;
        }

        return merging.withStringRestrictions(mergeResult.restrictions);
    }
}


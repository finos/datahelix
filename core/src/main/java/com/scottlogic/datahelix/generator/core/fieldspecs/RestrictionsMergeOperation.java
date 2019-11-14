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

package com.scottlogic.datahelix.generator.core.fieldspecs;

import com.scottlogic.datahelix.generator.core.restrictions.*;
import com.scottlogic.datahelix.generator.core.restrictions.bool.BooleanRestrictions;
import com.scottlogic.datahelix.generator.core.restrictions.bool.BooleanRestrictionsMerger;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictions;
import com.scottlogic.datahelix.generator.core.restrictions.linear.LinearRestrictionsMerger;
import com.scottlogic.datahelix.generator.core.restrictions.string.StringRestrictions;
import com.scottlogic.datahelix.generator.core.restrictions.string.StringRestrictionsMerger;

import java.util.Optional;

public class RestrictionsMergeOperation {
    private final RestrictionsMerger linearMerger;
    private final RestrictionsMerger stringMerger;
    private final RestrictionsMerger booleanMerger;

    public RestrictionsMergeOperation(LinearRestrictionsMerger linearMerger, StringRestrictionsMerger stringMerger, BooleanRestrictionsMerger booleanMerger) {
        this.linearMerger = linearMerger;
        this.stringMerger = stringMerger;
        this.booleanMerger = booleanMerger;
    }

    public Optional<TypedRestrictions> applyMergeOperation(TypedRestrictions left, TypedRestrictions right, boolean useFinestGranularityAvailable) {
        return getMerger(left).merge(left, right, useFinestGranularityAvailable);
    }

    private RestrictionsMerger getMerger(TypedRestrictions restrictions) {
        if (restrictions instanceof StringRestrictions) {
            return stringMerger;
        }
        if (restrictions instanceof LinearRestrictions) {
            return linearMerger;
        }
        if (restrictions instanceof BooleanRestrictions) {
            return booleanMerger;
        }
        throw new IllegalStateException(restrictions.getClass() + " is an unsupported class");
    }
}

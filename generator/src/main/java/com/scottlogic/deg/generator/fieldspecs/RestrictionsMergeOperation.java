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
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsMerger;

import java.lang.reflect.Type;
import java.util.Optional;

public class RestrictionsMergeOperation {
    private final RestrictionsMerger linearMerger;
    private final RestrictionsMerger stringMerger;

    public RestrictionsMergeOperation(LinearRestrictionsMerger linearMerger, StringRestrictionsMerger stringMerger) {
        this.linearMerger = linearMerger;
        this.stringMerger = stringMerger;
    }

    public Optional<TypedRestrictions> applyMergeOperation(TypedRestrictions left, TypedRestrictions right) {
        return getMerger(left)
            .merge(left, right);
    }

    private RestrictionsMerger getMerger(TypedRestrictions restrictions) {
        if (restrictions instanceof StringRestrictions) {
            return stringMerger;
        }
        return linearMerger;
    }
}

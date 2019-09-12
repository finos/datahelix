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

import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.Set;

public class BlacklistRestictionsMergeOperation implements RestrictionMergeOperation {
    @Override
    public FieldSpec applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        Set<Object> newBlacklist;
        if (left.getBlacklist() == null) {
            newBlacklist = right.getBlacklist();
        }
        else if (right.getBlacklist() == null) {
            newBlacklist = left.getBlacklist();
        }
        else {
            newBlacklist = SetUtils.union(
                left.getBlacklist(),
                right.getBlacklist());
        }

        return merging.withBlacklist(newBlacklist);
    }
}

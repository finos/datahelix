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

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.Set;

import static com.scottlogic.deg.generator.restrictions.TypeRestrictions.ALL_TYPES_PERMITTED;

public class TypeRestrictionsMerger {
    public MergeResult<TypeRestrictions> merge(TypeRestrictions left, TypeRestrictions right) {
        if (allowsAll(left) && allowsAll(right))
            return new MergeResult<>(ALL_TYPES_PERMITTED);
        if (allowsAll(left))
            return new MergeResult<>(right);
        if (allowsAll(right))
            return new MergeResult<>(left);

        Set<IsOfTypeConstraint.Types> intersection = SetUtils.intersect(left.getAllowedTypes(), right.getAllowedTypes());

        if (intersection.isEmpty()) {
            return MergeResult.unsuccessful();
        }

        return new MergeResult<>(new TypeRestrictions(intersection));
    }

    private boolean allowsAll(TypeRestrictions restriction) {
        return restriction == null || restriction == ALL_TYPES_PERMITTED;
    }
}

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

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.Set;

import static com.scottlogic.deg.generator.fieldspecs.FieldSpec.NullOnly;

public class TypesRestrictionMergeOperation implements RestrictionMergeOperation {

    @Override
    public FieldSpec applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        if (left.getTypeRestrictions() == right.getTypeRestrictions()){
            return merging.withTypeRestrictions(left.getTypeRestrictions());
        }

        Set<IsOfTypeConstraint.Types> intersect = SetUtils.intersect(left.getTypeRestrictions(), right.getTypeRestrictions());

        if (intersect.isEmpty()) {
            return NullOnly;
        }

        return merging.withTypeRestrictions(intersect);
    }
}


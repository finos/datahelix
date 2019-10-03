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
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.relations.FieldSpecRelations;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.*;

public class RowSpecMerger {
    private final FieldSpecMerger fieldSpecMerger;

    @Inject
    public RowSpecMerger(FieldSpecMerger fieldSpecMerger) {
        this.fieldSpecMerger = fieldSpecMerger;
    }

    /**
     * @param left must have all the fields represented
     */
    public Optional<RowSpec> merge(RowSpec left, RowSpec right) {

        Map<Field, FieldSpec> newMap = new HashMap<>();

        for (Field field : left.getFields()) {
            Optional<FieldSpec> merge = fieldSpecMerger.merge(left.getSpecForField(field), right.getSpecForField(field));

            if (!merge.isPresent()){
                return Optional.empty();
            }
            newMap.put(field, merge.get());
        }

        List<FieldSpecRelations> relations = new ArrayList<>(
            SetUtils.union(left.getRelations(), right.getRelations()));

        return Optional.of(new RowSpec(left.getFields(), newMap, relations));
    }
}

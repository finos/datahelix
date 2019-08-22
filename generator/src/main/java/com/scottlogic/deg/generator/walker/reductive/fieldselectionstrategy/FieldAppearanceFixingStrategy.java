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

package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FieldAppearanceFixingStrategy implements FixFieldStrategy {
    final List<Field> fieldsInFixingOrder;

    public FieldAppearanceFixingStrategy(ConstraintNode rootNode) {
        FieldAppearanceAnalyser fieldAppearanceAnalyser = new FieldAppearanceAnalyser();
        rootNode.accept(fieldAppearanceAnalyser);

        fieldsInFixingOrder = fieldAppearanceAnalyser.fieldAppearances.entrySet().stream()
            .sorted(highestToLowest())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
    @Override
    public Field getNextFieldToFix(ReductiveState reductiveState) {
        return fieldsInFixingOrder.stream()
            .filter(field -> !reductiveState.isFieldFixed(field) && reductiveState.getFields().stream().anyMatch(pf -> pf.equals(field)))
            .findFirst()
            .orElse(null);
    }

    private Comparator<Map.Entry<Field, Integer>> highestToLowest() {
        return Collections.reverseOrder(this::compare);
    }

    private int compare(Map.Entry<Field, Integer> a, Map.Entry<Field, Integer> b) {
        if (a.getValue() != b.getValue()) {
            return a.getValue() - b.getValue();
        }
        return b.getKey().name.compareTo(a.getKey().name);
    }
}

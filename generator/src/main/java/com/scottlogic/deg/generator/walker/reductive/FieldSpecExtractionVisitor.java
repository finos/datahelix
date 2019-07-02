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

package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraintsHelper;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.visualisation.BaseVisitor;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class FieldSpecExtractionVisitor extends BaseVisitor {

    public final HashSet<FieldSpec> fieldSpecs = new HashSet<>();
    private final Field field;
    private final ConstraintReducer constraintReducer;

    FieldSpecExtractionVisitor(Field field, ConstraintReducer constraintReducer) {
        this.field = field;
        this.constraintReducer = constraintReducer;
    }

    @Override
    public ConstraintNode visit(ConstraintNode constraintNode) {
        List<AtomicConstraint> atomicConstraintsForField =
            AtomicConstraintsHelper.getConstraintsForField(constraintNode.getAtomicConstraints(), field);

        Optional<FieldSpec> fieldSpec =
            atomicConstraintsForField.isEmpty()
                ? Optional.empty()
                : constraintReducer.reduceConstraintsToFieldSpec(atomicConstraintsForField);

        if (fieldSpec.isPresent()) {
            fieldSpecs.add(fieldSpec.get());
        }

        return constraintNode;
    }
}

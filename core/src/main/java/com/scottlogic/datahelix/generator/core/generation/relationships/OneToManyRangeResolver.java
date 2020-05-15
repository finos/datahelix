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

package com.scottlogic.datahelix.generator.core.generation.relationships;

import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.common.output.GeneratedObject;
import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.core.decisiontree.ConstraintNode;
import com.scottlogic.datahelix.generator.core.decisiontree.DecisionTree;
import com.scottlogic.datahelix.generator.core.decisiontree.DecisionTreeFactory;
import com.scottlogic.datahelix.generator.core.generation.databags.DataBagValue;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.EqualToConstraint;
import com.scottlogic.datahelix.generator.core.walker.pruner.Merged;
import com.scottlogic.datahelix.generator.core.walker.pruner.TreePruner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class OneToManyRangeResolver {
    private final DecisionTreeFactory factory;
    private final TreePruner treePruner;

    @Inject
    public OneToManyRangeResolver(
        DecisionTreeFactory factory,
        TreePruner treePruner) {
        this.factory = factory;
        this.treePruner = treePruner;
    }

    public OneToManyRange getRange(Fields profileFields, Collection<Constraint> constraints, GeneratedObject generatedObject) {
        OneToManyRange range = new OneToManyRange(0, null);

        ExtentAugmentedFields extentAugmentedFields = new ExtentAugmentedFields(profileFields);
        DecisionTree tree = factory.analyse(new Profile(
            extentAugmentedFields,
            new ArrayList<>(constraints),
            Collections.emptyList())
        );

        //apply each value of generatedObject to the tree
        ConstraintNode rootNode = applyGeneratedData(profileFields, tree.getRootNode(), generatedObject);

        if (rootNode == null) {
            throw new RuntimeException("There are conditions that result in an unresolved extent");
        }

        if (!rootNode.getDecisions().isEmpty()) {
            throw new RuntimeException("There are conditional constraints that haven't been resolved");
        }

        //read the root-level properties for <min> and <max> if there are any decisions
        range = rootNode.getAtomicConstraints()
            .stream()
            .filter(ac -> extentAugmentedFields.isExtentField(ac.getField()))
            .reduce(
                range,
                (r, atomicConstraint) -> applyAtomicConstraint(r, atomicConstraint, extentAugmentedFields),
                (a, b) -> null);

        return range;
    }

    private static OneToManyRange applyAtomicConstraint(OneToManyRange range, AtomicConstraint atomicConstraint, ExtentAugmentedFields extentAugmentedFields) {
        Field field = atomicConstraint.getField();

        return extentAugmentedFields.applyExtent(range, field, getExtent(atomicConstraint));
    }

    private static int getExtent(AtomicConstraint atomicConstraint) {
        if (atomicConstraint instanceof EqualToConstraint) {
            return getIntegerValue(((EqualToConstraint) atomicConstraint).value);
        }

        throw new RuntimeException("Unsure how to extract an extent from a " + atomicConstraint.getClass().getName());
    }

    private static int getIntegerValue(Object value) {
        if (value instanceof Integer){
            return (int) value;
        }

        if (value instanceof BigDecimal){
            return ((BigDecimal) value).intValue();
        }

        throw new RuntimeException("Unable to extract an integer value from a " + value.getClass().getName());
    }

    private ConstraintNode applyGeneratedData(Fields profileFields, ConstraintNode rootNode, GeneratedObject generatedObject) {
        return profileFields.stream().reduce(
            rootNode,
            (nextRootNode, field) -> {
                if (nextRootNode == null) {
                    return null;
                }

                Object value = generatedObject.getValue(field);
                Merged<ConstraintNode> constraintNodeMerged = treePruner.pruneConstraintNode(nextRootNode, field, new DataBagValue(value));
                if (constraintNodeMerged.isContradictory()){
                    return null;
                }

                return constraintNodeMerged.get();
            },
            (a, b) -> null
        );
    }
}

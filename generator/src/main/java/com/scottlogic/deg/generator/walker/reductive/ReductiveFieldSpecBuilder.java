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

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraintsHelper;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;

import java.util.*;
import java.util.stream.Collectors;

public class ReductiveFieldSpecBuilder {

    private final ConstraintReducer constraintReducer;
    private final FieldSpecMerger fieldSpecMerger;

    @Inject
    public ReductiveFieldSpecBuilder(ConstraintReducer constraintReducer, FieldSpecMerger fieldSpecMerger) {
        this.constraintReducer = constraintReducer;
        this.fieldSpecMerger = fieldSpecMerger;
    }

    /**
     * creates a FieldSpec for a field for the current state of the tree
     * FieldSpec to be used for generating values
     * @param rootNode of the tree to create the fieldSpec for
     * @param field to create the fieldSpec for
     * @return fieldSpec with mustContains restriction if not contradictory, otherwise Optional.empty()
     */
    public Set<FieldSpec> getDecisionFieldSpecs(ConstraintNode rootNode, Field field){
        List<AtomicConstraint> constraintsForRootNode =
            AtomicConstraintsHelper.getConstraintsForField(rootNode.getAtomicConstraints(), field);

        Optional<FieldSpec> rootOptional = constraintReducer.reduceConstraintsToFieldSpec(constraintsForRootNode);
        if (!rootOptional.isPresent()){
            return Collections.emptySet();
        }
        FieldSpec rootFieldSpec = rootOptional.get();
        if (hasSetOrIsNull(rootFieldSpec)){
            return Collections.singleton(rootFieldSpec);
        }

        Set<FieldSpec> fieldSpecsForDecisions = getFieldSpecsForDecisions(field, rootNode);

        if (fieldSpecsForDecisions.isEmpty()) { return Collections.singleton(rootFieldSpec); }

        return mergeDecisionFieldSpecsWithRoot(
            rootFieldSpec,
            fieldSpecsForDecisions);
    }

    private boolean hasSetOrIsNull(FieldSpec fieldSpec) {
        return  (fieldSpec.getWhitelist() != null);
    }

    private Set<FieldSpec> getFieldSpecsForDecisions(Field field, ConstraintNode rootNode) {
        FieldSpecExtractionVisitor visitor = new FieldSpecExtractionVisitor(field, constraintReducer);

        //ignore the root node, pass the visitor into any option of a decision below the root node.
        rootNode.getDecisions()
            .forEach(d -> d.getOptions()
                .forEach(o -> o.accept(visitor)));

        return visitor.fieldSpecs;
    }

    private Set<FieldSpec> mergeDecisionFieldSpecsWithRoot(FieldSpec rootFieldSpec, Set<FieldSpec> decisionFieldSpecs) {
        return decisionFieldSpecs.stream()
            .map(decisionSpec -> fieldSpecMerger.merge(rootFieldSpec, decisionSpec))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
    }

}

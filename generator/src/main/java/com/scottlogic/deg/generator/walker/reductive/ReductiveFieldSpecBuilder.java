package com.scottlogic.deg.generator.walker.reductive;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraintsHelper;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;

import java.util.*;

public class ReductiveFieldSpecBuilder {

    private final ConstraintReducer constraintReducer;

    @Inject
    public ReductiveFieldSpecBuilder(ConstraintReducer constraintReducer) {
        this.constraintReducer = constraintReducer;
    }

    /**
     * creates a FieldSpec for a field for the current state of the tree
     * FieldSpec to be used for generating values
     * @param rootNode of the tree to create the fieldSpec for
     * @param field to create the fieldSpec for
     * @return fieldSpec with mustContains restriction if not contradictory, otherwise Optional.empty()
     */
    public Optional<FieldSpec> getFieldSpecWithMustContains(ConstraintNode rootNode, Field field){
        List<AtomicConstraint> constraintsForRootNode =
            AtomicConstraintsHelper.getConstraintsForField(rootNode.getAtomicConstraints(), field);

        Set<FieldSpec> fieldSpecsForDecisions = getFieldSpecsForDecisions(field, rootNode);

        return constraintReducer.reduceConstraintsToFieldSpecWithMustContains(
            constraintsForRootNode,
            fieldSpecsForDecisions);
    }

    private Set<FieldSpec> getFieldSpecsForDecisions(Field field, ConstraintNode rootNode) {
        FieldSpecExtractionVisitor visitor = new FieldSpecExtractionVisitor(field, constraintReducer);

        //ignore the root node, pass the visitor into any option of a decision below the root node.
        rootNode.getDecisions()
            .forEach(d -> d.getOptions()
                .forEach(o -> o.accept(visitor)));

        return visitor.fieldSpecs;
    }
}

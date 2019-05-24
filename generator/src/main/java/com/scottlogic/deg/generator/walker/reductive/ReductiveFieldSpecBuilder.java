package com.scottlogic.deg.generator.walker.reductive;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraintsHelper;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.MustContainRestriction;

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
    public Optional<FieldSpec> getFieldSpecWithMustContains(ConstraintNode rootNode, Field field){
        List<AtomicConstraint> constraintsForRootNode =
            AtomicConstraintsHelper.getConstraintsForField(rootNode.getAtomicConstraints(), field);

        Set<FieldSpec> fieldSpecsForDecisions = getFieldSpecsForDecisions(field, rootNode);

        Optional<FieldSpec> rootFieldSpec = constraintReducer.reduceConstraintsToFieldSpec(constraintsForRootNode);
        if (!rootFieldSpec.isPresent()){
            return Optional.empty();
        }

        return reduceConstraintsToFieldSpecWithMustContains(
            rootFieldSpec.get(),
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

    public Optional<FieldSpec> reduceConstraintsToFieldSpecWithMustContains(FieldSpec rootFieldSpec,
                                                                            Set<FieldSpec> decisionFieldSpecs) {
        if (decisionFieldSpecs.isEmpty()) { return Optional.of(rootFieldSpec); }

        return Optional.of(rootFieldSpec.withMustContainRestriction(
            new MustContainRestriction(
                decisionFieldSpecs.stream()
                    .map(decisionSpec -> fieldSpecMerger.merge(rootFieldSpec, decisionSpec))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet())
            )
        ));
    }

}

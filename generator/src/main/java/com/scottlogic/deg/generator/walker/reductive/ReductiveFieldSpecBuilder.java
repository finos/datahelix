package com.scottlogic.deg.generator.walker.reductive;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraintsHelper;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategy;

import java.util.*;
import java.util.stream.Stream;

public class ReductiveFieldSpecBuilder {

    private final ConstraintReducer constraintReducer;
    private final ReductiveDataGeneratorMonitor monitor;
    private final FieldSpecValueGenerator generator;

    @Inject
    public ReductiveFieldSpecBuilder(
        ConstraintReducer constraintReducer,
        ReductiveDataGeneratorMonitor monitor,
        FieldSpecValueGenerator generator) {
        this.constraintReducer = constraintReducer;
        this.monitor = monitor;
        this.generator = generator;
    }


    public Optional<FieldSpec> getFieldSpecWithMustContains(ConstraintNode rootNode, Field field){
        List<AtomicConstraint> constraintsForRootNode =
            AtomicConstraintsHelper.getConstraintsForField(rootNode.getAtomicConstraints(), field);

        Set<FieldSpec> fieldSpecsForDecisions = getFieldSpecsForDecisions(field, rootNode);

        return this.constraintReducer.reduceConstraintsToFieldSpecWithMustContains(
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

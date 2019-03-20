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
import com.sun.corba.se.spi.orbutil.fsm.FSM;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FixedFieldBuilder {

    private final ConstraintReducer constraintReducer;
    private final ReductiveDataGeneratorMonitor monitor;
    private final FieldSpecValueGenerator generator;

    @Inject
    public FixedFieldBuilder(
        ConstraintReducer constraintReducer,
        ReductiveDataGeneratorMonitor monitor,
        FieldSpecValueGenerator generator) {
        this.constraintReducer = constraintReducer;
        this.monitor = monitor;
        this.generator = generator;
    }

    //work out the next field to fix and return a new ReductiveState with this field fixed
    public FixedField findNextFixedField(ReductiveState reductiveState, ConstraintNode rootNode, FixFieldStrategy fixFieldStrategy) {
        Field fieldToFix = fixFieldStrategy.getNextFieldToFix(reductiveState, rootNode);

        return createFixedFieldWithValues(fieldToFix, rootNode);
    }

    private Optional<FieldSpec> getFieldSpecWithMustContains(ConstraintNode rootNode, Field field){
        List<AtomicConstraint> constraintsForRootNode =
            AtomicConstraintsHelper.getConstraintsForField(rootNode.getAtomicConstraints(), field);

        Set<FieldSpec> fieldSpecsForDecisions = getFieldSpecsForDecisions(field, rootNode);

        return this.constraintReducer.reduceConstraintsToFieldSpecWithMustContains(
            constraintsForRootNode,
            fieldSpecsForDecisions);
    }

    //for the given field get a stream of possible values
    private FixedField createFixedFieldWithValues(Field field, ConstraintNode rootNode) {
        Optional<FieldSpec> rootConstraintsFieldSpec = getFieldSpecWithMustContains(rootNode, field);

        if (!rootConstraintsFieldSpec.isPresent()) {
            //contradiction in the root node
            return null;
        }

        //use the FieldSpecValueGenerator to emit all possible values given the generation mode, interesting or full-sequential
        Stream<Object> values = generator.generate(field, rootConstraintsFieldSpec.orElse(FieldSpec.Empty))
            .map(dataBag -> dataBag.getValue(field));

        return new FixedField(field, values, this.monitor);
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

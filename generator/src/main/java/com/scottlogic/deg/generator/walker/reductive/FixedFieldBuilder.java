package com.scottlogic.deg.generator.walker.reductive;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.NotConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.decisiontree.visualisation.BaseVisitor;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.fieldspecs.*;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FixedFieldBuilder {

    private final GenerationConfig generationConfig;
    private final ConstraintReducer constraintReducer;
    private final FixFieldStrategy fixFieldStrategy;
    private final ReductiveDataGeneratorMonitor monitor;
    private final FieldSpecValueGenerator generator;

    @Inject
    public FixedFieldBuilder(
        GenerationConfig config,
        ConstraintReducer constraintReducer,
        FixFieldStrategy fixFieldStrategy,
        ReductiveDataGeneratorMonitor monitor,
        FieldSpecValueGenerator generator) {
        this.fixFieldStrategy = fixFieldStrategy;
        this.generationConfig = config;
        this.constraintReducer = constraintReducer;
        this.monitor = monitor;
        this.generator = generator;
    }

    //work out the next field to fix and return a new ReductiveState with this field fixed
    public FixedField findNextFixedField(ReductiveState reductiveState, ReductiveConstraintNode rootNode) {
        Field fieldToFix = fixFieldStrategy.getNextFieldToFix(reductiveState, rootNode);

        if (fieldToFix == null){
            throw new UnsupportedOperationException(
                String.format(
                    "Unable to find a field to fix, no finite constraints\nUnfixed fields: %s",
                    Objects.toString(reductiveState.getUnfixedFields())));
        }

        return createFixedFieldWithValues(fieldToFix, rootNode);
    }

    //for the given field get a stream of possible values
    private FixedField createFixedFieldWithValues(Field field, ConstraintNode rootNode) {
        //from the original tree, get all atomic constraints that match the given field
        Set<AtomicConstraint> constraintsForRootNode = rootNode.getAtomicConstraints()
            .stream()
            .filter(c -> c.getField().equals(field))
            .collect(Collectors.toSet());

        Set<AtomicConstraint> constraintsForDecisions = getAtomicConstraintsInDecisions(field, rootNode);

        //produce a fieldspec for all the atomic constraints
        FieldSpec rootConstraintsFieldSpec = this.constraintReducer.reduceConstraintsToFieldSpec(
            constraintsForRootNode,
            constraintsForDecisions)
            .orElse(FieldSpec.Empty);

        //use the FieldSpecValueGenerator to emit all possible values given the generation mode, interesting or full-sequential
        Stream<Object> values = generator.generate(field, rootConstraintsFieldSpec)
            .map(dataBag -> dataBag.getValue(field));

        return new FixedField(field, values, rootConstraintsFieldSpec, this.monitor);
    }

    private Set<AtomicConstraint> getAtomicConstraintsInDecisions(Field field, ConstraintNode rootNode) {
        DecisionAtomicConstraintExtractionVisitor visitor = new DecisionAtomicConstraintExtractionVisitor(field);

        //ignore the root node, pass the visitor into any option of a decision below the root node.
        rootNode.getDecisions()
            .forEach(d -> d.getOptions()
                .forEach(o -> o.accept(visitor)));

        return visitor.atomicConstraints;
    }

    class DecisionAtomicConstraintExtractionVisitor extends BaseVisitor {
        public final HashSet<AtomicConstraint> atomicConstraints = new HashSet<>();
        private final Field field;

        DecisionAtomicConstraintExtractionVisitor(Field field) {
            this.field = field;
        }

        @Override
        public AtomicConstraint visit(AtomicConstraint atomicConstraint) {
            if (!(atomicConstraint instanceof NotConstraint) && atomicConstraint.getField().equals(this.field)){
                this.atomicConstraints.add(atomicConstraint);
            }

            return atomicConstraint;
        }
    }
}

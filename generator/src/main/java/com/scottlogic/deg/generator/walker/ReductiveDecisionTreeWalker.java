package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.walker.reductive.*;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FieldValue;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategy;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class ReductiveDecisionTreeWalker implements DecisionTreeWalker {
    private final ReductiveTreePruner treePruner;
    private final IterationVisualiser iterationVisualiser;
    private final ReductiveFieldSpecBuilder reductiveFieldSpecBuilder;
    private final ReductiveDataGeneratorMonitor monitor;
    private final ReductiveRowSpecGenerator reductiveRowSpecGenerator;
    private final FieldSpecValueGenerator fieldSpecValueGenerator;

    @Inject
    public ReductiveDecisionTreeWalker(
        IterationVisualiser iterationVisualiser,
        ReductiveFieldSpecBuilder reductiveFieldSpecBuilder,
        ReductiveDataGeneratorMonitor monitor,
        ReductiveTreePruner treePruner,
        ReductiveRowSpecGenerator reductiveRowSpecGenerator,
        FieldSpecValueGenerator fieldSpecValueGenerator) {
        this.iterationVisualiser = iterationVisualiser;
        this.reductiveFieldSpecBuilder = reductiveFieldSpecBuilder;
        this.monitor = monitor;
        this.treePruner = treePruner;
        this.reductiveRowSpecGenerator = reductiveRowSpecGenerator;
        this.fieldSpecValueGenerator = fieldSpecValueGenerator;
    }

    /* initialise the walker with a set (ReductiveState) of unfixed fields */
    public Stream<RowSpec> walk(DecisionTree tree, FixFieldStrategy fixFieldStrategy) {
        ReductiveState initialState = new ReductiveState(tree.fields);
        visualise(tree.getRootNode(), initialState);
        return fixNextField(tree.getRootNode(), initialState, fixFieldStrategy);
    }

    private Stream<RowSpec> fixNextField(ConstraintNode tree, ReductiveState reductiveState, FixFieldStrategy fixFieldStrategy) {

        Field fieldToFix = fixFieldStrategy.getNextFieldToFix(reductiveState, tree);
        Optional<FieldSpec> nextFieldSpec = reductiveFieldSpecBuilder.getFieldSpecWithMustContains(tree, fieldToFix);

        if (!nextFieldSpec.isPresent()){
            //couldn't fix a field, maybe there are contradictions in the root node?
            monitor.noValuesForField(reductiveState, fieldToFix);
            return Stream.empty();
        }

        Stream<FieldValue> values = fieldSpecValueGenerator.generate(fieldToFix, nextFieldSpec.get())
            .map(dataBag -> new FieldValue(fieldToFix, dataBag.getValue(fieldToFix), nextFieldSpec.get().getFormatRestrictions()));

        return FlatMappingSpliterator.flatMap(
            values,
            fieldValue -> pruneTreeForNextValue(tree, reductiveState, fixFieldStrategy, fieldValue));
    }

    private Stream<RowSpec> pruneTreeForNextValue(
        ConstraintNode tree,
        ReductiveState reductiveState,
        FixFieldStrategy fixFieldStrategy,
        FieldValue fieldValue){

        Merged<ConstraintNode> reducedTree = this.treePruner.pruneConstraintNode(tree, fieldValue);

        if (reducedTree.isContradictory()){
            //yielding an empty stream will cause back-tracking
            this.monitor.unableToStepFurther(reductiveState);
            return Stream.empty();
        }

        monitor.fieldFixedToValue(fieldValue.getField(), fieldValue.getValue());
        visualise(reducedTree.get(), reductiveState);

        ReductiveState newReductiveState =
            reductiveState.withFixedFieldValue(fieldValue);

        if (newReductiveState.allFieldsAreFixed()){
            return Stream.of(reductiveRowSpecGenerator.createRowSpecsFromFixedValues(newReductiveState));
        }

        return fixNextField(reducedTree.get(), newReductiveState, fixFieldStrategy);
    }

    private void visualise(ConstraintNode rootNode, ReductiveState reductiveState){
        try {
            iterationVisualiser.visualise(rootNode, reductiveState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

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
        return walkForNextField(tree.getRootNode(), initialState, fixFieldStrategy);
    }

    private Stream<RowSpec> walkForNextField(ConstraintNode constraintNode, ReductiveState reductiveState, FixFieldStrategy fixFieldStrategy) {

        Field fieldToFix = fixFieldStrategy.getNextFieldToFix(reductiveState, constraintNode);
        Optional<FieldSpec> nextFieldSpec = reductiveFieldSpecBuilder.getFieldSpecWithMustContains(constraintNode, fieldToFix);

        if (!nextFieldSpec.isPresent()){
            //couldn't fix a field, maybe there are contradictions in the root node?
            monitor.noValuesForField(reductiveState, fieldToFix);
            return Stream.empty();
        }

        Stream<FieldValue> values = fieldSpecValueGenerator.generate(fieldToFix, nextFieldSpec.get())
            .map(dataBag -> new FieldValue(fieldToFix, dataBag.getValue(fieldToFix)));

        return FlatMappingSpliterator.flatMap(
            values,
            fieldValue -> walkForNextValue(constraintNode, reductiveState, fixFieldStrategy, fieldValue));
    }

    private Stream<RowSpec> walkForNextValue(
        ConstraintNode constraintNode,
        ReductiveState reductiveState,
        FixFieldStrategy fixFieldStrategy,
        FieldValue value){

        //reduce the tree based on the fields that are now fixed
        Merged<ConstraintNode> reducedNode = this.treePruner.pruneConstraintNode(constraintNode, value);

        if (reducedNode.isContradictory()){
            //yielding an empty stream will cause back-tracking
            this.monitor.unableToStepFurther(reductiveState);
            return Stream.empty();
        }

        monitor.fieldFixedToValue(value.getField(), value.getValue());
        visualise(reducedNode.get(), reductiveState);

        ReductiveState newReductiveState =
            reductiveState.withCurrentFieldFixedToValue(value);

        if (newReductiveState.allFieldsAreFixed()){
            return reductiveRowSpecGenerator.createRowSpecsFromFixedValues(newReductiveState);
        }

        return walkForNextField(reducedNode.get(), newReductiveState, fixFieldStrategy);
    }

    private void visualise(ConstraintNode rootNode, ReductiveState reductiveState){
        try {
            iterationVisualiser.visualise(rootNode, reductiveState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

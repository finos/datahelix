package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.walker.reductive.*;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategy;

import java.io.IOException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ReductiveDecisionTreeWalker implements DecisionTreeWalker {
    private final ReductiveTreePruner treePruner;
    private final IterationVisualiser iterationVisualiser;
    private final FixedFieldBuilder fixedFieldBuilder;
    private final ReductiveDataGeneratorMonitor monitor;
    private final ReductiveRowSpecGenerator reductiveRowSpecGenerator;

    @Inject
    public ReductiveDecisionTreeWalker(
        IterationVisualiser iterationVisualiser,
        FixedFieldBuilder fixedFieldBuilder,
        ReductiveDataGeneratorMonitor monitor,
        ReductiveTreePruner treePruner,
        ReductiveRowSpecGenerator reductiveRowSpecGenerator) {
        this.iterationVisualiser = iterationVisualiser;
        this.fixedFieldBuilder = fixedFieldBuilder;
        this.monitor = monitor;
        this.treePruner = treePruner;
        this.reductiveRowSpecGenerator = reductiveRowSpecGenerator;
    }

    /* initialise the walker with a set (ReductiveState) of unfixed fields */
    public Stream<RowSpec> walk(DecisionTree tree, FixFieldStrategy fixFieldStrategy) {
        ConstraintNode rootNode = tree.getRootNode();
        ReductiveState initialState = new ReductiveState(tree.fields);

        visualise(rootNode, initialState);

        FixedField nextFixedField = fixedFieldBuilder.findNextFixedField(initialState, rootNode, fixFieldStrategy);

        if (nextFixedField == null){
            //couldn't fix a field, maybe there are contradictions in the root node?

            return Stream.empty();
        }

        return walkForNextField(rootNode, initialState.withNextFieldToFixChosen(nextFixedField), fixFieldStrategy);
    }

    private Stream<RowSpec> walkForNextField(ConstraintNode constraintNode, ReductiveState reductiveState, FixFieldStrategy fixFieldStrategy) {

        Iterator<Object> valueIterator = reductiveState.getValuesFromNextFieldToFix().iterator();

        if (!valueIterator.hasNext()){
            this.monitor.noValuesForField(reductiveState);
            return Stream.empty();
        }

        // for each value for the last fixed field, fix the value and process the tree based on this field being fixed
        return FlatMappingSpliterator.flatMap(
            StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(valueIterator, Spliterator.ORDERED),
                false),
            fieldValue -> walkForNextValue(constraintNode, reductiveState, fixFieldStrategy, fieldValue));
    }

    private Stream<RowSpec> walkForNextValue(
        ConstraintNode constraintNode,
        ReductiveState reductiveState,
        FixFieldStrategy fixFieldStrategy,
        Object value){

        //reduce the tree based on the fields that are now fixed
        Merged<ConstraintNode> reducedNode = this.treePruner.pruneConstraintNode(constraintNode, reductiveState.getNextFieldToFix().getField(), value);

        if (reducedNode.isContradictory()){
            //yielding an empty stream will cause back-tracking
            this.monitor.unableToStepFurther(reductiveState);
            return Stream.empty();
        }

        visualise(reducedNode.get(), reductiveState);

        ReductiveState newReductiveState =
            reductiveState.withCurrentFieldFixedToValue(value);

        if (newReductiveState.allFieldsAreFixed()){
            return reductiveRowSpecGenerator.createRowSpecsFromFixedValues(newReductiveState);
        }

        //find the next fixed field and continue
        FixedField nextFixedField = fixedFieldBuilder.findNextFixedField(newReductiveState, reducedNode.get(), fixFieldStrategy);

        if (nextFixedField == null){
            return Stream.empty();
        }

        return walkForNextField(reducedNode.get(), newReductiveState.withNextFieldToFixChosen(nextFixedField), fixFieldStrategy);
    }

    private void visualise(ConstraintNode rootNode, ReductiveState reductiveState){
        try {
            iterationVisualiser.visualise(rootNode, reductiveState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

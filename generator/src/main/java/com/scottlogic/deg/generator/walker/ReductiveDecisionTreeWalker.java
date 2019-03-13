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
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ReductiveDecisionTreeWalker implements DecisionTreeWalker {
    private final ReductiveTreePruner treeReducer;
    private final IterationVisualiser iterationVisualiser;
    private final FixedFieldBuilder fixedFieldBuilder;
    private final ReductiveDataGeneratorMonitor monitor;
    private final ReductiveRowSpecGenerator reductiveRowSpecGenerator;

    @Inject
    public ReductiveDecisionTreeWalker(
        IterationVisualiser iterationVisualiser,
        FixedFieldBuilder fixedFieldBuilder,
        ReductiveDataGeneratorMonitor monitor,
        ReductiveTreePruner treeReducer,
        ReductiveRowSpecGenerator reductiveRowSpecGenerator) {
        this.iterationVisualiser = iterationVisualiser;
        this.fixedFieldBuilder = fixedFieldBuilder;
        this.monitor = monitor;
        this.treeReducer = treeReducer;
        this.reductiveRowSpecGenerator = reductiveRowSpecGenerator;
    }

    /* initialise the walker with a set (ReductiveState) of unfixed fields */
    public Stream<RowSpec> walk(DecisionTree tree, FixFieldStrategy fixFieldStrategy) {
        ConstraintNode rootNode = tree.getRootNode();
        ReductiveState initialState = new ReductiveState(tree.fields);

        visualise(rootNode, initialState);

        FixedField nextFixedField = fixedFieldBuilder.findNextFixedField(initialState, tree.rootNode, fixFieldStrategy);

        if (nextFixedField == null){
            //couldn't fix a field, maybe there are contradictions in the root node?

            return Stream.empty();
        }

        return process(rootNode, initialState.with(nextFixedField), fixFieldStrategy);
    }

    private Stream<RowSpec> process(ConstraintNode constraintNode, ReductiveState reductiveState, FixFieldStrategy fixFieldStrategy) {
        /* if all fields are fixed, return a stream of the values for the last fixed field with all other field values repeated */
        if (reductiveState.allFieldsAreFixed()){
            return reductiveRowSpecGenerator.createRowSpecsFromFixedValues(reductiveState, constraintNode);
        }

        Iterator<Object> valueIterator = reductiveState.getValuesFromLastFixedField().iterator();
        if (!valueIterator.hasNext()){
            this.monitor.noValuesForField(reductiveState);
            return Stream.empty();
        }

        // for each value for the last fixed field, fix the value and process the tree based on this field being fixed
        return FlatMappingSpliterator.flatMap(
            StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(valueIterator, Spliterator.ORDERED),
                false),
            fieldValue -> getRowSpecsForFixedField(constraintNode, reductiveState, fixFieldStrategy));
    }

    private Stream<RowSpec> getRowSpecsForFixedField(
        ConstraintNode constraintNode,
        ReductiveState reductiveState,
        FixFieldStrategy fixFieldStrategy){

        //reduce the tree based on the fields that are now fixed
        Optional<ConstraintNode> reducedNode = this.treeReducer.pruneConstraintNode(constraintNode, reductiveState.getLastFixedField());

        if (!reducedNode.isPresent()){
            //a field has been fixed, but is contradictory, i.e. it has invalidated the tree
            //yielding an empty stream will cause back-tracking

            this.monitor.unableToStepFurther(reductiveState);
            return Stream.empty();
        }

        //visualise the tree now
        visualise(reducedNode.get(), reductiveState);

        //find the next fixed field and continue
        FixedField nextFixedField = fixedFieldBuilder.findNextFixedField(reductiveState, reducedNode.get(), fixFieldStrategy);

        if (nextFixedField == null){
            //couldn't fix a field, maybe there are contradictions in the root node?

            return Stream.empty();
        }

        return process(reducedNode.get(), reductiveState.with(nextFixedField), fixFieldStrategy);
    }

    private void visualise(ConstraintNode rootNode, ReductiveState reductiveState){
        try {
            iterationVisualiser.visualise(rootNode, reductiveState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

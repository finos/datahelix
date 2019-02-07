package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.walker.reductive.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ReductiveDecisionTreeWalker implements DecisionTreeWalker {
    private final ReductiveDecisionTreeReducer treeReducer;
    private final IterationVisualiser iterationVisualiser;
    private final FixedFieldBuilder fixedFieldBuilder;
    private final ReductiveDataGeneratorMonitor monitor;
    private final ReductiveRowSpecGenerator reductiveRowSpecGenerator;
    private final GenerationConfig config;

    @Inject
    public ReductiveDecisionTreeWalker(
        IterationVisualiser iterationVisualiser,
        FixedFieldBuilder fixedFieldBuilder,
        ReductiveDataGeneratorMonitor monitor,
        ReductiveDecisionTreeReducer treeReducer,
        ReductiveRowSpecGenerator reductiveRowSpecGenerator,
        GenerationConfig config) {
        this.iterationVisualiser = iterationVisualiser;
        this.fixedFieldBuilder = fixedFieldBuilder;
        this.monitor = monitor;
        this.treeReducer = treeReducer;
        this.reductiveRowSpecGenerator = reductiveRowSpecGenerator;
        this.config = config;
    }

    /* initialise the walker with a set (ReductiveState) of unfixed fields */
    public Stream<RowSpec> walk(DecisionTree tree) {
        ConstraintNode rootNode = tree.getRootNode();
        ReductiveState initialState = new ReductiveState(tree.fields);

        visualise(rootNode, initialState);

        //calculate a field to fix and start processing
        ReductiveConstraintNode reduced = treeReducer.reduce(rootNode, initialState);

        if (reduced == null){
            //a field has been fixed, but is contradictory, i.e. it has invalidated the tree
            //yielding an empty stream will cause back-tracking

            this.monitor.unableToStepFurther(initialState);
            return Stream.empty();
        }

        Integer maxRowSpecsPerSource = config.getDataGenerationType() == GenerationConfig.DataGenerationType.RANDOM
            ? 1
            : null;

        Iterator<RowSpec> iterator = new SourceRepeatingIterator<>(
            maxRowSpecsPerSource,
            () -> {
                FixedField nextFixedField = fixedFieldBuilder.findNextFixedField(initialState, reduced);

                if (nextFixedField == null){
                    //couldn't fix a field, maybe there are contradictions in the root node?

                    return Collections.emptyIterator();
                }

                return process(rootNode, initialState.with(nextFixedField)).iterator();
            });

        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(
                iterator,
                Spliterator.ORDERED
            ), false);
    }

    private Stream<RowSpec> process(ConstraintNode constraintNode, ReductiveState reductiveState) {
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
            fieldValue -> getRowSpecsForFixedField(constraintNode, reductiveState));
    }

    private Stream<RowSpec> getRowSpecsForFixedField(
        ConstraintNode constraintNode,
        ReductiveState reductiveState){

        //reduce the tree based on the fields that are now fixed
        ReductiveConstraintNode reducedNode = this.treeReducer.reduce(constraintNode, reductiveState);
        if (reducedNode == null){
            //a field has been fixed, but is contradictory, i.e. it has invalidated the tree
            //yielding an empty stream will cause back-tracking

            this.monitor.unableToStepFurther(reductiveState);
            return Stream.empty();
        }

        //visualise the tree now
        visualise(reducedNode, reductiveState);

        //find the next fixed field and continue
        FixedField nextFixedField = fixedFieldBuilder.findNextFixedField(reductiveState, reducedNode);
        if (nextFixedField == null){
            //couldn't fix a field, maybe there are contradictions in the root node?

            return Stream.empty();
        }

        return process(reducedNode, reductiveState.with(nextFixedField));
    }

    private void visualise(ConstraintNode rootNode, ReductiveState reductiveState){
        try {
            iterationVisualiser.visualise(rootNode, reductiveState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

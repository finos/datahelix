package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.walker.reductive.*;

import java.io.IOException;
import java.util.stream.Stream;

public class ReductiveDecisionTreeWalker implements DecisionTreeWalker {
    private final ReductiveDecisionTreeReducer treeReducer;
    private final IterationVisualiser iterationVisualiser;
    private final FixedFieldBuilder fixedFieldBuilder;
    private final ReductiveDataGeneratorMonitor monitor;
    private final ReductiveRowSpecGenerator reductiveRowSpecGenerator;

    ReductiveDecisionTreeWalker(
        IterationVisualiser iterationVisualiser,
        FixedFieldBuilder fixedFieldBuilder,
        ReductiveDataGeneratorMonitor monitor,
        ReductiveDecisionTreeReducer treeReducer,
        ReductiveRowSpecGenerator reductiveRowSpecGenerator) {
        this.iterationVisualiser = iterationVisualiser;
        this.fixedFieldBuilder = fixedFieldBuilder;
        this.monitor = monitor;
        this.treeReducer = treeReducer;
        this.reductiveRowSpecGenerator = reductiveRowSpecGenerator;
    }

    /* initialise the walker with a set (ReductiveState) of unfixed fields */
    public Stream<RowSpec> walk(DecisionTree tree) {
        ReductiveState initialState = new ReductiveState(tree.fields);
        return walk(tree, initialState);
    }

    public Stream<RowSpec> walk(DecisionTree tree, ReductiveState initialState) {
        ConstraintNode rootNode = tree.getRootNode();
        visualise(rootNode, new ReductiveState(tree.fields));


        ReductiveConstraintNode reduced = treeReducer.reduce(rootNode, initialState);
        visualise(reduced, initialState);

        if (initialState.allValuesAreFixed()){
            return reductiveRowSpecGenerator.createRowSpecsFromFixedValues(initialState, reduced);
        }
        FixedField nextFixedField = fixedFieldBuilder.findNextFixedField(initialState, reduced);
        return process(rootNode, initialState.with(nextFixedField));
    }


    private Stream<RowSpec> process(ConstraintNode constraintNode, ReductiveState reductiveState) {
        /* if all fields are fixed, return a stream of the values for the last fixed field with all other field values repeated */
        if (reductiveState.allValuesAreFixed()){
            return reductiveRowSpecGenerator.createRowSpecsFromFixedValues(reductiveState, constraintNode);
        }

        // for each value for the last fixed field, fix the value and process the tree based on this field being fixed
        return FlatMappingSpliterator.flatMap(
            reductiveState.getValuesFromLastFixedField(),
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

        if (reductiveState.allValuesAreFixed()){
            return reductiveRowSpecGenerator.createRowSpecsFromFixedValues(reductiveState, constraintNode);
        }

        //find the next fixed field and continue
        FixedField nextFixedField = fixedFieldBuilder.findNextFixedField(reductiveState, reducedNode);
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

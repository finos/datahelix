package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.walker.reductive.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.stream.Stream;

public class ReductiveDecisionTreeWalker implements DecisionTreeWalker {
    Logger logger = LogManager.getLogger(ReductiveDecisionTreeWalker.class);
    private final ReductiveDecisionTreeReducer treeReducer;
    private final IterationVisualiser iterationVisualiser;
    private final FixedFieldBuilder fixedFieldBuilder;
    private final ReductiveRowSpecGenerator reductiveRowSpecGenerator;

    ReductiveDecisionTreeWalker(
        IterationVisualiser iterationVisualiser,
        FixedFieldBuilder fixedFieldBuilder,
        ReductiveDecisionTreeReducer treeReducer,
        ReductiveRowSpecGenerator reductiveRowSpecGenerator) {
        this.iterationVisualiser = iterationVisualiser;
        this.fixedFieldBuilder = fixedFieldBuilder;
        this.treeReducer = treeReducer;
        this.reductiveRowSpecGenerator = reductiveRowSpecGenerator;
    }

    /* initialise the walker with a set (ReductiveState) of unfixed fields */
    public Stream<RowSpec> walk(DecisionTree tree) {
        ConstraintNode rootNode = tree.getRootNode();
        ReductiveState initialState = new ReductiveState(tree.fields);

        visualise(rootNode, initialState);

        //calculate a field to fix and start processing
        ReductiveConstraintNode reduced = treeReducer.reduce(rootNode, initialState);
        FixedField nextFixedField = fixedFieldBuilder.findNextFixedField(initialState, reduced);
        return process(rootNode, initialState.with(nextFixedField));
    }

    private Stream<RowSpec> process(ConstraintNode constraintNode, ReductiveState reductiveState) {
        /* if all fields are fixed, return a stream of the values for the last fixed field with all other field values repeated */
        if (reductiveState.allFieldsAreFixed()){
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

            logger.debug("{}: Unable to step further {} ", reductiveState.getFixedFieldsExceptLast().size(),
                                                                reductiveState.toString(true));
            return Stream.empty();
        }

        //visualise the tree now
        visualise(reducedNode, reductiveState);

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

package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.walker.reductive.FieldCollection;
import com.scottlogic.deg.generator.walker.reductive.FieldCollectionFactory;
import com.scottlogic.deg.generator.walker.reductive.IterationVisualiser;
import com.scottlogic.deg.generator.walker.reductive.ReductiveDecisionTreeAdapter;

import java.io.IOException;
import java.util.stream.Stream;

public class ReductiveDecisionTreeWalker implements DecisionTreeWalker {
    private final ReductiveDecisionTreeAdapter nodeAdapter = new ReductiveDecisionTreeAdapter();

    private final IterationVisualiser iterationVisualiser;
    private final FieldCollectionFactory fieldCollectionFactory;
    private final ReductiveDataGeneratorMonitor monitor;

    ReductiveDecisionTreeWalker(
        IterationVisualiser iterationVisualiser,
        FieldCollectionFactory fieldCollectionFactory,
        ReductiveDataGeneratorMonitor monitor) {
        this.iterationVisualiser = iterationVisualiser;
        this.fieldCollectionFactory = fieldCollectionFactory;
        this.monitor = monitor;
    }

    /* initialise the walker with a set (FieldCollection) of unfixed fields */
    public Stream<RowSpec> walk(DecisionTree tree) {
        FieldCollection fieldCollection = fieldCollectionFactory.create(tree);
        return walk(tree, fieldCollection);
    }

    public Stream<RowSpec> walk(DecisionTree tree, FieldCollection fieldCollection) {
        ConstraintNode rootNode = tree.getRootNode();
        visualise(rootNode, fieldCollection);

        ReductiveConstraintNode adaptedRootNode = nodeAdapter.adapt(rootNode, fieldCollection);
        if (fieldCollection.allFieldsAreFixed()){
            return fieldCollection.createRowSpecFromFixedValues(adaptedRootNode);
        }

        //calculate a field to fix and start processing
        return process(rootNode, fieldCollection.getNextFixedField(adaptedRootNode));
    }

    private Stream<RowSpec> process(ConstraintNode constraintNode, FieldCollection fieldCollection) {
        /* if all fields are fixed, return a stream of the values for the last fixed field with all other field values repeated */
        if (fieldCollection.allValuesAreFixed()){
            return fieldCollection.createRowSpecFromFixedValues(constraintNode);
        }

        // for each value for the last fixed field, fix the value and process the tree based on this field being fixed
        return FlatMappingSpliterator.flatMap(
            fieldCollection.getValuesFromLastFixedField(),
            fieldValue -> getRowSpecsForFixedField(constraintNode, fieldCollection));
    }

    private Stream<RowSpec> getRowSpecsForFixedField(
        ConstraintNode constraintNode,
        FieldCollection fieldCollection){

        //reduce the tree based on the fields that are now fixed
        ReductiveConstraintNode adaptedNode = this.nodeAdapter.adapt(constraintNode, fieldCollection);
        if (adaptedNode == null){
            //a field has been fixed, but is contradictory, i.e. it has invalidated the tree
            //yielding an empty stream will cause back-tracking

            this.monitor.unableToStepFurther(fieldCollection);
            return Stream.empty();
        }

        //visualise the tree now
        visualise(adaptedNode, fieldCollection);

        if (fieldCollection.allValuesAreFixed()){
            return fieldCollection.createRowSpecFromFixedValues(constraintNode);
        }

        //find the next fixed field and continue
        return process(adaptedNode, fieldCollection.getNextFixedField(adaptedNode));
    }

    private void visualise(ConstraintNode rootNode, FieldCollection fieldCollection){
        try {
            iterationVisualiser.visualise(rootNode, fieldCollection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ReductiveDataGeneratorMonitor getMonitor(){
        return this.monitor;
    }
}

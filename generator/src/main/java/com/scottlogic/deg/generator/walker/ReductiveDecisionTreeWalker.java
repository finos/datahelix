package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.walker.reductive.FieldCollection;
import com.scottlogic.deg.generator.walker.reductive.FieldCollectionFactory;
import com.scottlogic.deg.generator.walker.reductive.IterationVisualiser;
import com.scottlogic.deg.generator.walker.reductive.ReductiveDecisionTreeAdapter;

import java.util.stream.Stream;

public class ReductiveDecisionTreeWalker implements DecisionTreeWalker {
    private final ReductiveDecisionTreeAdapter nodeAdapter = new ReductiveDecisionTreeAdapter();

    private final IterationVisualiser iterationVisualiser;
    private final FieldCollectionFactory fieldCollectionFactory;

    ReductiveDecisionTreeWalker(
        IterationVisualiser iterationVisualiser,
        FieldCollectionFactory fieldCollectionFactory) {
        this.iterationVisualiser = iterationVisualiser;
        this.fieldCollectionFactory = fieldCollectionFactory;
    }

    /* initialise the walker with a set (FieldCollection) of unfixed fields */
    public Stream<RowSpec> walk(DecisionTree tree) {
        ConstraintNode rootNode = tree.getRootNode();
        FieldCollection fieldCollection = fieldCollectionFactory.create(tree);

        iterationVisualiser.visualise(rootNode, fieldCollection);

        //calculate a field to fix and start processing
        return process(rootNode, fieldCollection.getNextFixedField(nodeAdapter.adapt(rootNode, fieldCollection)));
    }

    private Stream<RowSpec> process(ConstraintNode constraintNode, FieldCollection fieldCollection) {
        /* if all fields are fixed, return a stream of the values for the last fixed field with all other field values repeated */
        if (fieldCollection.allFieldsAreFixed()){
            return fieldCollection.createRowSpecFromFixedValues(constraintNode);
        }

        // for each value for the last fixed field, fix the value and process the tree based on this field being fixed
        return fieldCollection.getValuesFromLastFixedField()
            .flatMap(fieldValue -> getRowSpecsForFixedField(constraintNode, fieldCollection));
    }

    private Stream<RowSpec> getRowSpecsForFixedField(
        ConstraintNode constraintNode,
        FieldCollection fieldCollection){

        //reduce the tree based on the fields that are now fixed
        ReductiveConstraintNode adaptedNode = this.nodeAdapter.adapt(constraintNode, fieldCollection);
        if (adaptedNode == null){
            //a field has been fixed, but is contradictory, i.e. it has invalidated the tree
            //yielding an empty stream will cause back-tracking

            System.out.println(String.format("%d: Unable to step further %s ", fieldCollection.getFixedFields().size(), fieldCollection.toString(true)));

            return Stream.empty();
        }

        //visualise the tree now
        this.iterationVisualiser.visualise(adaptedNode, fieldCollection);

        //find the next fixed field and continue
        return process(adaptedNode, fieldCollection.getNextFixedField(adaptedNode));
    }
}

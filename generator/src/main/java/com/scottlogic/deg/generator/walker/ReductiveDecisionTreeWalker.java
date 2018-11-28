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

    public Stream<RowSpec> walk(DecisionTree tree) {
        ConstraintNode rootNode = tree.getRootNode();
        FieldCollection fieldCollection = fieldCollectionFactory.create(tree);

        iterationVisualiser.visualise(rootNode, fieldCollection);

        return process(rootNode, fieldCollection.getNextFixedField(nodeAdapter.adapt(rootNode, fieldCollection)));
    }

    private Stream<RowSpec> process(ConstraintNode constraintNode, FieldCollection fieldCollection) {
        if (fieldCollection.allFieldsAreFixed()){
            return fieldCollection.createRowSpecFromFixedValues(constraintNode);
        }

        return fieldCollection.getValuesFromLastFixedField()
            .flatMap(fieldValue -> getRowSpecsForFixedField(constraintNode, fieldCollection));
    }

    private Stream<RowSpec> getRowSpecsForFixedField(
        ConstraintNode constraintNode,
        FieldCollection fieldCollection){
        ReductiveConstraintNode adaptedNode = this.nodeAdapter.adapt(constraintNode, fieldCollection);
        if (adaptedNode == null){
            this.nodeAdapter.adapt(constraintNode, fieldCollection);

            System.out.println(String.format("%d: Unable to step further %s ", fieldCollection.getFixedFields().size(), fieldCollection.toString(true)));

            return Stream.empty();
        }

        this.iterationVisualiser.visualise(adaptedNode, fieldCollection);

        return process(adaptedNode, fieldCollection.getNextFixedField(adaptedNode));
    }
}

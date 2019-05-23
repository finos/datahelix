package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.walker.reductive.*;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategyFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class ReductiveDecisionTreeWalker implements DecisionTreeWalker {
    private final ReductiveTreePruner treePruner;
    private final IterationVisualiser iterationVisualiser;
    private final ReductiveFieldSpecBuilder reductiveFieldSpecBuilder;
    private final ReductiveDataGeneratorMonitor monitor;
    private final FieldSpecValueGenerator fieldSpecValueGenerator;
    private final FixFieldStrategyFactory fixFieldStrategyFactory;

    @Inject
    public ReductiveDecisionTreeWalker(
        IterationVisualiser iterationVisualiser,
        ReductiveFieldSpecBuilder reductiveFieldSpecBuilder,
        ReductiveDataGeneratorMonitor monitor,
        ReductiveTreePruner treePruner,
        FieldSpecValueGenerator fieldSpecValueGenerator,
        FixFieldStrategyFactory fixFieldStrategyFactory) {
        this.iterationVisualiser = iterationVisualiser;
        this.reductiveFieldSpecBuilder = reductiveFieldSpecBuilder;
        this.monitor = monitor;
        this.treePruner = treePruner;
        this.fieldSpecValueGenerator = fieldSpecValueGenerator;
        this.fixFieldStrategyFactory = fixFieldStrategyFactory;
    }

    /* initialise the walker with a set (ReductiveState) of unfixed fields */
    @Override
    public Stream<DataBag> walk(DecisionTree tree) {
        ReductiveState initialState = new ReductiveState(tree.fields);
        visualise(tree.getRootNode(), initialState);
        FixFieldStrategy fixFieldStrategy = fixFieldStrategyFactory.create(tree.getRootNode());
        return fixNextField(tree.getRootNode(), initialState, fixFieldStrategy);
    }

    private Stream<DataBag> fixNextField(ConstraintNode tree, ReductiveState reductiveState, FixFieldStrategy fixFieldStrategy) {
        Field fieldToFix = fixFieldStrategy.getNextFieldToFix(reductiveState);

        Optional<FieldSpec> nextFieldSpec = reductiveFieldSpecBuilder.getFieldSpecWithMustContains(tree, fieldToFix);
        if (!nextFieldSpec.isPresent()){
            //couldn't fix a field, maybe there are contradictions in the root node?
            monitor.noValuesForField(reductiveState, fieldToFix);
            return Stream.empty();
        }

        Stream<DataBagValue> values = fieldSpecValueGenerator.generate(nextFieldSpec.get());

        return FlatMappingSpliterator.flatMap(
            values,
            dataBagValue -> pruneTreeForNextValue(tree, reductiveState, fixFieldStrategy, fieldToFix, dataBagValue));
    }

    private Stream<DataBag> pruneTreeForNextValue(
        ConstraintNode tree,
        ReductiveState reductiveState,
        FixFieldStrategy fixFieldStrategy,
        Field field,
        DataBagValue fieldValue){

        Merged<ConstraintNode> reducedTree = treePruner.pruneConstraintNode(tree, field, fieldValue);

        if (reducedTree.isContradictory()){
            //yielding an empty stream will cause back-tracking
            this.monitor.unableToStepFurther(reductiveState);
            return Stream.empty();
        }

        monitor.fieldFixedToValue(field, fieldValue.getFormattedValue());

        ReductiveState newReductiveState =
            reductiveState.withFixedFieldValue(field, fieldValue);
        visualise(reducedTree.get(), newReductiveState);

        if (newReductiveState.allFieldsAreFixed()){
            return Stream.of(newReductiveState.asDataBag());
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

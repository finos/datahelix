package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;
import com.scottlogic.deg.generator.walker.reductive.*;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategyFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class ReductiveDataGenerator implements DataGenerator {
    private final ReductiveTreePruner treePruner;
    private final IterationVisualiser iterationVisualiser;
    private final ReductiveFieldSpecBuilder reductiveFieldSpecBuilder;
    private final ReductiveDataGeneratorMonitor monitor;
    private final FieldSpecValueGenerator fieldSpecValueGenerator;
    private final FixFieldStrategyFactory fixFieldStrategyFactory;

    @Inject
    public ReductiveDataGenerator(
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

    @Override
    public Stream<GeneratedObject> generateData(Profile profile, DecisionTree tree, GenerationConfig generationConfig) {
        FixFieldStrategy fixFieldStrategy = fixFieldStrategyFactory.getWalkerStrategy(profile, tree, generationConfig);
        ReductiveState initialState = new ReductiveState(tree.fields);
        visualise(tree.getRootNode(), initialState);
        return fixNextField(tree.getRootNode(), initialState, fixFieldStrategy);
    }

    private Stream<GeneratedObject> fixNextField(ConstraintNode tree, ReductiveState reductiveState, FixFieldStrategy fixFieldStrategy) {

        Field fieldToFix = fixFieldStrategy.getNextFieldToFix(reductiveState, tree);
        Optional<FieldSpec> nextFieldSpec = reductiveFieldSpecBuilder.getFieldSpecWithMustContains(tree, fieldToFix);

        if (!nextFieldSpec.isPresent()){
            //couldn't fix a field, maybe there are contradictions in the root node?
            monitor.noValuesForField(reductiveState, fieldToFix);
            return Stream.empty();
        }

        Stream<DataBagValue> values = fieldSpecValueGenerator.generate(fieldToFix, nextFieldSpec.get());

        return FlatMappingSpliterator.flatMap(
            values,
            fieldValue -> pruneTreeForNextValue(tree, reductiveState, fixFieldStrategy, fieldValue));
    }

    private Stream<GeneratedObject> pruneTreeForNextValue(
        ConstraintNode tree,
        ReductiveState reductiveState,
        FixFieldStrategy fixFieldStrategy,
        DataBagValue fieldValue){

        Merged<ConstraintNode> reducedTree = this.treePruner.pruneConstraintNode(tree, fieldValue);

        if (reducedTree.isContradictory()){
            //yielding an empty stream will cause back-tracking
            this.monitor.unableToStepFurther(reductiveState);
            return Stream.empty();
        }

        monitor.fieldFixedToValue(fieldValue.getField(), fieldValue.getValue());

        ReductiveState newReductiveState =
            reductiveState.withFixedFieldValue(fieldValue);
        visualise(reducedTree.get(), newReductiveState);

        if (newReductiveState.allFieldsAreFixed()){
            return Stream.of(new GeneratedObject(newReductiveState.getFieldValues()));
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

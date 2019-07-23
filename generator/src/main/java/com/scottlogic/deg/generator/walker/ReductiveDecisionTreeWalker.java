/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.util.FlatMappingSpliterator;
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
import java.util.Set;
import java.util.stream.Stream;

public class ReductiveDecisionTreeWalker implements DecisionTreeWalker {
    private final ReductiveTreePruner treePruner;
    private final IterationVisualiser iterationVisualiser;
    private final ReductiveFieldSpecBuilder reductiveFieldSpecBuilder;
    private final ReductiveDataGeneratorMonitor monitor;
    private final FieldSpecValueGenerator fieldSpecValueGenerator;
    private final FixFieldStrategyFactory fixFieldStrategyFactory;
    private ReductiveWalkerRetryChecker retryChecker;

    @Inject
    public ReductiveDecisionTreeWalker(
        IterationVisualiser iterationVisualiser,
        ReductiveFieldSpecBuilder reductiveFieldSpecBuilder,
        ReductiveDataGeneratorMonitor monitor,
        ReductiveTreePruner treePruner,
        FieldSpecValueGenerator fieldSpecValueGenerator,
        FixFieldStrategyFactory fixFieldStrategyFactory,
        ReductiveWalkerRetryChecker retryChecker) {
        this.iterationVisualiser = iterationVisualiser;
        this.reductiveFieldSpecBuilder = reductiveFieldSpecBuilder;
        this.monitor = monitor;
        this.treePruner = treePruner;
        this.fieldSpecValueGenerator = fieldSpecValueGenerator;
        this.fixFieldStrategyFactory = fixFieldStrategyFactory;
        this.retryChecker = retryChecker;
    }

    /* initialise the walker with a set (ReductiveState) of unfixed fields */
    @Override
    public Stream<DataBag> walk(DecisionTree tree) {
        ReductiveState initialState = new ReductiveState(tree.fields);
        visualise(tree.getRootNode(), initialState);
        FixFieldStrategy fixFieldStrategy = fixFieldStrategyFactory.create(tree.getRootNode());
        retryChecker.reset();
        return fixNextField(tree.getRootNode(), initialState, fixFieldStrategy);
    }

    private Stream<DataBag> fixNextField(ConstraintNode tree, ReductiveState reductiveState, FixFieldStrategy fixFieldStrategy) {
        Field fieldToFix = fixFieldStrategy.getNextFieldToFix(reductiveState);
        Set<FieldSpec> nextFieldSpecs = reductiveFieldSpecBuilder.getDecisionFieldSpecs(tree, fieldToFix);

        if (nextFieldSpecs.isEmpty()){
            monitor.noValuesForField(reductiveState, fieldToFix);
            return Stream.empty();
        }

        Stream<DataBagValue> values = fieldSpecValueGenerator.generate(nextFieldSpecs);

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
            retryChecker.retryUnsuccessful();
            return Stream.empty();
        }

        monitor.fieldFixedToValue(field, fieldValue.getFormattedValue());

        ReductiveState newReductiveState =
            reductiveState.withFixedFieldValue(field, fieldValue);
        visualise(reducedTree.get(), newReductiveState);

        if (newReductiveState.allFieldsAreFixed()) {
            retryChecker.retrySuccessful();
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

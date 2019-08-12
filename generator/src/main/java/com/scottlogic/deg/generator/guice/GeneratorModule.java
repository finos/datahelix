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

package com.scottlogic.deg.generator.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.scottlogic.deg.generator.config.detail.DataGenerationType;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.MaxStringLengthInjectingDecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.ReductiveWalkerRetryChecker;
import com.scottlogic.deg.generator.walker.decisionbased.OptionPicker;
import com.scottlogic.deg.generator.walker.reductive.IterationVisualiser;
import com.scottlogic.deg.generator.walker.rowspec.RowSpecTreeSolver;

import java.time.OffsetDateTime;

/**
 * Class to define default bindings for Guice injection. Utilises the generation config source to determine which
 * 'generate' classes should be bound for this execution run.
 */
public class GeneratorModule extends AbstractModule {
    private final GenerationConfigSource generationConfigSource;

    public GeneratorModule(GenerationConfigSource configSource) {
        this.generationConfigSource = configSource;
    }

    @Override
    protected void configure() {
        // Bind command line to correct implementation
        bind(GenerationConfigSource.class).toInstance(generationConfigSource);

        // Bind providers - used to retrieve implementations based on user input
        bind(DecisionTreeOptimiser.class).toProvider(DecisionTreeOptimiserProvider.class);
        bind(TreePartitioner.class).toProvider(TreePartitioningProvider.class);
        bind(DecisionTreeWalker.class).toProvider(DecisionTreeWalkerProvider.class);
        bind(ProfileValidator.class).toProvider(ProfileValidatorProvider.class);
        bind(ReductiveDataGeneratorMonitor.class).toProvider(MonitorProvider.class).in(Singleton.class);
        bind(IterationVisualiser.class).toProvider(IterationVisualiserProvider.class);
        bind(CombinationStrategy.class).toProvider(CombinationStrategyProvider.class);
        bind(OptionPicker.class).toProvider(OptionPickerProvider.class);
        bind(RowSpecTreeSolver.class).toProvider(RowSpecTreeSolverProvider.class);

        // bind config directly
        bind(DataGenerationType.class).toInstance(generationConfigSource.getGenerationType());

        bind(long.class)
            .annotatedWith(Names.named("config:maxRows"))
            .toInstance(generationConfigSource.getMaxRows());

        // Bind known implementations - no user input required
        bind(DataGeneratorMonitor.class).to(ReductiveDataGeneratorMonitor.class);
        bind(DataGenerator.class).to(DecisionTreeDataGenerator.class);
        bind(DecisionTreeFactory.class).to(MaxStringLengthInjectingDecisionTreeFactory.class);
        bind(FieldValueSourceEvaluator.class).to(StandardFieldValueSourceEvaluator.class);
        bind(ReductiveWalkerRetryChecker.class).toInstance(new ReductiveWalkerRetryChecker(10000));

        bind(JavaUtilRandomNumberGenerator.class)
            .toInstance(new JavaUtilRandomNumberGenerator(OffsetDateTime.now().getNano()));
    }
}

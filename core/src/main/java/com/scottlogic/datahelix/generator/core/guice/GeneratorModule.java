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

package com.scottlogic.datahelix.generator.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.scottlogic.datahelix.generator.core.config.detail.CombinationStrategyType;
import com.scottlogic.datahelix.generator.core.config.detail.DataGenerationType;
import com.scottlogic.datahelix.generator.core.config.detail.MonitorType;
import com.scottlogic.datahelix.generator.core.generation.*;
import com.scottlogic.datahelix.generator.core.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.datahelix.generator.core.utils.JavaUtilRandomNumberGenerator;
import com.scottlogic.datahelix.generator.core.walker.DecisionTreeWalker;
import com.scottlogic.datahelix.generator.core.walker.decisionbased.OptionPicker;

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
        bind(DecisionTreeWalker.class).toProvider(DecisionTreeWalkerProvider.class);
        bind(AbstractDataGeneratorMonitor.class).toProvider(MonitorProvider.class).in(Singleton.class);
        bind(CombinationStrategy.class).toProvider(CombinationStrategyProvider.class);
        bind(OptionPicker.class).toProvider(OptionPickerProvider.class);

        // bind config directly
        bind(DataGenerationType.class).toInstance(generationConfigSource.getGenerationType());
        bind(CombinationStrategyType.class).toInstance(generationConfigSource.getCombinationStrategyType());

        bind(long.class)
            .annotatedWith(Names.named("config:maxRows"))
            .toInstance(generationConfigSource.getMaxRows());

        bind(MonitorType.class)
            .toInstance(generationConfigSource.getMonitorType());

        // Bind known implementations - no user input required
        bind(DataGeneratorMonitor.class).to(AbstractDataGeneratorMonitor.class);
        bind(DataGenerator.class).toProvider(DataGeneratorProvider.class);

        bind(JavaUtilRandomNumberGenerator.class)
            .toInstance(new JavaUtilRandomNumberGenerator(OffsetDateTime.now().getNano()));
        bind(int.class)
            .annotatedWith(Names.named("config:internalRandomRowSpecStorage"))
            .toInstance(256);
    }
}

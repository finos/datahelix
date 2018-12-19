package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.walker.DecisionTreeWalkerFactory;
import com.scottlogic.deg.generator.walker.RuntimeDecisionTreeWalkerFactory;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;

public class DecisionTreeWalkerFactoryProvider implements Provider<DecisionTreeWalkerFactory> {

    private Provider<GenerationConfig> config;
    private final ReductiveDataGeneratorMonitor monitor;
    private final Provider<FixFieldStrategy> fixFieldStrategy;

    @Inject
    public DecisionTreeWalkerFactoryProvider(Provider<GenerationConfig> config, ReductiveDataGeneratorMonitor monitor,
                                     Provider<FixFieldStrategy> fixFieldStrategy) {
        this.config = config;
        this.monitor = monitor;
        this.fixFieldStrategy = fixFieldStrategy;
    }

    @Override
    public DecisionTreeWalkerFactory get() {
        return new RuntimeDecisionTreeWalkerFactory(config.get(), monitor, fixFieldStrategy.get());
    }
}

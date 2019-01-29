package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.MostProlificConstraintOptimiser;
import com.scottlogic.deg.generator.decisiontree.NoopDecisionTreeOptimiser;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;

public class DecisionTreeOptimiserProvider implements Provider<DecisionTreeOptimiser> {
    private final GenerationConfigSource configSource;

    @Inject
    public DecisionTreeOptimiserProvider(GenerationConfigSource configSource) {
        this.configSource = configSource;
    }

    @Override
    public DecisionTreeOptimiser get() {
        if(this.configSource.dontOptimise()) {
            return new NoopDecisionTreeOptimiser();
        }
        return new MostProlificConstraintOptimiser();
    }
}

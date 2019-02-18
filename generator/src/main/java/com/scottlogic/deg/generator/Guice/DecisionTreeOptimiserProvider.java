package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.ConfigSource;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.MostProlificConstraintOptimiser;
import com.scottlogic.deg.generator.decisiontree.NoopDecisionTreeOptimiser;

public class DecisionTreeOptimiserProvider implements Provider<DecisionTreeOptimiser> {
    private final ConfigSource configSource;

    @Inject
    public DecisionTreeOptimiserProvider(ConfigSource configSource) {
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

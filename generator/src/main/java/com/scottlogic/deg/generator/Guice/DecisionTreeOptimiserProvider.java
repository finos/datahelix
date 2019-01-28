package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.MostProlificConstraintOptimiser;
import com.scottlogic.deg.generator.decisiontree.NoopDecisionTreeOptimiser;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;

public class DecisionTreeOptimiserProvider implements Provider<DecisionTreeOptimiser> {
    private final GenerationConfigSource commandLine;

    @Inject
    public DecisionTreeOptimiserProvider(GenerationConfigSource commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    public DecisionTreeOptimiser get() {
        if(this.commandLine.dontOptimise()) {
            return new NoopDecisionTreeOptimiser();
        }
        return new MostProlificConstraintOptimiser();
    }
}

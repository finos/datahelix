package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.CommandLine.GenerateCommandLine;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.MostProlificConstraintOptimiser;
import com.scottlogic.deg.generator.decisiontree.NoopDecisionTreeOptimiser;

public class DecisionTreeOptimiserProvider implements Provider<DecisionTreeOptimiser> {
    private final GenerateCommandLine commandLine;

    @Inject
    public DecisionTreeOptimiserProvider(GenerateCommandLine commandLine) {
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

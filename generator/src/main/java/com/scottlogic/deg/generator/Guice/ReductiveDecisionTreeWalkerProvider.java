package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.ReductiveDecisionTreeWalker;
import com.scottlogic.deg.generator.walker.reductive.FieldCollectionFactory;
import com.scottlogic.deg.generator.walker.reductive.IterationVisualiser;

public class ReductiveDecisionTreeWalkerProvider implements Provider<DecisionTreeWalker> {

    private IterationVisualiser iterationVisualiser;
    private FieldCollectionFactory fieldCollectionFactory;
    private ReductiveDataGeneratorMonitor monitor;

    @Inject
    public ReductiveDecisionTreeWalkerProvider(IterationVisualiser iterationVisualiser,
                                               FieldCollectionFactory fieldCollectionFactory,
                                               ReductiveDataGeneratorMonitor monitor) {
        this.iterationVisualiser = iterationVisualiser;
        this.fieldCollectionFactory = fieldCollectionFactory;
        this.monitor = monitor;
    }

    @Override
    public DecisionTreeWalker get() {
        return new ReductiveDecisionTreeWalker(iterationVisualiser, fieldCollectionFactory, monitor);
    }
}
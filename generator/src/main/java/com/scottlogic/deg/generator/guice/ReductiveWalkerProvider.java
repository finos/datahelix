package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.walker.ReductiveDecisionTreeWalker;
import com.scottlogic.deg.generator.walker.ReductiveTreeWalker;
import com.scottlogic.deg.generator.walker.ReductiveVisualiseDecisionTreeWalker;

public class ReductiveWalkerProvider implements Provider<ReductiveTreeWalker> {
    private final GenerationConfigSource configSource;
    private final ReductiveDecisionTreeWalker reductiveDecisionTreeWalker;
    private final ReductiveVisualiseDecisionTreeWalker reductiveVisualiseDecisionTreeWalker;

    @Inject
    public ReductiveWalkerProvider(GenerationConfigSource configSource,
                                   ReductiveDecisionTreeWalker reductiveDecisionTreeWalker,
                                   ReductiveVisualiseDecisionTreeWalker reductiveVisualiseDecisionTreeWalker) {
        this.configSource = configSource;
        this.reductiveDecisionTreeWalker = reductiveDecisionTreeWalker;
        this.reductiveVisualiseDecisionTreeWalker = reductiveVisualiseDecisionTreeWalker;
    }


    @Override
    public ReductiveTreeWalker get() {
        return  configSource.visualiseReductions() ? reductiveVisualiseDecisionTreeWalker : reductiveDecisionTreeWalker;
    }
}

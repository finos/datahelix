package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.walker.*;

public class DecisionTreeWalkerProvider implements Provider<DecisionTreeWalker> {
    private final DecisionTreeWalker cartesianProductDecisionTreeWalker;
    private final DecisionTreeWalker routedDecisionTreeWalker;
    private final GenerationConfigSource configSource;

    @Inject
    public DecisionTreeWalkerProvider(
        CartesianProductDecisionTreeWalker cartesianProductDecisionTreeWalker,
        DecisionTreeRoutesTreeWalker routedDecisionTreeWalker,
        GenerationConfigSource configSource) {
        this.cartesianProductDecisionTreeWalker = cartesianProductDecisionTreeWalker;
        this.routedDecisionTreeWalker = routedDecisionTreeWalker;
        this.configSource = configSource;
    }

    @Override
    public DecisionTreeWalker get() {
          switch(this.configSource.getWalkerType()) {
              case ROUTED: return routedDecisionTreeWalker;
              default: return cartesianProductDecisionTreeWalker;
        }
    }
}

package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.walker.*;

public class DecisionTreeWalkerProvider implements Provider<DecisionTreeWalker> {
    private final DecisionTreeWalker reductiveDecisionTreeWalker;
    private final DecisionTreeWalker cartesianProductDecisionTreeWalker;
    private final DecisionTreeWalker routedDecisionTreeWalker;
    private final RandomReductiveDecisionTreeWalker randomReductiveDecisionTreeWalker;
    private final GenerationConfigSource configSource;

    @Inject
    public DecisionTreeWalkerProvider(
        ReductiveDecisionTreeWalker reductiveDecisionTreeWalker,
        CartesianProductDecisionTreeWalker cartesianProductDecisionTreeWalker,
        DecisionTreeRoutesTreeWalker routedDecisionTreeWalker,
        RandomReductiveDecisionTreeWalker randomReductiveDecisionTreeWalker,
        GenerationConfigSource configSource) {
        this.reductiveDecisionTreeWalker = reductiveDecisionTreeWalker;
        this.cartesianProductDecisionTreeWalker = cartesianProductDecisionTreeWalker;
        this.routedDecisionTreeWalker = routedDecisionTreeWalker;
        this.randomReductiveDecisionTreeWalker = randomReductiveDecisionTreeWalker;
        this.configSource = configSource;
    }

    @Override
    public DecisionTreeWalker get() {
          switch(this.configSource.getWalkerType()) {
              case CARTESIAN_PRODUCT:
                  return this.cartesianProductDecisionTreeWalker;

              case REDUCTIVE:
                  if (this.configSource.getGenerationType() == GenerationConfig.DataGenerationType.RANDOM)
                      return this.randomReductiveDecisionTreeWalker;

                  return this.reductiveDecisionTreeWalker;

              case ROUTED:
                  return this.routedDecisionTreeWalker;

              default:
                  return this.reductiveDecisionTreeWalker;
        }
    }
}

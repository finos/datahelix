package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.config.detail.DataGenerationType;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.walker.*;

public class DecisionTreeWalkerProvider implements Provider<DecisionTreeWalker> {
    private final DecisionTreeWalker reductiveDecisionTreeWalker;
    private final DecisionTreeWalker cartesianProductDecisionTreeWalker;
    private final RandomReductiveDecisionTreeWalker randomReductiveDecisionTreeWalker;
    private final GenerationConfigSource configSource;

    @Inject
    public DecisionTreeWalkerProvider(
        ReductiveDecisionTreeWalker reductiveDecisionTreeWalker,
        CartesianProductDecisionTreeWalker cartesianProductDecisionTreeWalker,
        RandomReductiveDecisionTreeWalker randomReductiveDecisionTreeWalker,
        GenerationConfigSource configSource) {
        this.reductiveDecisionTreeWalker = reductiveDecisionTreeWalker;
        this.cartesianProductDecisionTreeWalker = cartesianProductDecisionTreeWalker;
        this.randomReductiveDecisionTreeWalker = randomReductiveDecisionTreeWalker;
        this.configSource = configSource;
    }

    @Override
    public DecisionTreeWalker get() {
          switch(this.configSource.getWalkerType()) {
              case CARTESIAN_PRODUCT:
                  return this.cartesianProductDecisionTreeWalker;

              case REDUCTIVE:
                  if (this.configSource.getGenerationType() == DataGenerationType.RANDOM)
                      return this.randomReductiveDecisionTreeWalker;

                  return this.reductiveDecisionTreeWalker;

              default:
                  return this.reductiveDecisionTreeWalker;
        }
    }
}

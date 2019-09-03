package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.walker.decisionbased.DecisionBasedSolver;
import com.scottlogic.deg.generator.walker.rowspec.CartesianProductRowSpecTreeSolver;
import com.scottlogic.deg.generator.walker.rowspec.RowSpecTreeSolver;

public class RowSpecTreeSolverProvider implements Provider<RowSpecTreeSolver> {

    private final GenerationConfigSource config;
    private final CartesianProductRowSpecTreeSolver cartesianProductRowSpecTreeSolver;
    private final DecisionBasedSolver decisionBasedSolver;

    @Inject
    public RowSpecTreeSolverProvider(GenerationConfigSource config, CartesianProductRowSpecTreeSolver cartesianProductRowSpecTreeSolver, DecisionBasedSolver decisionBasedSolver) {
        this.config = config;
        this.cartesianProductRowSpecTreeSolver = cartesianProductRowSpecTreeSolver;
        this.decisionBasedSolver = decisionBasedSolver;
    }

    @Override
    public RowSpecTreeSolver get() {
        switch (config.getWalkerType()) {
            case DECISION_BASED:
                return decisionBasedSolver;
            case CARTESIAN_PRODUCT:
                return cartesianProductRowSpecTreeSolver;
            default:
                throw new ValidationException("no WalkerType selected");
        }
    }
}

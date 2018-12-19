package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.CommandLine.CanGenerate;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

public class DecisionTreeWalkerProvider implements Provider<DecisionTreeWalker> {
    private final Provider<DecisionTreeWalker> reductiveFactory;
    private final Provider<DecisionTreeWalker> cartesianFactory;
    private final CanGenerate commandLine;

    @Inject
    public DecisionTreeWalkerProvider(
        Provider<DecisionTreeWalker> reductiveFactory,
        Provider<DecisionTreeWalker> cartesianFactory,
        CanGenerate commandLine) {
        this.reductiveFactory = reductiveFactory;
        this.cartesianFactory = cartesianFactory;
        this.commandLine = commandLine;
    }

    @Override
            public DecisionTreeWalker get() {
          switch(this.commandLine.getWalkerType()) {
              case CARTESIAN_PRODUCT:
                return this.cartesianFactory.get();

              case REDUCTIVE:
                return this.reductiveFactory.get();
        }
        return null;
    }
}

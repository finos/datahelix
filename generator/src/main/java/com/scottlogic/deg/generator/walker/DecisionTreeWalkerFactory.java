package com.scottlogic.deg.generator.walker;

import java.nio.file.Path;

public interface DecisionTreeWalkerFactory {
    DecisionTreeWalker getDecisionTreeWalker(Path outputPath);
}


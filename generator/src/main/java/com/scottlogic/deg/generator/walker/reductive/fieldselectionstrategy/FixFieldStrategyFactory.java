package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.analysis.FieldDependencyAnalyser;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.GenerationConfig;

public class FixFieldStrategyFactory {
    private final FieldDependencyAnalyser analyser;

    @Inject
    public FixFieldStrategyFactory(FieldDependencyAnalyser analyser) {
        this.analyser = analyser;
    }

    public FixFieldStrategy getWalkerStrategy(
        Profile profile,
        DecisionTree tree,
        GenerationConfig generationConfig){

        return new HierarchicalDependencyFixFieldStrategy(profile, analyser, tree);
    }
}
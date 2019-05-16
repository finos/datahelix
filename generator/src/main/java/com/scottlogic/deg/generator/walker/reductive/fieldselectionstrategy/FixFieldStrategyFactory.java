package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.analysis.FieldDependencyAnalyser;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.config.details.TreeWalkerType;

public class FixFieldStrategyFactory {
    private final FieldDependencyAnalyser analyser;

    @Inject
    public FixFieldStrategyFactory(FieldDependencyAnalyser analyser) {
        this.analyser = analyser;
    }

    public FixFieldStrategy getWalkerStrategy(
        Profile profile,
        DecisionTree tree,
        GenerationConfigSource generationConfig){

        if (generationConfig.getWalkerType() != TreeWalkerType.REDUCTIVE){
            return null;
        }

        return new HierarchicalDependencyFixFieldStrategy(profile, analyser, tree);
    }
}
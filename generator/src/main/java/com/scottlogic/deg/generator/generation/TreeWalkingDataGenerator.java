package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.databags.ConcatenatingDataBagSource;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagSourceFactory;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategyFactory;

import java.util.stream.Stream;

public class TreeWalkingDataGenerator implements DataGenerator {

    private final DecisionTreeWalker treeWalker;
    private final RowSpecDataBagSourceFactory dataBagSourceFactory;
    private final FixFieldStrategyFactory walkerStrategyFactory;

    @Inject
    public TreeWalkingDataGenerator(
        DecisionTreeWalker treeWalker,
        RowSpecDataBagSourceFactory dataBagSourceFactory,
        FixFieldStrategyFactory walkerStrategyFactory) {
        this.treeWalker = treeWalker;
        this.dataBagSourceFactory = dataBagSourceFactory;
        this.walkerStrategyFactory = walkerStrategyFactory;
    }
    @Override
    public Stream<GeneratedObject> generateData(Profile profile, DecisionTree analysedProfile, GenerationConfig generationConfig) {
        FixFieldStrategy walkerStrategy = walkerStrategyFactory.getWalkerStrategy(profile, analysedProfile, generationConfig);

        Stream<RowSpec> walked = treeWalker.walk(analysedProfile, walkerStrategy);

        return new ConcatenatingDataBagSource(
            walked.map(dataBagSourceFactory::createDataBagSource))
            .generate(generationConfig);
    }
}

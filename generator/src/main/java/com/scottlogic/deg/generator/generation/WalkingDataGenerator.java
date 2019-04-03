package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.databags.ConcatenatingDataBagSource;
import com.scottlogic.deg.generator.generation.databags.DataBagSource;
import com.scottlogic.deg.generator.generation.databags.GeneratedObject;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagSourceFactory;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

import java.util.stream.Stream;

public class WalkingDataGenerator implements DataGenerator {

    private final DecisionTreeWalker treeWalker;
    private final RowSpecDataBagSourceFactory dataBagSourceFactory;
    private final GenerationConfig generationConfig;

    @Inject
    public WalkingDataGenerator(
        DecisionTreeWalker treeWalker,
        RowSpecDataBagSourceFactory dataBagSourceFactory,
        GenerationConfig generationConfig) {
        this.treeWalker = treeWalker;
        this.dataBagSourceFactory = dataBagSourceFactory;
        this.generationConfig = generationConfig;
    }

    @Override
    public Stream<GeneratedObject> generateData(Profile profile, DecisionTree analysedProfile) {
        Stream<RowSpec> rowSpecs = treeWalker.walk(analysedProfile);

        return generateDataFromRowSpecs(rowSpecs);
    }

    private Stream<GeneratedObject> generateDataFromRowSpecs(Stream<RowSpec> rowSpecs) {
        Stream<DataBagSource> dataBagSources = rowSpecs.map(dataBagSourceFactory::createDataBagSource);

        return new ConcatenatingDataBagSource(dataBagSources)
            .generate(generationConfig);
    }
}

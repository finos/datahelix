package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.rows.ConcatenatingRowSource;
import com.scottlogic.deg.generator.generation.rows.RowSource;
import com.scottlogic.deg.generator.generation.rows.Row;
import com.scottlogic.deg.generator.generation.rows.RowSourceFactory;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;

import java.util.stream.Stream;

/**
 * Generates data by walking a tree to get RowSpecs, then creating data from the RowSpecs
 */
public class CartesianRowSolver implements RowSolver {

    private final DecisionTreeWalker treeWalker;
    private final RowSourceFactory rowSourceFactory;
    private final GenerationConfig generationConfig;

    @Inject
    public CartesianRowSolver(
        DecisionTreeWalker treeWalker,
        RowSourceFactory rowSourceFactory,
        GenerationConfig generationConfig) {
        this.treeWalker = treeWalker;
        this.rowSourceFactory = rowSourceFactory;
        this.generationConfig = generationConfig;
    }

    @Override
    public Stream<Row> generateRows(Profile profile, DecisionTree analysedProfile) {
        Stream<RowSpec> rowSpecs = treeWalker.walk(analysedProfile);

        return generateDataFromRowSpecs(rowSpecs);
    }

    private Stream<Row> generateDataFromRowSpecs(Stream<RowSpec> rowSpecs) {
        Stream<RowSource> rowSourceStream = rowSpecs.map(rowSourceFactory::createRowSource);

        return new ConcatenatingRowSource(rowSourceStream)
            .generate(generationConfig);
    }
}

package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.databags.Row;

import java.util.stream.Stream;

public interface RowSolver {
    Stream<Row> generateRows(Profile profile, DecisionTree analysedProfile);
}
